# **********************************************************************
#
# Copyright (c) 2003-2015 ZeroC, Inc. All rights reserved.
#
# This copy of Ice is licensed to you under the terms described in the
# ICE_LICENSE file included in this distribution.
#
# **********************************************************************

$(demo)_server_sources	= Server.cpp Nrvo.ice MyStringSeq.cpp NrvoI.cpp
$(demo)_client_sources	= Client.cpp Nrvo.ice MyStringSeq.cpp

demos += $(demo)
