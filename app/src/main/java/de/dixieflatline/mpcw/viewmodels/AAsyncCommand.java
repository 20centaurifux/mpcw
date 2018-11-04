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

import android.util.*;

import java.util.*;

import de.dixieflatline.mpcw.*;

public abstract class AAsyncCommand<T>
{
    private List<IAsyncCommandListener<T>> listeners = new ArrayList<>();

    public void runAsync(T arg)
    {
        Thread thread = new Thread(() ->
        {
            try
            {
               run(arg);
               listeners.forEach(l -> l.onSuccess(arg));
            }
            catch(Exception ex)
            {
                Log.d(Tags.COMMAND, "Command execution failed.", ex);

                listeners.forEach(l -> l.onFailed(ex));
            }
        });

        thread.start();
    }

    protected abstract void run(T arg) throws Exception;

    public void addListener(IAsyncCommandListener<T> listener)
    {
        listeners.add(listener);
    }

    public void removeListener(IAsyncCommandListener<T> listener)
    {
        listeners.remove(listener);
    }
}