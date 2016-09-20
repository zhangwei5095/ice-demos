// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

#include "stdafx.h"
#include "PatchClient.h"
#include "PatchClientDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

BEGIN_MESSAGE_MAP(CPatchClientApp, CWinApp)
    ON_COMMAND(ID_HELP, CWinApp::OnHelp)
END_MESSAGE_MAP()

using namespace std;

CPatchClientApp::CPatchClientApp()
{
    // Place all significant initialization in InitInstance
}

// The one and only CPatchClientApp object

CPatchClientApp theApp;

BOOL
CPatchClientApp::InitInstance()
{
    // InitCommonControls() is required on Windows XP if an application
    // manifest specifies use of ComCtl32.dll version 6 or later to enable
    // visual styles.  Otherwise, any window creation will fail.
    InitCommonControls();

    CWinApp::InitInstance();

    //
    // Create a communicator.
    //
    Ice::CommunicatorPtr communicator;
    try
    {
        Ice::StringSeq args = Ice::argsToStringSeq(__argc, __wargv);
        communicator = Ice::initialize(args);
    }
    catch(const IceUtil::Exception& ex)
    {
        ostringstream ostr;
        ostr << ex;
        string s = ostr.str();
        AfxMessageBox(CString(s.c_str()), MB_OK|MB_ICONEXCLAMATION);
        return FALSE;
    }

    Ice::PropertiesPtr properties = communicator->getProperties();
    if(properties->getProperty("IcePatch2Client.Proxy").empty())
    {
        properties->setProperty("IcePatch2Client.Proxy", "IcePatch2/server:tcp -h localhost -p 10000");
    }

    CPatchDlg dlg(communicator);
    m_pMainWnd = &dlg;
    dlg.DoModal();

    //
    // Clean up.
    //
    try
    {
        communicator->destroy();
    }
    catch(const IceUtil::Exception&)
    {
    }

    // Since the dialog has been closed, return FALSE so that we exit the
    // application, rather than start the application's message pump.
    return FALSE;
}
