// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

#include <Ice/Ice.h>
#include <Filesystem.h>
#include <iostream>
#include <iterator>
#include <stdexcept>

using namespace std;
using namespace Filesystem;

// Recursively print the contents of directory "dir" in tree fashion. 
// For files, show the contents of each file. The "depth"
// parameter is the current nesting level (for indentation).

static void
listRecursive(const DirectoryPrx& dir, int depth = 0)
{
    string indent(++depth, '\t');

    NodeSeq contents = dir->list();

    for(NodeSeq::const_iterator i = contents.begin(); i != contents.end(); ++i)
    {
        DirectoryPrx d = DirectoryPrx::checkedCast(*i);
        FilePrx file = FilePrx::uncheckedCast(*i);
        cout << indent << (*i)->name() << (d ? " (directory):" : " (file):") << endl;
        if(d)
        {
            listRecursive(d, depth);
        }
        else
        {
            Lines text = file->read();
            for(Lines::const_iterator j = text.begin(); j != text.end(); ++j)
            {
                cout << indent << "\t" << *j << endl;
            }
        }
    }
}

int
main(int argc, char* argv[])
{
    try
    {
        //
        // Create a communicator
        //
        Ice::CommunicatorHolder icHolder = Ice::initialize(argc, argv);

        //
        // Create a proxy for the root directory
        //
        Ice::ObjectPrx base = icHolder->stringToProxy("RootDir:default -h localhost -p 10000");

        //
        // Down-cast the proxy to a Directory proxy
        //
        DirectoryPrx rootDir = DirectoryPrx::checkedCast(base);
        if(!rootDir)
        {
            throw std::runtime_error("Invalid proxy");
        }

        //
        // Recursively list the contents of the root directory
        //
        cout << "Contents of root directory:" << endl;
        listRecursive(rootDir);
    }
    catch(const std::exception& ex)
    {
        cerr << ex.what() << endl;
        return 1;
    }
    return 0;
}
