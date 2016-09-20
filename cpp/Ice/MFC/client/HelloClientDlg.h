// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************


#ifndef HELLO_CLIENT_DLG_H
#define HELLO_CLIENT_DLG_H

#include "Hello.h"

#pragma once

class CHelloClientDlg : public CDialog
{
public:

    CHelloClientDlg(CWnd* = NULL);

    enum { IDD = IDD_HELLOCLIENT_DIALOG };

    afx_msg void OnCbnSelchangeMode();

    void exception(const Ice::Exception&);
    void response();;
    void sent();
    void flushed();

protected:

    virtual void DoDataExchange(CDataExchange*);    // DDX/DDV support

    Ice::CommunicatorPtr _communicator;
    Demo::HelloPrx _helloPrx;
    Demo::Callback_Hello_sayHelloPtr _sayHelloCallback;
    Demo::Callback_Hello_shutdownPtr _shutdownCallback;
    Ice::Callback_Object_ice_flushBatchRequestsPtr _flushCallback;
    CEdit* _host;
    CComboBox* _mode;
    CSliderCtrl* _timeout;
    CStatic* _timeoutStatus;
    CSliderCtrl* _delay;
    CStatic* _delayStatus;
    CStatic* _status;
    CButton* _flush;
    HICON _hIcon;

    // Generated message map functions
    virtual BOOL OnInitDialog();
    afx_msg void OnClose();
    afx_msg void OnPaint();
    afx_msg HCURSOR OnQueryDragIcon();
    afx_msg void OnHostChanged();
    afx_msg void OnModeChanged();
    afx_msg void OnHScroll(UINT, UINT, CScrollBar*);
    afx_msg void OnSayHello();
    afx_msg void OnFlush();
    afx_msg void OnShutdown();
    afx_msg LRESULT OnAMICallback(WPARAM, LPARAM);
    DECLARE_MESSAGE_MAP()

private:

    void updateProxy();
    BOOL deliveryModeIsBatch();
    void handleException(const IceUtil::Exception&);
};

#endif
