// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

#ifndef LOG_I_H
#define LOG_I_H

class LogI : public Ice::Logger
{
public:

    LogI();

    virtual void print(const std::string&);
    virtual void trace(const std::string&, const std::string&);
    virtual void warning(const std::string&);
    virtual void error(const std::string&);
    virtual std::string getPrefix();
    virtual Ice::LoggerPtr cloneWithPrefix(const std::string&);

    void message(const std::string&);
    void setHandle(HWND);

private:

    void post(const std::string&);

    std::string _buffer;
    HWND _hwnd;
};

typedef IceUtil::Handle<LogI> LogIPtr;

#endif
