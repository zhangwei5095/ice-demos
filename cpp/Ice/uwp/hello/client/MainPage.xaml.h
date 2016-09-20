﻿// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

#pragma once

#include "MainPage.g.h"
#include <IceUtil/IceUtil.h>
#include <Ice/Ice.h>
#include <Hello.h>

namespace hello
{

public ref class MainPage sealed
{
public:

    MainPage();

private:

    void updateProxy();
    bool isBatch();
    void print(const std::string&);

    void hello_Click(Platform::Object^ sender, Windows::UI::Xaml::RoutedEventArgs^ e);
    void shutdown_Click(Platform::Object^ sender, Windows::UI::Xaml::RoutedEventArgs^ e);
    void flush_Click(Platform::Object^ sender, Windows::UI::Xaml::RoutedEventArgs^ e);
    void mode_SelectionChanged(Platform::Object^ sender, Windows::UI::Xaml::Controls::SelectionChangedEventArgs^ e);
    void timeout_ValueChanged(Platform::Object^ sender, Windows::UI::Xaml::Controls::Primitives::RangeBaseValueChangedEventArgs^ e);
    void hostname_TextChanged(Platform::Object^ sender, Windows::UI::Xaml::Controls::TextChangedEventArgs^ e);
	void useDiscovery_Changed(Platform::Object^ sender, Windows::UI::Xaml::RoutedEventArgs^ e);

    Ice::CommunicatorPtr _communicator;
    Demo::HelloPrx _helloPrx;
    bool _response;
};

}
