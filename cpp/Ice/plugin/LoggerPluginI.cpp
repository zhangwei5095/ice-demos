// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

#include <Ice/Ice.h>

using namespace std;

namespace
{

class LoggerI : public Ice::Logger
{
public:

    virtual void
    print(const string& message)
    {
        cout << "PRINT: " << message << endl;
    }

    virtual void
    trace(const string& category, const string& message)
    {
        cout << "TRACE(" << category << "): " << message << endl;
    }

    virtual void
    warning(const string& message)
    {
        cout << "WARNING: " << message << endl;
    }

    virtual void
    error(const string& message)
    {
        cout << "ERROR: " << message << endl;
    }

    virtual string
    getPrefix()
    {
        return "";
    }

    virtual Ice::LoggerPtr
    cloneWithPrefix(const std::string&)
    {
        return new LoggerI;   
    }
};

}

extern "C"
{

ICE_DECLSPEC_EXPORT ::Ice::Plugin*
createLogger(const Ice::CommunicatorPtr& communicator, const string&, const Ice::StringSeq&)
{
    return new Ice::LoggerPlugin(communicator, new LoggerI);
}

}
