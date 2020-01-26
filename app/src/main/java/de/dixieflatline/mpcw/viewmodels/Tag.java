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

import de.dixieflatline.mpcw.*;

public class Tag extends BaseObservable
{
    private final ETag tag;
    private String value;
    private ICommand<Tag> tagActivateCommand;
    private AAsyncCommand tagSelectCommand;

    private Tag(ETag tag, String value)
    {
        this.tag = tag;
        this.value = value;
    }

    public static Tag Artist(String value)
    {
        return new Tag(ETag.Artist, value);
    }

    public static Tag Album(String value)
    {
        return new Tag(ETag.Album, value);
    }

    @Bindable
    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
        notifyPropertyChanged(BR.value);
    }

    public ICommand<Tag> getTagActivateCommand()
    {
        return tagActivateCommand;
    }

    public void setTagActivateCommand(ICommand<Tag> tagActivateCommand)
    {
        this.tagActivateCommand = tagActivateCommand;
    }

    public AAsyncCommand getTagSelectCommand()
    {
        return tagSelectCommand;
    }

    public void setTagSelectCommand(AAsyncCommand tagSelectCommand)
    {
        this.tagSelectCommand = tagSelectCommand;
    }
}
