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

import java.util.*;

import de.dixieflatline.mpcw.services.*;

public class DevelopmentNetworkManager implements INetworkManager
{
    private final List<INetworkManagerListener> listeners = new ArrayList<>();

    @Override
    public void connect()
    {
        listeners.forEach(l -> l.onConnected());
    }

    @Override
    public void release() { }

    @Override
    public void addListener(INetworkManagerListener listener)
    {
        listeners.add(listener);
    }

    @Override
    public void removeListener(INetworkManagerListener listener)
    {
        listeners.remove(listener);
    }
}
