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

import de.dixieflatline.mpcw.BR;

public class Player extends BaseObservable
{
    public static final int PLAY = 0;
    public static final int PAUSE = 1;
    public static final int STOP = 2;
    public static final int EJECT = 3;

    private int status;
    private String artist;
    private String title;
    private boolean connected;
    private boolean hasPrevious;
    private boolean hasNext;
    private Runnable previousCommand;
    private Runnable nextCommand;
    private Runnable toggleCommand;

    @Bindable
    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

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

    @Bindable
    public boolean getConnected() { return connected; }

    public void setConnected(boolean connected)
    {
        this.connected = connected;
        notifyPropertyChanged(BR.connected);
    }

    @Bindable
    public boolean getHasPrevious() { return hasPrevious; }

    public void setHasPrevious(boolean hasPrevious)
    {
        this.hasPrevious= hasPrevious;
        notifyPropertyChanged(BR.hasPrevious);
    }

    @Bindable
    public boolean getHasNext() { return hasNext; }

    public void setHasNext(boolean hasNext)
    {
        this.hasNext = hasNext;
        notifyPropertyChanged(BR.hasNext);
    }

    public Runnable getPreviousCommand()
    {
        return previousCommand;
    }

    public void setPreviousCommand(Runnable previousCommand)
    {
        this.previousCommand = previousCommand;
    }

    public Runnable getNextCommand()
    {
        return nextCommand;
    }

    public void setNextCommand(Runnable nextCommand)
    {
        this.nextCommand = nextCommand;
    }

    public Runnable getToggleCommand()
    {
        return toggleCommand;
    }

    public void setToggleCommand(Runnable toggleCommand)
    {
        this.toggleCommand = toggleCommand;
    }
}