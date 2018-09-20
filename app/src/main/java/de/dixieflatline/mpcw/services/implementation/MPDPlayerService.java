package de.dixieflatline.mpcw.services.implementation;

import android.os.Handler;

import java.util.ArrayList;

import de.dixieflatline.mpcw.services.IPlayerService;
import de.dixieflatline.mpcw.services.EPlayerStatus;
import de.dixieflatline.mpcw.services.IPlayerListener;

public class MPDPlayerService implements IPlayerService
{
    private ArrayList<IPlayerListener> listeners = new ArrayList<IPlayerListener>();
    private boolean connected;

    @Override
    public void connectAsync()
    {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                listeners.forEach(l->l.onConnectionChanged(true));

                listeners.forEach(l->l.onArtistChanged("the cure"));
                listeners.forEach(l->l.onTitleChanged("fascination street"));
                listeners.forEach(l->l.onHasNextChanged(false));
                listeners.forEach(l->l.onHasPreviousChanged(true));
                listeners.forEach(l->l.onStatusChanged(EPlayerStatus.Playing));
            }
        }, 10);
    }

    @Override
    public void disconnect()
    {
        listeners.forEach(l->l.onConnectionChanged(false));
    }

    @Override
    public void next() {

    }

    @Override
    public void previous()
    {

    }

    @Override
    public void play()
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void stop()
    {

    }

    @Override
    public void addListener(IPlayerListener listener) { listeners.add(listener); }

    @Override
    public void removeListener(IPlayerListener listener) { listeners.remove(listener); }
}