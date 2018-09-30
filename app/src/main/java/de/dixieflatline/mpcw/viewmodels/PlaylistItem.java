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

import android.databinding.*;

import de.dixieflatline.mpcw.*;

public class PlaylistItem extends BaseObservable
{
    private String artist;
    private String title;

    @Bindable
    public String getArtist() { return artist; }

    public void setArtist(String artist)
    {
        this.artist = artist;
        notifyPropertyChanged(BR.artist);
    }

    @Bindable
    public String getTitle() { return title; }

    public void setTitle(String title)
    {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }
}