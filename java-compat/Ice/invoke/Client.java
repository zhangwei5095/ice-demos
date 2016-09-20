// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

public class Client extends Ice.Application
{
    private static void
    menu()
    {
        System.out.println(
            "usage:\n" +
            "1: print string\n" +
            "2: print string sequence\n" +
            "3: print dictionary\n" +
            "4: print enum\n" +
            "5: print struct\n" +
            "6: print struct sequence\n" +
            "7: print class\n" +
            "8: get values\n" +
            "9: throw exception\n" +
            "s: shutdown server\n" +
            "x: exit\n" +
            "?: help\n");
    }

    class ShutdownHook extends Thread
    {
        @Override
        public void
        run()
        {
            try
            {
                communicator().destroy();
            }
            catch(Ice.LocalException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public int
    run(String[] args)
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

        Ice.ObjectPrx obj = communicator().propertyToProxy("Printer.Proxy");

        menu();

        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

        String line = null;
        do
        {
            try
            {
                System.out.print("==> ");
                System.out.flush();
                line = reader.readLine();
                if(line == null)
                {
                    break;
                }
                if(line.equals("1"))
                {
                    //
                    // Marshal the in parameter.
                    //
                    Ice.OutputStream out = new Ice.OutputStream(communicator());
                    out.startEncapsulation();
                    out.writeString("The streaming API works!");
                    out.endEncapsulation();

                    //
                    // Invoke operation.
                    //
                    if(!obj.ice_invoke("printString", Ice.OperationMode.Normal, out.finished(), null))
                    {
                        System.out.println("Unknown user exception");
                    }
                }
                else if(line.equals("2"))
                {
                    //
                    // Marshal the in parameter.
                    //
                    Ice.OutputStream out = new Ice.OutputStream(communicator());
                    out.startEncapsulation();
                    final String[] arr = { "The", "streaming", "API", "works!" };
                    Demo.StringSeqHelper.write(out, arr);
                    out.endEncapsulation();

                    //
                    // Invoke operation.
                    //
                    if(!obj.ice_invoke("printStringSequence", Ice.OperationMode.Normal, out.finished(), null))
                    {
                        System.out.println("Unknown user exception");
                    }
                }
                else if(line.equals("3"))
                {
                    //
                    // Marshal the in parameter.
                    //
                    Ice.OutputStream out = new Ice.OutputStream(communicator());
                    out.startEncapsulation();
                    java.util.Map<String, String> dict = new java.util.HashMap<String, String>();
                    dict.put("The", "streaming");
                    dict.put("API", "works!");
                    Demo.StringDictHelper.write(out, dict);
                    out.endEncapsulation();

                    //
                    // Invoke operation.
                    //
                    if(!obj.ice_invoke("printDictionary", Ice.OperationMode.Normal, out.finished(), null))
                    {
                        System.out.println("Unknown user exception");
                    }
                }
                else if(line.equals("4"))
                {
                    //
                    // Marshal the in parameter.
                    //
                    Ice.OutputStream out = new Ice.OutputStream(communicator());
                    out.startEncapsulation();
                    Demo.Color.write(out, Demo.Color.green);
                    out.endEncapsulation();

                    //
                    // Invoke operation.
                    //
                    if(!obj.ice_invoke("printEnum", Ice.OperationMode.Normal, out.finished(), null))
                    {
                        System.out.println("Unknown user exception");
                    }
                }
                else if(line.equals("5"))
                {
                    //
                    // Marshal the in parameter.
                    //
                    Ice.OutputStream out = new Ice.OutputStream(communicator());
                    out.startEncapsulation();
                    Demo.Structure s = new Demo.Structure();
                    s.name = "red";
                    s.value = Demo.Color.red;
                    Demo.Structure.write(out, s);
                    out.endEncapsulation();

                    //
                    // Invoke operation.
                    //
                    if(!obj.ice_invoke("printStruct", Ice.OperationMode.Normal, out.finished(), null))
                    {
                        System.out.println("Unknown user exception");
                    }
                }
                else if(line.equals("6"))
                {
                    //
                    // Marshal the in parameter.
                    //
                    Ice.OutputStream out = new Ice.OutputStream(communicator());
                    out.startEncapsulation();
                    Demo.Structure[] arr = new Demo.Structure[3];
                    arr[0] = new Demo.Structure();
                    arr[0].name = "red";
                    arr[0].value = Demo.Color.red;
                    arr[1] = new Demo.Structure();
                    arr[1].name = "green";
                    arr[1].value = Demo.Color.green;
                    arr[2] = new Demo.Structure();
                    arr[2].name = "blue";
                    arr[2].value = Demo.Color.blue;
                    Demo.StructureSeqHelper.write(out, arr);
                    out.endEncapsulation();

                    //
                    // Invoke operation.
                    //
                    if(!obj.ice_invoke("printStructSequence", Ice.OperationMode.Normal, out.finished(), null))
                    {
                        System.out.println("Unknown user exception");
                    }
                }
                else if(line.equals("7"))
                {
                    //
                    // Marshal the in parameter.
                    //
                    Ice.OutputStream out = new Ice.OutputStream(communicator());
                    out.startEncapsulation();
                    Demo.C c = new Demo.C();
                    c.s = new Demo.Structure();
                    c.s.name = "blue";
                    c.s.value = Demo.Color.blue;
                    out.writeValue(c);
                    out.writePendingValues();
                    out.endEncapsulation();

                    //
                    // Invoke operation.
                    //
                    if(!obj.ice_invoke("printClass", Ice.OperationMode.Normal, out.finished(), null))
                    {
                        System.out.println("Unknown user exception");
                    }
                }
                else if(line.equals("8"))
                {
                    //
                    // Invoke operation.
                    //
                    Ice.ByteSeqHolder outParams = new Ice.ByteSeqHolder();
                    if(!obj.ice_invoke("getValues", Ice.OperationMode.Normal, null, outParams))
                    {
                        System.out.println("Unknown user exception");
                        continue;
                    }

                    //
                    // Unmarshal the results.
                    //
                    Ice.InputStream in = new Ice.InputStream(communicator(), outParams.value);
                    in.startEncapsulation();
                    Demo.CHolder c = new Demo.CHolder();
                    in.readValue(c);
                    String str = in.readString();
                    in.readPendingValues();
                    in.endEncapsulation();
                    System.out.println("Got string `" + str + "' and class: s.name=" + c.value.s.name +
                        ", s.value=" + c.value.s.value);
                }
                else if(line.equals("9"))
                {
                    //
                    // Invoke operation.
                    //
                    Ice.ByteSeqHolder outParams = new Ice.ByteSeqHolder();
                    if(obj.ice_invoke("throwPrintFailure", Ice.OperationMode.Normal, null, outParams))
                    {
                        System.out.println("Expected exception");
                        continue;
                    }

                    Ice.InputStream in = new Ice.InputStream(communicator(), outParams.value);
                    in.startEncapsulation();
                    try
                    {
                        in.throwException();
                    }
                    catch(Demo.PrintFailure ex)
                    {
                        // Expected.
                    }
                    catch(Ice.UserException ex)
                    {
                        System.out.println("Unknown user exception");
                    }
                    in.endEncapsulation();
                }
                else if(line.equals("s"))
                {
                    obj.ice_invoke("shutdown", Ice.OperationMode.Normal, null, null);
                }
                else if(line.equals("x"))
                {
                    // Nothing to do.
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
            catch(Ice.LocalException ex)
            {
                ex.printStackTrace();
            }
        }
        while(!line.equals("x"));

        return 0;
    }

    public static void
    main(String[] args)
    {
        Client app = new Client();
        int status = app.main("Client", args, "config.client");
        System.exit(status);
    }
}
