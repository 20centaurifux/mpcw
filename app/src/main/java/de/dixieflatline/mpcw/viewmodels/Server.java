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

public class Server extends BaseObservable
{
    private String hostname;
    private int port = 6600;
    
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
}
