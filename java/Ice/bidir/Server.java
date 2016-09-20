// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

public class Server extends com.zeroc.Ice.Application
{
    @Override
    public int run(String[] args)
    {
        if(args.length > 0)
        {
            System.err.println(appName() + ": too many arguments");
            return 1;
        }

        com.zeroc.Ice.ObjectAdapter adapter = communicator().createObjectAdapter("Callback.Server");
        CallbackSenderI sender = new CallbackSenderI(communicator());
        adapter.add(sender, com.zeroc.Ice.Util.stringToIdentity("sender"));
        adapter.activate();

        Thread t = new Thread(sender);
        t.start();

        try
        {
            communicator().waitForShutdown();
        }
        finally
        {
            sender.destroy();
            try
            {
                t.join();
            }
            catch(java.lang.InterruptedException ex)
            {
            }
        }

        return 0;
    }

    public static void main(String[] args)
    {
        Server app = new Server();
        int status = app.main("Server", args, "config.server");
        System.exit(status);
    }
}
