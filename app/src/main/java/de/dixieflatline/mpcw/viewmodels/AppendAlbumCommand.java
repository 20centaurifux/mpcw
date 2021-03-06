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
package de.dixieflatline.mpcw.viewmodels;

import javax.inject.*;
;
import de.dixieflatline.mpcw.services.*;

public class AppendAlbumCommand extends AAsyncCommand<Tag>
{
    private final String artist;

    @Inject
    INetworkManager networkManager;

    @Inject
    IBrowserService browserService;

    public static AppendAlbumCommand all()
    {
        return new AppendAlbumCommand("");
    }

    public static AppendAlbumCommand withArtist(String artist)
    {
        return new AppendAlbumCommand(artist);
    }

    private AppendAlbumCommand(String artist)
    {
        this.artist = artist;
    }

    @Override
    public void run(Tag album) throws Exception
    {
        networkManager.connectAndWait();

        if(artist == null || artist.isEmpty())
        {
            browserService.appendSongsFromAlbum(album.getValue());
        }
        else
        {
            browserService.appendSongsFromArtistAndAlbum(artist, album.getValue());
        }
    }
}