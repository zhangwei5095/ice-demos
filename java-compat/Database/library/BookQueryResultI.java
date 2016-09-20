// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

import Demo.*;

class BookQueryResultI extends _BookQueryResultDisp
{
    BookQueryResultI(SQLRequestContext context, java.sql.ResultSet rs, Ice.ObjectAdapter adapter) throws java.sql.SQLException
    {
        _books = new java.util.Stack<BookDescription>();
        for(int i = 0; i < MAX_BOOK_QUERY_RESULT; ++i)
        {
            _books.add(BookI.extractDescription(context, rs, adapter));
            if(!rs.next())
            {
                break;
            }
        }
    }

    @Override
    synchronized public java.util.List<BookDescription>
    next(int n, Ice.BooleanHolder destroyed, Ice.Current current)
    {
        if(_destroyed)
        {
            throw new Ice.ObjectNotExistException();
        }
        destroyed.value = false;
        java.util.List<BookDescription> l = new java.util.LinkedList<BookDescription>();
        if(n <= 0)
        {
            return l;
        }

        for(int i = 0; i < n && _books.size() > 0; ++i)
        {
            l.add(_books.pop());
        }

        if(_books.size() <= 0)
        {
            try
            {
                destroyed.value = true;
                destroy(current);
            }
            catch(Exception e)
            {
                // Ignore.
            }
        }

        return l;
    }

    @Override
    synchronized public void
    destroy(Ice.Current current)
    {
        if(_destroyed)
        {
            throw new Ice.ObjectNotExistException();
        }
        _destroyed = true;

        current.adapter.remove(current.id);
    }

    // Called on application shutdown by the Library.
    synchronized public void
    shutdown()
    {
        if(!_destroyed)
        {
            _destroyed = true;
        }
    }

    private java.util.Stack<BookDescription> _books;
    private boolean _destroyed = false;
    private static final int MAX_BOOK_QUERY_RESULT = 1000;
}

