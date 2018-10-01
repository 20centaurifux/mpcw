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
import de.dixieflatline.mpcw.services.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class PlayerService implements IPlayerService
{
    private final List<IPlayerListener> playerListeners = new ArrayList<IPlayerListener>();
    private final AsyncConnectionLoop loop;
    private final SynchronizePlayerStatus synchronizePlayerStatus;
    private final SynchronizePlaylist synchronizePlaylist;
    private final Semaphore semaphore = new Semaphore(1);
    private boolean running;
    private Thread thread;

    public PlayerService(Preferences preferences)
    {
        String connectionString = ConnectionStringBuilder.fromPreferences(preferences);

        loop = new AsyncConnectionLoop(connectionString);

        synchronizePlayerStatus = new SynchronizePlayerStatus();
        synchronizePlaylist = new SynchronizePlaylist();

        loop.addInterval(synchronizePlayerStatus, 1000);
        loop.addInterval(synchronizePlaylist, 1500);
    }

    @Override
    public void startAsync()
    {
        if(semaphore.tryAcquire())
        {
            if(!running)
            {
                thread = new Thread(loop);
                running = true;

                thread.start();
            }

            semaphore.release();
        }
    }

    @Override
    public void stopService()
    {
        if(semaphore.tryAcquire())
        {
            if(running)
            {
                thread.interrupt();

                try
                {
                    thread.join();
                }
                catch(InterruptedException ex)
                {
                    Log.w("PlayerService", ex);
                }

                thread = null;
                running = false;
            }

            semaphore.release();
        }
    }

    @Override
    public void previous()
    {
        loop.addTimeout(new APlayerCommand()
        {
            @Override
            protected void run(IPlayer player) throws CommunicationException, ProtocolException
            {
                player.previous();
            }
        });
    }

    @Override
    public void next()
    {
        loop.addTimeout(new APlayerCommand()
        {
            @Override
            protected void run(IPlayer player) throws CommunicationException, ProtocolException
            {
                player.next();
            }
        });
    }

    @Override
    public void toggle()
    {
        loop.addTimeout(new APlayerCommand()
        {
            @Override
            protected void run(IPlayer player) throws CommunicationException, ProtocolException
            {
                Status status = player.getStatus();
                EPlayerStatus newStatus;

                if(status.getState() == EState.Play)
                {
                    player.pause();
                    newStatus = EPlayerStatus.Pause;
                }
                else
                {
                    player.play();
                    newStatus = EPlayerStatus.Play;
                }

                playerListeners.forEach((l) -> l.onStatusChanged(newStatus));
            }
        });
    }

    @Override
    public void playFromCurrentPlaylist(int position)
    {
        loop.addTimeout((connection) ->
        {
            try
            {
                synchronizePlaylist.getPlaylist().selectAndPlay(position);
            }
            catch(ProtocolException ex)  { }
        });
    }

    @Override
    public void addConnectionListener(IConnectionListener listener)
    {
        loop.addListener(listener);
    }

    @Override
    public void removeConnectionListener(IConnectionListener listener)
    {
        loop.removeListener(listener);
    }

    @Override
    public void addPlayerListener(IPlayerListener listener)
    {
        playerListeners.add(listener);
        synchronizePlayerStatus.addListener(listener);
    }

    @Override
    public void removePlayerListener(IPlayerListener listener)
    {
        playerListeners.remove(listener);
        synchronizePlayerStatus.removeListener(listener);
    }

    @Override
    public void addPlaylistListener(IPlaylistListener listener)
    {
        synchronizePlaylist.addListener(listener);
    }

    @Override
    public void removePlaylistListener(IPlaylistListener listener)
    {
        synchronizePlaylist.removeListener(listener);
    }
}