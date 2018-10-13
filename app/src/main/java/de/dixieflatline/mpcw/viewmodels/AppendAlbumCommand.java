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

import android.util.*;

import javax.inject.*;
;
import de.dixieflatline.mpcw.services.*;

public class AppendAlbumCommand implements ITagCommand
{
    private final String artist;
    @Inject IBrowserService browserService;

    public AppendAlbumCommand(String artist)
    {
        this.artist = artist;
    }

    @Override
    public void run(Tag album)
    {
        Thread thread = new Thread(() ->
        {
            try
            {
                browserService.appendSongsFromAlbum(artist, album.getValue());
            }
            catch(Exception ex)
            {
                Log.e("AppendAlbumCommand", ex.getMessage());
            }
        });

        thread.start();
    }
}