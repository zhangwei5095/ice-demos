// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

#include <stdafx.h>
#include <LogI.h>

using namespace std;

LogI::LogI() :
    _hwnd(0)
{
}

void
LogI::print(const string& msg)
{
    string s = msg;

    string::size_type idx = 0;
    while((idx = s.find("\n", idx)) != string::npos)
    {
        s.replace(idx, 1, "\r\n  ");
        idx += 3;
    }

    message(s);
}

void
LogI::trace(const string& category, const string& msg)
{
    string s = "[ " + category + ": " + msg + " ]";

    string::size_type idx = 0;
    while((idx = s.find("\n", idx)) != string::npos)
    {
        s.replace(idx, 1, "\r\n  ");
        idx += 3;
    }

    message(s);
}

void
LogI::warning(const string& msg)
{
    message("warning: " + msg);
}

void
LogI::error(const string& msg)
{
    message("error: " + msg);
}

string
LogI::getPrefix()
{
    return "";
}

Ice::LoggerPtr
LogI::cloneWithPrefix(const string&)
{
    return this;
}

void
LogI::message(const string& msg)
{
    string line = msg + "\r\n";
    if(_hwnd)
    {
        post(line);
    }
    else
    {
        _buffer.append(line);
    }
}

void
LogI::setHandle(HWND hwnd)
{
    _hwnd = hwnd;
    if(_hwnd != 0 && !_buffer.empty())
    {
        post(_buffer);
        _buffer.clear();
    }
}

void
LogI::post(const string& data)
{
    assert(_hwnd != 0);
    char* text = new char[data.size()+1];
#ifdef _WIN32
    strcpy_s(text, data.size() + 1, data.c_str());    
#else
    strcpy(text, data.c_str());
#endif
    ::PostMessage(_hwnd, WM_USER, (WPARAM)FALSE, (LPARAM)text);
}
