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
import java.util.concurrent.*;

import de.dixieflatline.mpcw.client.*;
import de.dixieflatline.mpcw.client.Connection;
import de.dixieflatline.mpcw.utils.*;

public class AsyncConnectionLoop implements Runnable
{
    private static final int MAX_ERRORS = 3;

    private final List<IConnectionListener> listeners = new ArrayList<IConnectionListener>();
    private final IConnection connection;
    private final Queue<Runnable> queue = new ConcurrentLinkedDeque<Runnable>();
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
        queue.add(wrapConnectionHandler(handler));
    }

    public void addInterval(IConnectionHandler handler, long millis)
    {
        Runnable runnable = wrapConnectionHandler(handler);

        queue.add(new RecurringRunnable(runnable, millis));
    }

    private Runnable wrapConnectionHandler(IConnectionHandler handler)
    {
        return () ->
        {
            try
            {
                handler.run(connection);
            }
            catch(CommunicationException ex)
            {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    public void run()
    {
        boolean interrupted = false;
        int errorCounter = 0;

        while(!interrupted && connection != null)
        {
            try
            {
                addTasksFromQueue();

                try
                {
                    connect();
                    loop.iterate();
                    interrupted = Thread.currentThread().isInterrupted();
                    errorCounter = 0;
                }
                catch(Exception ex)
                {
                    Log.d("AsyncConnectionLoop", ex.getMessage());

                    ++errorCounter;

                    if(errorCounter == MAX_ERRORS)
                    {
                        listeners.forEach((l) -> l.onAborted(ex));
                        interrupted = true;
                    }
                }

                if(!interrupted)
                {
                    Thread.sleep(150);
                }
            }
            catch(InterruptedException ex)
            {
                interrupted = true;
            }
        }
    }

    private void addTasksFromQueue()
    {
        while(!queue.isEmpty())
        {
            Runnable runnable = queue.poll();

            if(runnable instanceof RecurringRunnable)
            {
                loop.add((RecurringRunnable)runnable);
            }
            else
            {
                loop.add(runnable);
            }
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
                listeners.forEach((l) -> l.onConnected());
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