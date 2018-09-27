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

import java.util.ArrayList;
import java.util.concurrent.*;

import de.dixieflatline.mpcw.services.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class PlayerService implements IPlayerService
{
    private final AsyncConnectionLoop loop;
    private final SynchronizePlayerStatus synchronizePlayerStatus;
    private final Semaphore semaphore = new Semaphore(1);
    private boolean running;
    private Thread thread;

    public PlayerService(Preferences preferences)
    {
        String connectionString = ConnectionStringBuilder.fromPreferences(preferences);

        loop = new AsyncConnectionLoop(connectionString);

        synchronizePlayerStatus = new SynchronizePlayerStatus();

        loop.addInterval(synchronizePlayerStatus, 1000);
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
                    thread.wait();
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
        synchronizePlayerStatus.addListener(listener);
    }

    @Override
    public void removePlayerListener(IPlayerListener listener)
    {
        synchronizePlayerStatus.removeListener(listener);
    }
}