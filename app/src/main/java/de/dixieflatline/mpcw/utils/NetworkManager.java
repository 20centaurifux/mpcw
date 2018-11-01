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
package de.dixieflatline.mpcw.utils;

import android.content.*;
import android.net.*;
import android.os.*;

import java.util.*;
import java.util.concurrent.*;

public class NetworkManager
{
    private final static int ConnectionTimeoutMillis = 10000;

    private final ConnectivityManager connectivityManager;
    private final List<INetworkMangerListener> listeners = new ArrayList<>();
    private final Handler handler = new Handler();
    private ConnectivityManager.NetworkCallback callback;
    private Runnable timeoutCallback;

    public NetworkManager(Context context)
    {
        connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void connect()
    {
        unregisterCallbacks();

        NetworkRequest request = new NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                                                             .build();

        callback = new ConnectivityManager.NetworkCallback()
        {
            @Override
            public void onAvailable(Network network)
            {
                super.onAvailable(network);

                unregisterTimeoutCallback();

                if(connectivityManager.bindProcessToNetwork(network))
                {
                    listeners.forEach(l -> l.onConnected());
                }
            }
        };

        connectivityManager.requestNetwork(request, callback);

        timeoutCallback = () ->
        {
            listeners.forEach(l -> l.onFailure(new TimeoutException("Couldn't establish WiFi connection.")));
        };

        handler.postDelayed(timeoutCallback, ConnectionTimeoutMillis);
    }

    public void release()
    {
        unregisterCallbacks();
    }

    private void unregisterCallbacks()
    {
        unregisterTimeoutCallback();
        unregisterNetworkCallback();
    }

    private void unregisterTimeoutCallback()
    {
        if(timeoutCallback != null)
        {
            handler.removeCallbacks(timeoutCallback);
            timeoutCallback = null;
        }
    }

    private void unregisterNetworkCallback()
    {
        if (callback != null)
        {
            connectivityManager.unregisterNetworkCallback(callback);
            callback = null;
        }
    }

    public void addListener(INetworkMangerListener listener)
    {
        listeners.add(listener);
    }

    public void removeListener(INetworkMangerListener listener)
    {
        listeners.remove(listener);
    }
}