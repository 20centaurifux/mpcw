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

import java.util.*;

import de.dixieflatline.mpcw.client.*;
import de.dixieflatline.mpcw.diff.*;
import de.dixieflatline.mpcw.services.*;
import de.dixieflatline.mpcw.viewmodels.PlaylistItem;

public class SynchronizePlaylist implements IConnectionHandler
{
    private final List<IPlaylistListener> listeners = new ArrayList<IPlaylistListener>();
    private IPlaylist playlist;

    public void addListener(IPlaylistListener listener)
    {
        listeners.add(listener);
    }

    public void removeListener(IPlaylistListener listener)
    {
        listeners.remove(listener);
    }

    @Override
    public void run(IConnection connection) throws CommunicationException
    {
        try
        {
            Iterable<ITransformation> transformations = getTransformations(connection);

            for(ITransformation transformation : transformations)
            {
                if(transformation instanceof InsertPlaylistItem)
                {
                    InsertPlaylistItem insert = (InsertPlaylistItem)transformation;

                    PlaylistItem item = new PlaylistItem();

                    item.setArtist(insert.getItem().getArtist());
                    item.setTitle(insert.getItem().getTitle());

                    listeners.forEach((l) -> l.onPlaylistItemInserted(item, insert.getOffset()));
                }
                else if(transformation instanceof Delete)
                {
                    Delete delete = (Delete)transformation;

                    listeners.forEach((l) -> l.onPlaylistItemsRemoved(delete.getOffset(), delete.getLength()));
                }
            }
        }
        catch(CommunicationException ex)
        {
            throw ex;
        }
        catch(Exception ex)
        {
            throw new CommunicationException("Couldn't synchronize playlist.");
        }
    }

    private Iterable<ITransformation> getTransformations(IConnection connection) throws ProtocolException, CommunicationException
    {
        Iterable<ITransformation> transformations = null;

        if(playlist == null)
        {
            transformations = loadInitialPlaylist(connection);
        }
        else
        {
            transformations = updatePlaylist(connection);
        }

        return transformations;
    }

    private Iterable<ITransformation> loadInitialPlaylist(IConnection connection) throws ProtocolException, CommunicationException
    {
        IClient client = connection.getClient();

        playlist = client.getCurrentPlaylist();

        return playlist.synchronize();
    }

    private Iterable<ITransformation> updatePlaylist(IConnection connection) throws ProtocolException, CommunicationException
    {
        Iterable<ITransformation> transformations;

        try
        {
            transformations = playlist.synchronize();
        }
        catch(CommunicationException ex)
        {
            IClient client = connection.getClient();

            playlist = client.resyncCurrentPlaylist(playlist);

            transformations = playlist.synchronize();
        }

        return transformations;
    }
}