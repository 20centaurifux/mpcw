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

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import de.dixieflatline.mpcw.BR;

public class Preferences extends BaseObservable
{
    private String hostname;
    private int port;
    private boolean authenticationEnabled = false;
    private String password;

    @Bindable
    public String getHostname()
    {
        return hostname;
    }

    public void setHostname(String hostname)
    {
        this.hostname = hostname;
        notifyPropertyChanged(BR.hostname);
    }

    @Bindable
    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
        notifyPropertyChanged(BR.port);
    }

    @Bindable
    public boolean getAuthenticationEnabled()
    {
        return authenticationEnabled;
    }

    public void setAuthenticationEnabled(boolean authenticationEnabled)
    {
        this.authenticationEnabled = authenticationEnabled;
        notifyPropertyChanged(BR.authenticationEnabled);
    }

    @Bindable
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}
