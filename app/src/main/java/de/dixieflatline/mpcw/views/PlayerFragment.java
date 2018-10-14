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

import android.content.*;
import android.os.*;
import android.support.v7.widget.*;
import android.support.wear.widget.*;
import android.view.*;
import android.widget.*;

import javax.inject.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.services.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class PlayerFragment extends AInjectableFragment implements IConnectionListener, IPlayerListener, IPlaylistListener
{
    private PlayerRecyclerAdapter adapter;
    private final Handler handler = new Handler();

    @Inject IPlayerService playerService;

    private final Player player = new Player();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        inject();

        return inflater.inflate(R.layout.fragment_player, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        setupPlayer();
        setupRecyclerView();
        setupPlayerService();
        setupActions();
        setupListeners();
    }

    private void setupPlayer()
    {
        player.setPreviousCommand(() -> playerService.previous());
        player.setNextCommand(() -> playerService.next());
        player.setToggleCommand(() -> playerService.toggle());
    }

    private void setupRecyclerView()
    {
        WearableRecyclerView recyclerView = getView().findViewById(R.id.recycler_view);

        recyclerView.setEdgeItemsCenteringEnabled(true);

        WearableRecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlayerRecyclerAdapter(player);
        recyclerView.setAdapter(adapter);
    }

    private void setupPlayerService()
    {
        if(playerService != null)
        {
            playerService.addConnectionListener(this);
            playerService.addPlayerListener(this);
            playerService.addPlaylistListener(this);
        }
    }

    private void setupActions()
    {
        Button button = getView().findViewById(R.id.browser);

        button.setOnClickListener((btn) ->
        {
            Intent intent  = new Intent(getActivity(), BrowserActivity.class);

            startActivity(intent);
        });
    }

    private void setupListeners()
    {
        View view = getView();

        view.findViewById(R.id.clear_playlist).setOnClickListener((s) -> playerService.clear());
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if(playerService == null)
        {
            NavigationUtil navigationUtil = new NavigationUtil(getActivity());

            navigationUtil.openConnectionFailure(new Exception("Please check connection preferences."));
        }
        else
        {
            playerService.startAsync();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if(playerService != null)
        {
            playerService.stopService();
        }
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
        NavigationUtil navigationUtil = new NavigationUtil(getActivity());

        navigationUtil.openConnectionFailure(cause);
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
            item.setSelectCommand((playlistItem, position) ->
            {
                playerService.playFromCurrentPlaylist(position);
            });

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