// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

using Demo;

class NestedI : NestedDisp_
{
    internal NestedI(NestedPrx self)
    {
        _self = self;
    }
    
    public override void nestedCall(int level, NestedPrx proxy, Ice.Current current)
    {
        System.Console.Out.WriteLine("" + level);
        if(--level > 0)
        {
            //
            // Ensure the invocation times out if the nesting level is too
            // high and there are no more threads in the thread pool to
            // dispatch the call.
            //
            ((NestedPrx)proxy.ice_invocationTimeout(5000)).nestedCall(level, _self);
        }
    }
    
    private NestedPrx _self;
}
