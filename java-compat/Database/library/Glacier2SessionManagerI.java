// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

import Demo.*;

class Glacier2SessionManagerI extends Glacier2._SessionManagerDisp
{
    @Override
    public Glacier2.SessionPrx
    create(String userId, Glacier2.SessionControlPrx control, Ice.Current c)
    {
        SessionI session = new SessionI(_logger, c.adapter);
        _Glacier2SessionTie servant = new _Glacier2SessionTie(session);

        Glacier2.SessionPrx proxy = Glacier2.SessionPrxHelper.uncheckedCast(c.adapter.addWithUUID(servant));

        _logger.trace("SessionFactory", "create new session: " +
                      Ice.Util.identityToString(proxy.ice_getIdentity()));

        _reaper.add(proxy, session);

        return proxy;
    }

    Glacier2SessionManagerI(Ice.Logger logger, ReapTask reaper)
    {
        _logger = logger;
        _reaper = reaper;
    }

    private Ice.Logger _logger;
    private ReapTask _reaper;
}
