// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

#include <Ice/Ice.h>
#include <IceGrid/IceGrid.h>

using namespace std;
using namespace IceGrid;

namespace
{

class RegistryPluginI : public Ice::Plugin
{
public:

    RegistryPluginI(const Ice::CommunicatorPtr&);

    virtual void initialize();
    virtual void destroy();

private:

    // Required to prevent compiler warnings with MSVC++
    RegistryPluginI& operator=(const RegistryPluginI&);

    const Ice::CommunicatorPtr _communicator;
};

class ReplicaGroupFilterI : public IceGrid::ReplicaGroupFilter
{
public:

    ReplicaGroupFilterI(const RegistryPluginFacadePtr&);

    virtual Ice::StringSeq filter(const string&, const Ice::StringSeq&, const Ice::ConnectionPtr&, const Ice::Context&);

private:

    RegistryPluginFacadePtr _facade;
};

}

//
extern "C"
{

ICE_DECLSPEC_EXPORT Ice::Plugin*
createRegistryPlugin(const Ice::CommunicatorPtr& communicator, const string&, const Ice::StringSeq&)
{
    return new RegistryPluginI(communicator);
}

}

RegistryPluginI::RegistryPluginI(const Ice::CommunicatorPtr& communicator) : _communicator(communicator)
{
}

void
RegistryPluginI::initialize()
{
    IceGrid::RegistryPluginFacadePtr facade = IceGrid::getRegistryPluginFacade();
    if(facade)
    {
        facade->addReplicaGroupFilter("filterByCurrency", new ReplicaGroupFilterI(facade));
    }
}

void
RegistryPluginI::destroy()
{
}

ReplicaGroupFilterI::ReplicaGroupFilterI(const IceGrid::RegistryPluginFacadePtr& facade) : _facade(facade)
{
}

Ice::StringSeq
ReplicaGroupFilterI::filter(const string& /* replicaGroupId */,
                            const Ice::StringSeq& adapters,
                            const Ice::ConnectionPtr&,
                            const Ice::Context& ctx)
{
    Ice::Context::const_iterator p = ctx.find("currency");
    if(p == ctx.end())
    {
        return adapters;
    }

    string currency = p->second;

    //
    // Get the Currencies property value from the server descriptor
    // that owns the adapter and only keep adapters for servers that
    // are configured with the currency specified in the client
    // context.
    //
    Ice::StringSeq filteredAdapters;
    for(Ice::StringSeq::const_iterator q = adapters.begin(); q != adapters.end(); ++q)
    {
        if(_facade->getPropertyForAdapter(*q, "Currencies").find(currency) != string::npos)
        {
            filteredAdapters.push_back(*q);
        }
    }
    return filteredAdapters;
}
