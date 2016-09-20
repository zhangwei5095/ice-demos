// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

#import <objc/Ice.h>
#import <Printer.h>
#import <stdio.h>


int
main(int argc, char* argv[])
{
    int status = EXIT_FAILURE;
    @autoreleasepool
    {
        id<ICECommunicator> communicator = nil;
        @try
        {
            communicator = [ICEUtil createCommunicator:&argc argv:argv];
            id<ICEObjectPrx> base = [communicator stringToProxy:@"SimplePrinter:default -p 10000"];
            id<DemoPrinterPrx> printer = [DemoPrinterPrx checkedCast:base];
            if(!printer)
            {
                [NSException raise:@"Invalid proxy" format:@""];
            }
            [printer printString:@"Hello World!"];
            status = EXIT_SUCCESS;
        }
        @catch(NSException* ex)
        {
            NSLog(@"%@", ex);
        }

        @try
        {
            [communicator destroy];
        }
        @catch (NSException* ex)
        {
            NSLog(@"%@", ex);
        }
    }
    return status;
}
