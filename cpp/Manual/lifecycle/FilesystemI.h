// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

#ifndef __FilesystemI_h__
#define __FilesystemI_h__

#include <Ice/Ice.h>
#include <IceUtil/IceUtil.h>
#include <Filesystem.h>
#include <map>

#ifdef _MSC_VER
#   pragma warning(push)
#   pragma warning(disable:4250) // ... : inherits ... via dominance
#endif

namespace FilesystemI
{
    class DirectoryI;
    typedef IceUtil::Handle<DirectoryI> DirectoryIPtr;

    class NodeI : virtual public Filesystem::Node
    {
    public:

        virtual std::string name(const Ice::Current&);
        Ice::Identity id() const;

    protected:

        NodeI(const ::std::string&, const DirectoryIPtr&);

        const ::std::string _name;
        const DirectoryIPtr _parent;
        bool _destroyed;
        Ice::Identity _id;
        IceUtil::Mutex _m;
    };
    typedef IceUtil::Handle<NodeI> NodeIPtr;

    class FileI : public Filesystem::File, public NodeI
    {
    public:

        virtual Filesystem::Lines read(const Ice::Current&);
        virtual void write(const Filesystem::Lines&, const Ice::Current&);
        virtual void destroy(const Ice::Current&);

        FileI(const std::string&, const DirectoryIPtr&);

    private:

        Filesystem::Lines _lines;
    };
    typedef IceUtil::Handle<FileI> FileIPtr;

    class DirectoryI :  public NodeI, public Filesystem::Directory
    {
    public:

        virtual Filesystem::NodeDescSeq list(const Ice::Current&);
        virtual Filesystem::NodeDesc find(const std::string& name, const Ice::Current&);
        Filesystem::FilePrx createFile(const ::std::string&, const Ice::Current&);
        Filesystem::DirectoryPrx createDirectory(const ::std::string&, const Ice::Current&);
        virtual void destroy(const Ice::Current&);

        DirectoryI(const std::string& = "/", const DirectoryIPtr& = 0);

        void removeEntry(const ::std::string&);

    private:

        typedef ::std::map< ::std::string, NodeIPtr> Contents;
        Contents _contents;
    };
}

#ifdef _MSC_VER
#   pragma warning(pop)
#endif

#endif
