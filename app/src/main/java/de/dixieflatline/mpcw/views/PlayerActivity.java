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

import android.os.*;
import android.support.v7.widget.*;
import android.support.wear.widget.*;

import javax.inject.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.services.*;
import de.dixieflatline.mpcw.services.implementation.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class PlayerActivity extends AActivity implements IConnectionListener, IPlayerListener, IPlaylistListener
{
    private PlayerRecyclerAdapter adapter;
    private final Handler handler = new Handler();

    @Inject
    IPlayerService playerService;

    private final Player player = new Player();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);

        setupPlayer();
        setupRecyclerView();
        setupPlayerService();
        setupDrawer(NavigationAdapter.PLAYER);

        setAmbientEnabled();
    }

    private void setupPlayer()
    {
        player.setPreviousCommand(() -> playerService.previous());
        player.setNextCommand(() -> playerService.next());
        player.setToggleCommand(() -> playerService.toggle());
    }

    private void setupRecyclerView()
    {
        setContentView(R.layout.activity_player);

        WearableRecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setEdgeItemsCenteringEnabled(true);

        WearableRecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlayerRecyclerAdapter(player);
        recyclerView.setAdapter(adapter);
    }

    private void setupPlayerService()
    {
        playerService.addConnectionListener(this);
        playerService.addPlayerListener(this);
        playerService.addPlaylistListener(this);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        playerService.startAsync();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        playerService.stopService();
    }

    @Override
    public void onConnected()
    {
        handler.post(() -> player.setConnected(true));
    }

    @Override
    public void onDisconnected()
    {
        handler.post(() -> player.setConnected(false));
    }

    @Override
    public void onAborted(Exception cause)
    {
    }

    @Override
    public void onPlaylistChanged(boolean hasPrevious, boolean hasNext)
    {
        handler.post(() ->
        {
            player.setHasPrevious(hasPrevious);
            player.setHasNext(hasNext);
        });
    }

    @Override
    public void onSongChanged(String artist, String title)
    {
        handler.post(() ->
        {
            player.setArtist(artist);
            player.setTitle(title);
        });
    }

    @Override
    public void onStatusChanged(EPlayerStatus status)
    {
        handler.post(() ->
        {
            switch(status)
            {
                case Eject:
                    player.setStatus(Player.EJECT);
                    break;

                case Pause:
                    player.setStatus(Player.PAUSE);
                    break;

                case Play:
                    player.setStatus(Player.PLAY);
                    break;

                case Stop:
                    player.setStatus(Player.STOP);
                    break;
            }
        });
    }

    @Override
    public void onPlaylistItemInserted(PlaylistItem item, int offset)
    {
        handler.post(() ->
        {
            adapter.insert(item, offset);
        });
    }

    @Override
    public void onPlaylistItemsRemoved(int from, int count)
    {
        handler.post(() ->
        {
            adapter.remove(from, count);
        });
    }
}