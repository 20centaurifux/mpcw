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
package de.dixieflatline.mpcw.views;

import android.app.*;
import android.os.*;

import java.util.concurrent.*;

public class FutureNotification
{
    private final Handler handler = new Handler();
    private final Notification notification;
    private final int timeoutMillis;

    public FutureNotification(Activity activity, int timeoutMillis)
    {
        notification = new Notification(activity);
        this.timeoutMillis = timeoutMillis;
    }

    public <T> void show(Future<T> future, int successResourceId, int failureResourceId)
    {
        CompletableFuture<T> f = new CompletableFuture<>();

        handler.post(() ->
        {
            if(waitForFuture(future))
            {
                notification.show(successResourceId);
            }
            else
            {
                notification.show(failureResourceId);
            }
        });
    }

    public <T> void showOnFailure(Future<T> future, int resourceId)
    {
        CompletableFuture<T> f = new CompletableFuture<>();

        handler.post(() ->
        {
            if(!waitForFuture(future))
            {
                notification.show(resourceId);
            }
        });
    }

    private <T> boolean waitForFuture(Future<T> future)
    {
        boolean success = false;

        try
        {
            future.get(timeoutMillis, TimeUnit.MILLISECONDS);
            success = future.isDone();
        }
        catch(Exception e)
        {
            success = false;
        }

        return success;
    }
}