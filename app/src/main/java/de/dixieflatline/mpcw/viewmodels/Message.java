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

public class Message extends BaseObservable
{
    private String message;

    public Message(String message)
    {
        setMessage(message);
    }
    
    @Bindable
    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }
}
