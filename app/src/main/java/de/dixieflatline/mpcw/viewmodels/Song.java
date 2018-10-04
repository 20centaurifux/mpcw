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

public class Song extends BaseObservable
{
    private String filename;
    private String artist;
    private String album;
    private int track;
    private String title;
    private IPlaylistItemSelectCommand selectCommand;

    @Bindable
    public String getFilename() { return filename; }

    public void setFilename(String filename)
    {
        this.filename = filename;
        notifyPropertyChanged(BR.filename);
    }

    @Bindable
    public String getArtist() { return artist; }

    public void setArtist(String artist)
    {
        this.artist = artist;
        notifyPropertyChanged(BR.artist);
    }

    @Bindable
    public String getAlbum() { return album; }

    public void setAlbum(String album)
    {
        this.album = album;
        notifyPropertyChanged(BR.album);
    }

    @Bindable
    public int getTrack() { return track; }

    public void setTrack(int track)
    {
        this.track = track;
        notifyPropertyChanged(BR.track);
    }

    @Bindable
    public String getTitle() { return title; }

    public void setTitle(String title)
    {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }
}