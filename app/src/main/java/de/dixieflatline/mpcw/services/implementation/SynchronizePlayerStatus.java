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
import de.dixieflatline.mpcw.services.*;

public class SynchronizePlayerStatus implements IConnectionHandler
{
    private final List<IPlayerListener> listeners = new ArrayList<IPlayerListener>();
    private boolean firstRun = true;
    private String lastArtist;
    private String lastTitle;
    private boolean lastPreviousButton = false;
    private EPlayerStatus lastStatus = EPlayerStatus.Eject;
    private boolean lastNextButton = false;

    public void addListener(IPlayerListener listener)
    {
        listeners.add(listener);
    }

    public void removeListener(IPlayerListener listener)
    {
        listeners.remove(listener);
    }

    @Override
    public void run(IConnection connection)
    {
        try
        {
            IClient client = connection.getClient();
            Status status = client.getPlayer().getStatus();

            synchronizeStatus(status);

            lastArtist = status.getArtist();
            lastTitle = status.getTitle();
            lastPreviousButton = status.hasPrevious();
            lastStatus = mapState(status);
            lastNextButton = status.hasNext();
        }
        catch(Exception ex)
        {
            Log.w("SynchronizePlayerStatus", ex);
        }
    }

    private void synchronizeStatus(Status status)
    {
        if(firstRun)
        {
            initialRun(status);
        }
        else
        {
            updateStatus(status);
        }
    }

    private void initialRun(Status status)
    {
        listeners.forEach((l) -> l.onSongChanged(status.getArtist(), status.getTitle()));
        listeners.forEach((l) -> l.onStatusChanged(mapState(status)));
        listeners.forEach((l) -> l.onPlaylistChanged(status.hasPrevious(), status.hasNext()));

        firstRun = false;
    }

    private void updateStatus(Status status)
    {
        if(!Objects.equals(lastArtist, status.getArtist()) || !Objects.equals(lastTitle, status.getTitle()))
        {
            listeners.forEach((l) -> l.onSongChanged(status.getArtist(), status.getTitle()));
        }

        final EPlayerStatus newStatus = mapState(status);

        if(lastStatus != newStatus)
        {
            listeners.forEach((l) -> l.onStatusChanged(newStatus));
        }

        if(lastPreviousButton != status.hasPrevious() || lastNextButton != status.hasNext())
        {
            listeners.forEach((l) -> l.onPlaylistChanged(status.hasPrevious(), status.hasNext()));
        }
    }

    private static EPlayerStatus mapState(Status status)
    {
        EPlayerStatus playerStatus = EPlayerStatus.Eject;

        if(status.getArtist() != null || status.getTitle() != null)
        {
            switch(status.getState())
            {
                case Pause:
                    playerStatus = EPlayerStatus.Pause;
                    break;

                case Stop:
                    playerStatus = EPlayerStatus.Stop;
                    break;

                case Play:
                    playerStatus = EPlayerStatus.Play;
                    break;
            }
        }

        return playerStatus;
    }
}