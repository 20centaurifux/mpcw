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

import java.io.*;
import java.net.*;

import de.dixieflatline.mpcw.viewmodels.*;

public final class ConnectionStringBuilder
{
    public static String build(String host, int port)
    {
        return String.format("mpd://%s:%d", host, port);
    }

    public static String buildWithPassword(String host, int port, String password) throws URISyntaxException
    {
        String connectionString = build(host, port) + "?password=";

        try
        {
            String parameter = "";

            if(password != null)
            {
                parameter = URLEncoder.encode(password, "UTF-8");
            }

            connectionString += parameter;
        }
        catch(UnsupportedEncodingException ex)
        {
            throw new URISyntaxException(connectionString + password, ex.getMessage());
        }

        return connectionString;
    }

    public static String fromPreferences(Preferences preferences)
    {
        String connectionString;

        try
        {
            if(preferences.getAuthenticationEnabled())
            {
                connectionString = ConnectionStringBuilder.buildWithPassword(preferences.getHostname(),
                                                                             preferences.getPort(),
                                                                             preferences.getPassword());
            }
            else
            {
                connectionString = ConnectionStringBuilder.build(preferences.getHostname(),
                                                                 preferences.getPort());
            }
        }
        catch(URISyntaxException ex)
        {
            connectionString = null;
        }

        return connectionString;
    }
}
