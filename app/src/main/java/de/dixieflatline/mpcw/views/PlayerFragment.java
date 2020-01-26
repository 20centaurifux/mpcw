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
import androidx.recyclerview.widget.*;
import android.os.*;
import androidx.wear.widget.*;
import android.view.*;
import android.widget.*;

import java.util.concurrent.*;

import javax.inject.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.services.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class PlayerFragment extends AInjectableFragment implements IConnectionListener, IPlayerListener, IPlaylistListener
{
    private PlayerRecyclerAdapter adapter;
    private final Handler handler = new Handler();
    private final Player player = new Player();

    @Inject
    IPlayerService playerService;

    @Inject
    INetworkManager networkManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        inject();

        return inflater.inflate(R.layout.fragment_player, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        setupNetworkManager();
        setupPlayer();
        setupRecyclerView();
        setupPlayerService();
        setupActions();
        setupListeners();
    }

    private void setupNetworkManager()
    {
        if(networkManager != null)
        {
            networkManager.addListener(new INetworkManagerListener()
            {
                @Override
                public void onConnected()
                {
                    if(playerService != null)
                    {
                        playerService.startAsync();
                    }
                }

                @Override
                public void onFailure(Exception cause)
                {
                    MainNavigation mainNavigation = new MainNavigation(getActivity());

                    mainNavigation.openConnectionFailure(cause);
                }
            });
        }
    }

    private void setupPlayer()
    {
        FutureNotification notification = new FutureNotification(getActivity(), 2500);

        player.setPreviousCommand(() ->
        {
            Future<Void> future = playerService.previous();

            notification.showOnFailure(future, R.string.notification_command_failed);
        });

        player.setNextCommand(() ->
        {
            Future<Void> future = playerService.next();

            notification.showOnFailure(future, R.string.notification_command_failed);
        });

        player.setToggleCommand(() ->
        {
            Future<Void> future = playerService.toggle();

            notification.showOnFailure(future, R.string.notification_command_failed);
        });
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
            Intent intent = new Intent(getActivity(), BrowserActivity.class);

            startActivity(intent);
        });
    }

    private void setupListeners()
    {
        View view = getView();

        view.findViewById(R.id.clear_playlist).setOnClickListener((s) ->
        {
            FutureNotification notification = new FutureNotification(getActivity(), 2500);
            Future<Void> future = playerService.clear();

            notification.show(future, R.string.notification_playlist_cleared, R.string.notification_command_failed);
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();

        MainNavigation mainNavigation = new MainNavigation(getActivity());

        if(playerService == null)
        {
            mainNavigation.openConnectionFailure(new Exception("Please check connection preferences."));
        }
        else if(networkManager != null)
        {
            networkManager.connect();
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

        if(networkManager != null)
        {
            networkManager.release();
        }
    }

    @Override
    public void onConnected()
    {
        handler.post(() -> player.setConnected(true));
    }

    @Override
    public void onDisconnected() { }

    @Override
    public void onAborted(Exception cause)
    {
        MainNavigation mainNavigation = new MainNavigation(getActivity());

        mainNavigation.openConnectionFailure(cause);
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