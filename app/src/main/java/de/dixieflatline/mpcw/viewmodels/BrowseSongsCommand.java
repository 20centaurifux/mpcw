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

import android.app.*;
import android.content.*;
import android.os.*;

import de.dixieflatline.mpcw.views.*;

public class BrowseSongsCommand implements ICommand<Tag>
{
    private final Activity activity;
    private final Bundle bundle = new Bundle();

    public static BrowseSongsCommand byArtistAndAlbum(Activity activity, String artist)
    {
        BrowseSongsCommand command = new BrowseSongsCommand(activity);

        command.bundle.putSerializable("VIEW", EBrowserViewKind.ARTIST_ALBUM_SONG);
        command.bundle.putString("ARTIST_FILTER", artist);

        return command;
    }

    public static BrowseSongsCommand byAlbumAndArtist(Activity activity, String album)
    {
        BrowseSongsCommand command = new BrowseSongsCommand(activity);

        command.bundle.putSerializable("VIEW", EBrowserViewKind.ALBUM_ARTIST_SONG);
        command.bundle.putString("ALBUM_FILTER", album);

        return command;
    }

    private BrowseSongsCommand(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public void run(Tag tag)
    {
        Intent intent = new Intent(activity, BrowserActivity.class);

        EBrowserViewKind viewKind = (EBrowserViewKind)bundle.getSerializable("VIEW");

        if(viewKind == EBrowserViewKind.ARTIST_ALBUM_SONG)
        {
            bundle.putString("ALBUM_FILTER", tag.getValue());
        }
        else
        {
            bundle.putString("ARTIST_FILTER", tag.getValue());
        }

        intent.putExtras(bundle);

        activity.startActivity(intent);
    }
}