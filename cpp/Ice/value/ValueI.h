// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

#ifndef VALUE_I_H
#define VALUE_I_H

#include <Value.h>

class InitialI : public Demo::Initial
{
public:

    InitialI(const Ice::ObjectAdapterPtr&);

    virtual Demo::SimplePtr getSimple(const Ice::Current&);
    virtual void getPrinter(::Demo::PrinterPtr&, Demo::PrinterPrx&, const Ice::Current&);
    virtual Demo::PrinterPtr getDerivedPrinter(const Ice::Current&);
    virtual Demo::PrinterPtr updatePrinterMessage(const Demo::PrinterPtr&, const Ice::Current&);
    virtual void throwDerivedPrinter(const Ice::Current&);
    virtual void shutdown(const Ice::Current&);

private:

    // Required to prevent compiler warnings with MSVC++
    InitialI& operator=(const InitialI&);

    const Demo::SimplePtr _simple;
    const Demo::PrinterPtr _printer;
    const Demo::PrinterPrx _printerProxy;
    const Demo::DerivedPrinterPtr _derivedPrinter;
};

#ifdef _MSC_VER
#   pragma warning(push)
#   pragma warning(disable:4250) // ... : inherits ... via dominance
#endif

//
// Virtual inheritance because we plan to reuse this implementation in DerivedPrinterI
//
class PrinterI : virtual public Demo::Printer
{
public:

    virtual void printBackwards(const Ice::Current&);
};

class DerivedPrinterI : public Demo::DerivedPrinter, public PrinterI
{
public:

    virtual void printUppercase(const Ice::Current&);
};

class ClientPrinterI : public Demo::ClientPrinter, public PrinterI
{
};

#ifdef _MSC_VER
#   pragma warning(pop)
#endif

#endif
