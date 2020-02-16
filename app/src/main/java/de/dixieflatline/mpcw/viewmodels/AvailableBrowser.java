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

import android.graphics.drawable.*;

import androidx.databinding.*;

import de.dixieflatline.mpcw.*;

public class AvailableBrowser extends BaseObservable
{
    private EBrowser browserType;
    private String title;
    private Drawable icon;
    private ICommand<AvailableBrowser> selectBrowserCommand;

    public AvailableBrowser(EBrowser browserType, String title, Drawable icon)
    {
        this.browserType = browserType;
        this.title = title;
        this.icon = icon;
    }

    public void setTitle(String title)
    {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public EBrowser getBrowserType()
    {
        return browserType;
    }

    public void setBrowserType(EBrowser browserType)
    {
        this.browserType = browserType;
        notifyPropertyChanged(BR.browserType);
    }

    @Bindable
    public String getTitle()
    {
        return title;
    }

    @Bindable
    public Drawable getIcon()
    {
        return icon;
    }

    public void setIcon(Drawable icon)
    {
        this.icon = icon;
        notifyPropertyChanged(BR.icon);
    }

    public ICommand<AvailableBrowser> getSelectBrowserCommand()
    {
        return selectBrowserCommand;
    }

    public void setSelectBrowserCommand(ICommand<AvailableBrowser> selectBrowserCommand)
    {
        this.selectBrowserCommand = selectBrowserCommand;
    }
}