// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

(function(){

//
// Initialize the communicator.
//
const communicator = Ice.initialize();

function sayHello()
{
    Ice.Promise.try(() =>
        {
            setState(State.Busy);
            
            //
            // Create a proxy for the hello object
            //
            const hostname = document.location.hostname || "127.0.0.1";
            const proxy = communicator.stringToProxy("hello:ws -h " + hostname + " -p 10000");
            
            //
            // Down-cast this proxy to the derived interface Demo::Hello
            // using checkedCast, and invoke the sayHello operation if 
            // the checkedCast succeeds.
            //
            return Demo.HelloPrx.checkedCast(proxy).then(hello => hello.sayHello());
        }
    ).catch(ex =>
        {
            //
            // Handle any exceptions thrown above.
            //
            $("#output").val(ex.toString());
        }
    ).finally(() =>
        {
            setState(State.Idle);
        });
    return false;
}

//
// Handle the client state
//
const State =
{
    Idle: 0,
    Busy: 1
};

var state;

function setState(newState)
{
    function assert(v)
    {
        if(!v)
        {
            throw new Error("Assertion failed");
        }
    }

    assert(state !== newState);
    switch(newState)
    {
        case State.Idle:
        {
            assert(state === undefined || state == State.Busy);

            //
            // Hide the progress indicator.
            //
            $("#progress").hide();
            $("body").removeClass("waiting");
            
            //
            // Enable buttons
            //
            $("#hello").removeClass("disabled").click(sayHello);
            break;
        }
        case State.Busy:
        {
            assert(state == State.Idle);

            //
            // Clear any previous error messages.
            //
            $("#output").val("");
            
            //
            // Disable buttons.
            //
            $("#hello").addClass("disabled").off("click");
            
            //
            // Display the progress indicator and set the wait cursor.
            //
            $("#progress").show();
            $("body").addClass("waiting");
            break;
        }
    }
    state = newState;
}

//
// Start in the idle state
//
setState(State.Idle);

}());
