/***************************************************************************
 begin........: September 2018
 copyright....: Sebastian Fedrau
 email........: sebastian.fedrau@gmail.com
 ***************************************************************************/

/***************************************************************************
 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License v3 as published by
 the Free Software Foundation.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License v3 for more details.
 ***************************************************************************/
package de.dixieflatline.mpcw.services.implementation;

import android.util.*;

import java.util.*;

import de.dixieflatline.mpcw.client.*;
import de.dixieflatline.mpcw.client.Connection;

public class AsyncConnectionLoop implements Runnable
{
    private final List<IConnectionListener> listeners = new ArrayList<IConnectionListener>();
    private final IConnection connection;
    private final Loop loop = new Loop();
    private boolean oldState;

    public AsyncConnectionLoop(String connectionString)
    {
        connection = createConnection(connectionString);
    }

    public IConnection getConnection()
    {
        return connection;
    }

    public void addListener(IConnectionListener listener)
    {
        listeners.add(listener);
    }

    public void removeListener(IConnectionListener listener)
    {
        listeners.remove(listener);
    }

    public void addTimeout(IConnectionHandler handler)
    {
        loop.addTimeout(wrapConnectionHandler(handler));
    }

    public void addInterval(IConnectionHandler handler, long millis)
    {
        loop.addInterval(wrapConnectionHandler(handler), millis);
    }

    private Runnable wrapConnectionHandler(IConnectionHandler handler)
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                handler.run(connection);
            }
        };
    }

    @Override
    public void run()
    {
        while(!Thread.currentThread().isInterrupted())
        {
            if(connection != null)
            {
                try
                {
                    if(connect())
                    {
                        loop.iterate();
                    }
                }
                catch(Exception ex)
                {
                    Log.w("AsyncConnectionLoop", ex);
                }
            }
            else
            {
                Log.d("AsyncConnectionLoop", "connection == null");
            }

            try
            {
                Thread.sleep(150);
            }
            catch(InterruptedException ex) { }
        }
    }

    private boolean connect() throws CommunicationException, AuthenticationException
    {
        boolean newState = connection.isConnected();

        if(newState != oldState)
        {
            Log.d("AsyncConnectionLoop", "Connection state changed: " + newState);

            if(newState)
            {
                listeners.forEach((l) -> l.onConnected(connection));
            }
            else
            {
                listeners.forEach((l) -> l.onDisconnected());
            }

            oldState = newState;
        }

        if(!newState)
        {
            connection.connect();
        }

        return newState;
    }

    private IConnection createConnection(String connectionString)
    {
        IConnection connection;

        try
        {
            connection = Connection.create(connectionString);
        }
        catch(Exception ex)
        {
            Log.wtf("AsyncConnectionLoop", ex);

            connection = null;
        }

        return connection;
    }
}