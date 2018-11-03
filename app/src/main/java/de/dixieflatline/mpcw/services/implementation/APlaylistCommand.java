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
package de.dixieflatline.mpcw.services.implementation;

import android.util.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.client.*;

public abstract class APlaylistCommand implements IConnectionHandler
{
    @Override
    public void run(IConnection connection)
    {
        try
        {
            run(connection.getClient().getCurrentPlaylist());
        }
        catch(Exception ex)
        {
            Log.w(Tags.SERVICE, ex);
        }
    }

    protected abstract void run(IPlaylist playlist) throws Exception;
}
