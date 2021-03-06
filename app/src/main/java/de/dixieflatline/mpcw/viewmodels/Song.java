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

import androidx.databinding.*;

import java.io.*;

import de.dixieflatline.mpcw.*;

public class Song extends BaseObservable
{
    private String filename;
    private String artist;
    private String album;
    private int track;
    private String title;
    private String displayTitle;
    private AAsyncCommand<Song> songSelectCommand;

    @Bindable
    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
        updateDisplayTitle();
        notifyPropertyChanged(BR.filename);
    }

    @Bindable
    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
        notifyPropertyChanged(BR.artist);
    }

    @Bindable
    public String getAlbum()
    {
        return album;
    }

    public void setAlbum(String album)
    {
        this.album = album;
        notifyPropertyChanged(BR.album);
    }

    @Bindable
    public int getTrack()
    {
        return track;
    }

    public void setTrack(int track)
    {
        this.track = track;
        notifyPropertyChanged(BR.track);
    }

    @Bindable
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
        updateDisplayTitle();
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getDisplayTitle()
    {
        String title = this.title;

        if(title == null || title.isEmpty())
        {
            File file = new File(filename);

            title = file.getName();
        }

        return title;
    }

    public void updateDisplayTitle()
    {
        displayTitle = this.title;

        if(displayTitle == null || displayTitle.isEmpty())
        {
            File file = new File(filename);

            displayTitle = file.getName();


            notifyPropertyChanged(BR.displayTitle);
        }
    }

    public AAsyncCommand<Song> getSongSelectCommand()
    {
        return songSelectCommand;
    }

    public void setSongSelectCommand(AAsyncCommand<Song> songSelectCommand)
    {
        this.songSelectCommand = songSelectCommand;
    }
}
