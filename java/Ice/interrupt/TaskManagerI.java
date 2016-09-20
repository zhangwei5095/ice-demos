// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

import java.util.concurrent.ExecutorService;

import Demo.*;

public class TaskManagerI implements TaskManager
{
    public TaskManagerI(ExecutorService executor)
    {
        _executor = executor;
    }

    @Override
    public void run(int id, com.zeroc.Ice.Current current)
    {
        System.out.println("starting task " + id);
        // Sleep for 10 seconds.
        try
        {
            Thread.sleep(10000);
            System.out.println("stopping task " + id);
        }
        catch(InterruptedException ex)
        {
            //
            // We are done, the server is shutting down.
            //
            System.out.println("interrupted task " + id);
        }
    }

    @Override
    public void shutdown(com.zeroc.Ice.Current current)
    {
        System.out.println("Shutting down...");
        //
        // Call shutdownNow on the executor. This interrupts all
        // executor threads causing any running upcalls to terminate
        // quickly.
        //
        _executor.shutdownNow();
        current.adapter.getCommunicator().shutdown();
    }
    
    private ExecutorService _executor;
}
