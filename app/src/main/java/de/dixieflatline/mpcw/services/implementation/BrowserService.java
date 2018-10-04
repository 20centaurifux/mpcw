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

import java.net.*;
import java.util.*;

import de.dixieflatline.mpcw.client.AuthenticationException;
import de.dixieflatline.mpcw.client.CommunicationException;
import de.dixieflatline.mpcw.client.Connection;
import de.dixieflatline.mpcw.client.DispatchException;
import de.dixieflatline.mpcw.client.ETag;
import de.dixieflatline.mpcw.client.Filter;
import de.dixieflatline.mpcw.client.IBrowser;
import de.dixieflatline.mpcw.client.IClient;
import de.dixieflatline.mpcw.client.IConnection;
import de.dixieflatline.mpcw.client.ISearchResult;
import de.dixieflatline.mpcw.client.ProtocolException;
import de.dixieflatline.mpcw.services.IBrowserService;
import de.dixieflatline.mpcw.viewmodels.Preferences;
import de.dixieflatline.mpcw.viewmodels.Song;

public class BrowserService implements IBrowserService
{
    private final String connectionString;

    public BrowserService(Preferences preferences)
    {
        connectionString = ConnectionStringBuilder.fromPreferences(preferences);
    }

    @Override
    public Iterable<String> getAllArtists() throws DispatchException,
                                                   URISyntaxException,
                                                   CommunicationException,
                                                   AuthenticationException,
                                                   ProtocolException
    {
        ISearchResult<de.dixieflatline.mpcw.client.Tag> result = query(browser -> browser.findTags(ETag.Artist));

        return selectTagValues(result.getItems()
                                     .iterator());
    }

    @Override
    public Iterable<String> getAlbumsByArtist(String artist) throws DispatchException,
                                                                    URISyntaxException,
                                                                    CommunicationException,
                                                                    AuthenticationException,
                                                                    ProtocolException
    {
        Filter[] filter = new Filter[] { new Filter(ETag.Artist, artist) };
        ISearchResult<de.dixieflatline.mpcw.client.Tag> result = query(browser -> browser.findTags(ETag.Album, filter));

        return selectTagValues(result.getItems()
                                     .iterator());
    }

    private Iterable<String> selectTagValues(Iterator<de.dixieflatline.mpcw.client.Tag> tags)
    {
        return () -> createTagValueIterator(tags);
    }

    private Iterator<String> createTagValueIterator(Iterator<de.dixieflatline.mpcw.client.Tag> tags)
    {
        return new Iterator<String>()
        {
            @Override
            public boolean hasNext()
            {
                return tags.hasNext();
            }

            @Override
            public String next()
            {
                return tags.next().getValue();
            }
        };
    }

    @Override
    public Iterable<Song> getSongsByAlbum(String artist, String album) throws DispatchException,
                                                                              URISyntaxException,
                                                                              CommunicationException,
                                                                              AuthenticationException,
                                                                              ProtocolException
    {
        Filter[] filter = new Filter[]
        {
            new Filter(ETag.Artist, artist),
            new Filter(ETag.Album, album),
        };
        ISearchResult<de.dixieflatline.mpcw.client.Song> result = query(browser -> browser.findSongs(filter));

        return convertSongs(result.getItems()
                                  .iterator());
    }

    private Iterable<Song> convertSongs(Iterator<de.dixieflatline.mpcw.client.Song> songs)
    {
        return () -> convertSongIterator(songs);
    }

    private Iterator<Song> convertSongIterator(Iterator<de.dixieflatline.mpcw.client.Song> songs)
    {
        return new Iterator<Song>()
        {
            @Override
            public boolean hasNext()
            {
                return songs.hasNext();
            }

            @Override
            public Song next()
            {
                de.dixieflatline.mpcw.client.Song model = songs.next();
                Song viewModel = new Song();

                viewModel.setFilename(model.getFilename());
                viewModel.setArtist(model.getArtist());
                viewModel.setAlbum(model.getAlbum());
                viewModel.setTrack(model.getTrack());
                viewModel.setTitle(model.getTitle());

                return viewModel;
            }
        };
    }

    private <T> T query(IQuery<T> query) throws DispatchException,
                                                URISyntaxException,
                                                CommunicationException,
                                                AuthenticationException,
                                                ProtocolException
    {
        IConnection connection = Connection.create(connectionString);

        connection.connect();

        IClient client = connection.getClient();
        IBrowser browser = client.getBrowser();
        T result = query.run(browser);

        connection.disconnect();

        return result;
    }
}