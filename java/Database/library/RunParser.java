// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

import Demo.*;

class RunParser
{
    //
    // Adapter for the two types of session objects.
    //
    interface SessionAdapter
    {
        public LibraryPrx getLibrary();
        public void destroy();
        public void refresh();
        public long getTimeout();
    }

    static SessionAdapter createSession(String appName, com.zeroc.Ice.Communicator communicator)
    {
        SessionAdapter session;
        final com.zeroc.Glacier2.RouterPrx router =
            com.zeroc.Glacier2.RouterPrx.uncheckedCast(communicator.getDefaultRouter());
        if(router != null)
        {
            com.zeroc.Glacier2.SessionPrx glacier2session = null;
            long timeout;
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            while(true)
            {
                System.out.println("This demo accepts any user-id / password combination.");
                try
                {
                    String id;
                    System.out.print("user id: ");
                    System.out.flush();
                    id = in.readLine();

                    String pw;
                    System.out.print("password: ");
                    System.out.flush();
                    pw = in.readLine();

                    try
                    {
                        glacier2session = router.createSession(id, pw);
                        timeout = router.getSessionTimeout() / 2;
                        break;
                    }
                    catch(com.zeroc.Glacier2.PermissionDeniedException ex)
                    {
                        System.out.println("permission denied:\n" + ex.reason);
                    }
                    catch(com.zeroc.Glacier2.CannotCreateSessionException ex)
                    {
                        System.out.println("cannot create session:\n" + ex.reason);
                    }
                }
                catch(java.io.IOException ex)
                {
                    ex.printStackTrace();
                }
            }
            final long to = timeout;
            final Glacier2SessionPrx sess = Glacier2SessionPrx.uncheckedCast(glacier2session);
            session = new SessionAdapter()
            {
                @Override
                public LibraryPrx getLibrary()
                {
                    return sess.getLibrary();
                }

                @Override
                public void destroy()
                {
                    try
                    {
                        router.destroySession();
                    }
                    catch(com.zeroc.Glacier2.SessionNotExistException ex)
                    {
                    }
                    catch(com.zeroc.Ice.ConnectionLostException ex)
                    {
                        //
                        // Expected: the router closed the connection.
                        //
                    }
                }

                @Override
                public void refresh()
                {
                    sess.refresh();
                }

                @Override
                public long getTimeout()
                {
                    return to;
                }
            };
        }
        else
        {
            SessionFactoryPrx factory = SessionFactoryPrx.checkedCast(
                communicator.propertyToProxy("SessionFactory.Proxy"));
            if(factory == null)
            {
                System.err.println(appName + ": invalid object reference");
                return null;
            }

            final SessionPrx sess = factory.create();
            final long timeout = factory.getSessionTimeout()/2;
            session = new SessionAdapter()
            {
                @Override
                public LibraryPrx getLibrary()
                {
                    return sess.getLibrary();
                }

                @Override
                public void destroy()
                {
                    sess.destroy();
                }

                @Override
                public void refresh()
                {
                    sess.refresh();
                }

                @Override
                public long getTimeout()
                {
                    return timeout;
                }
            };
        }
        return session;
    }

    static int runParser(String appName, String[] args, final com.zeroc.Ice.Communicator communicator)
    {
        final SessionAdapter session = createSession(appName, communicator);
        if(session == null)
        {
            return 1;
        }

        java.util.concurrent.ScheduledExecutorService executor =
            java.util.concurrent.Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() ->
            {
                try
                {
                    session.refresh();
                }
                catch(com.zeroc.Ice.LocalException ex)
                {
                    communicator.getLogger().warning("SessionRefreshThread: " + ex);
                    // Exceptions thrown from the executor task supress subsequent execution
                    // of the task.
                    throw ex;
                }
            }, session.getTimeout(), session.getTimeout(), java.util.concurrent.TimeUnit.SECONDS);

        LibraryPrx library = session.getLibrary();

        Parser parser = new Parser(communicator, library);

        int rc = 0;

        if(args.length == 1)
        {
            rc = parser.parse(args[0]);
        }

        if(rc == 0)
        {
            rc = parser.parse();
        }

        executor.shutdown();
        session.destroy();

        return rc;
    }
}
