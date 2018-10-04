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

public class BrowseAlbumCommand implements ITagActivateCommand
{
    private final Activity activity;
    private final String artist;

    public BrowseAlbumCommand(Activity activity, String artist)
    {
        this.activity = activity;
        this.artist = artist;
    }

    @Override
    public void run(Tag tag)
    {
        Bundle bundle = new Bundle();

        bundle.putString("ARTIST_FILTER", artist);
        bundle.putString("ALBUM_FILTER", tag.getValue());

        Intent intent = new Intent(activity, BrowserActivity.class);

        intent.putExtras(bundle);

        activity.startActivity(intent);
    }
}