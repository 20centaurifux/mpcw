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

import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

import de.dixieflatline.mpcw.client.*;
import de.dixieflatline.mpcw.services.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class PlayerService implements IPlayerService, IConnectionListener
{
    private final ArrayList<IPlayerListener> listeners = new ArrayList<IPlayerListener>();
    private String connectionString;
    private final Lock lock = new ReentrantLock();
    private boolean running;
    private Thread thread;
    private final AtomicReference<IConnection> connection = new AtomicReference<IConnection>();

    public PlayerService(Preferences preferences)
    {
        try
        {
            if(preferences.getAuthenticationEnabled())
            {
                connectionString = ConnectionStringBuilder.buildWithPassword(preferences.getHostname(),
                                                                             preferences.getPort(),
                                                                             preferences.getPassword());
            }
            else
            {
                connectionString = ConnectionStringBuilder.build(preferences.getHostname(),
                                                                 preferences.getPort());
            }
        }
        catch(URISyntaxException ex)
        {
            connectionString = null;
        }
    }

    @Override
    public void startAsync()
    {
        lock.lock();

        if(!running)
        {
            thread = new Thread(createConnectorRunnable());
            running = true;

            thread.start();
        }

        lock.unlock();
    }

    private Runnable createConnectorRunnable()
    {
        AsyncConnectionLoop connector = new AsyncConnectionLoop(connectionString);

        connector.addListener(this);

        connector.addInterval(new IConnectionHandler()
        {
            private boolean firstRun = true;
            private String lastArtist;
            private String lastTitle;
            private boolean lastPreviousButton = false;
            private EPlayerStatus lastStatus = EPlayerStatus.Ejected;
            private boolean lastNextButton = false;

            @Override
            public void run(IConnection connection)
            {
                try
                {
                    IClient client = connection.getClient();
                    Status status = client.getPlayer().getStatus();
                    String currentArtist = status.getArtist();
                    String currentTitle = status.getTitle();
                    EPlayerStatus currentStatus = mapState(status.getState());

                    if(firstRun)
                    {
                        listeners.forEach((l) -> l.onArtistChanged(currentArtist));
                        listeners.forEach((l) -> l.onTitleChanged(currentTitle));
                        listeners.forEach((l) -> l.onHasPreviousChanged(status.hasPrevious()));
                        listeners.forEach((l) -> l.onStatusChanged(currentStatus));
                        listeners.forEach((l) -> l.onHasNextChanged(status.hasNext()));

                        firstRun = false;
                    }
                    else
                    {
                        updatePlayer(status);
                    }

                    boolean hasNext = status.hasNext();
                    lastArtist = currentArtist;
                    lastTitle = currentTitle;
                    lastPreviousButton = status.hasPrevious();
                    lastStatus = currentStatus;
                    lastNextButton = status.hasNext();
                }
                catch(Exception ex)
                {
                    // TODO
                }
            }

            private void updatePlayer(Status status)
            {
                String currentArtist = status.getArtist();

                if((currentArtist != null && !currentArtist.equals(lastArtist)) || lastArtist != null)
                {
                    listeners.forEach((l) -> l.onArtistChanged(currentArtist));
                }

                String currentTitle = status.getTitle();

                if((currentTitle != null && !currentTitle.equals(lastTitle)) || lastTitle != null)
                {
                    listeners.forEach((l) -> l.onTitleChanged(currentTitle));
                }

                if(status.hasPrevious() != lastPreviousButton)
                {
                    listeners.forEach((l) -> l.onHasPreviousChanged(status.hasPrevious()));
                }

                EPlayerStatus currentStatus = mapState(status.getState());

                if(currentStatus != lastStatus)
                {
                    listeners.forEach((l) -> l.onStatusChanged(currentStatus));
                }

                if(status.hasNext() != lastNextButton)
                {
                    listeners.forEach((l) -> l.onHasNextChanged(status.hasNext()));
                }

                listeners.forEach((l) -> l.onTitleChanged(status.getTitle()));
            }
        }, 1000);

        return connector;
    }

    private static EPlayerStatus mapState(EState state)
    {
        EPlayerStatus status = EPlayerStatus.Ejected;

        switch(state)
        {
            case Pause:
                status = EPlayerStatus.Pause;
                break;

            case Stop:
                status = EPlayerStatus.Stopped;
                break;

            case Play:
                status = EPlayerStatus.Playing;
                break;
        }

        return status;
    }

    @Override
    public void stopService()
    {
        lock.lock();

        if(running)
        {
            thread.interrupt();

            try
            {
                thread.wait();
            }
            catch(InterruptedException ex)
            {
                Log.w("PlayerService", ex);
            }

            thread = null;
            running = false;
        }

        lock.unlock();
    }

    @Override
    public void next()
    {

    }

    @Override
    public void previous()
    {

    }

    @Override
    public void play()
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void stop()
    {

    }

    @Override
    public void addListener(IPlayerListener listener) { listeners.add(listener); }

    @Override
    public void removeListener(IPlayerListener listener) { listeners.remove(listener); }

    @Override
    public void onConnected(IConnection connection)
    {
        this.connection.set(connection);
        listeners.forEach(l -> l.onConnectionChanged(true));
    }

    @Override
    public void onDisconnected()
    {
        this.connection.set(null);
        listeners.forEach(l -> l.onConnectionChanged(false));
    }
}