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

import java.util.*;

import de.dixieflatline.mpcw.*;

public class Browser extends BaseObservable
{
    private boolean loaded;
    private Iterable<Tag> tags;
    private Iterable<Song> songs;

    @Bindable
    public boolean getLoaded() { return loaded; }

    public void setLoaded(boolean loaded)
    {
        this.loaded = loaded;
        notifyPropertyChanged(BR.loaded);
    }

    @Bindable
    public Iterable<Tag> getTags()
    {
        return tags;
    }

    public void setTags(Iterable<Tag> tags)
    {
        this.tags = tags;
        notifyPropertyChanged(BR.tags);
        notifyPropertyChanged(BR.items);
    }

    @Bindable
    public Iterable<Song> getSongs()
    {
        return songs;
    }

    public void setSongs(Iterable<Song> songs)
    {
        this.songs = songs;
        notifyPropertyChanged(BR.songs);
        notifyPropertyChanged(BR.items);
    }

    @Bindable
    public Iterable<Object> getItems()
    {
        return () -> new Iterator<Object>()
        {
            private final Iterator<Tag> tagIterator = tags != null ? tags.iterator()
                                                                   : null;
            private final Iterator<Song> songIterator = songs != null ? songs.iterator()
                                                                      : null;

            @Override
            public boolean hasNext()
            {
                return (tagIterator != null && tagIterator.hasNext()) ||
                       (songIterator != null && songIterator.hasNext());
            }

            @Override
            public Object next()
            {
                Object nextValue;

                if(tagIterator != null && tagIterator.hasNext())
                {
                    nextValue = tagIterator.next();
                }
                else
                {
                    nextValue = songIterator.next();
                }

                return nextValue;
            }
        };
    }
}