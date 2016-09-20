// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

import Demo.*;

public class Client extends com.zeroc.Ice.Application
{
    class ShutdownHook extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                communicator().destroy();
            }
            catch(com.zeroc.Ice.LocalException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private static void menu()
    {
        System.out.println(
            "usage:\n" +
            "1: call with no request context\n" +
            "2: call with explicit request context\n" +
            "3: call with per-proxy request context\n" +
            "4: call with implicit request context\n" +
            "s: shutdown server\n" +
            "x: exit\n" +
            "?: help\n");
    }

    @Override
    public int run(String[] args)
    {
        if(args.length > 0)
        {
            System.err.println(appName() + ": too many arguments");
            return 1;
        }

        //
        // Since this is an interactive demo we want to clear the
        // Application installed interrupt callback and install our
        // own shutdown hook.
        //
        setInterruptHook(new ShutdownHook());

        ContextPrx proxy = ContextPrx.checkedCast(communicator().propertyToProxy("Context.Proxy"));
        if(proxy == null)
        {
            System.err.println("invalid proxy");
            return 1;
        }

        menu();

        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

        String line = null;
        do
        {
            try
            {
                System.out.print("==> ");
                System.out.flush();
                line = in.readLine();
                if(line == null)
                {
                    break;
                }
                if(line.equals("1"))
                {
                    proxy.call();
                }
                else if(line.equals("2"))
                {
                    java.util.Map<String, String> ctx = new java.util.HashMap<String, String>();
                    ctx.put("type", "Explicit");
                    proxy.call(ctx);
                }
                else if(line.equals("3"))
                {
                    java.util.Map<String, String> ctx = new java.util.HashMap<>();
                    ctx.put("type", "Per-Proxy");
                    ContextPrx proxy2 = proxy.ice_context(ctx);
                    proxy2.call();
                }
                else if(line.equals("4"))
                {
                    com.zeroc.Ice.ImplicitContext ic = communicator().getImplicitContext();
                    java.util.Map<String, String> ctx = new java.util.HashMap<>();
                    ctx.put("type", "Implicit");
                    ic.setContext(ctx);
                    proxy.call();
                    ic.setContext(new java.util.HashMap<>());
                }
                else if(line.equals("s"))
                {
                    proxy.shutdown();
                }
                else if(line.equals("x"))
                {
                    // Nothing to do
                }
                else if(line.equals("?"))
                {
                    menu();
                }
                else
                {
                    System.out.println("unknown command `" + line + "'");
                    menu();
                }
            }
            catch(java.io.IOException ex)
            {
                ex.printStackTrace();
            }
            catch(com.zeroc.Ice.LocalException ex)
            {
                ex.printStackTrace();
            }
        }
        while(!line.equals("x"));

        return 0;
    }

    public static void main(String[] args)
    {
        Client app = new Client();
        int status = app.main("Client", args, "config.client");
        System.exit(status);
    }
}

