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

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.*;

import javax.inject.Inject;

import de.dixieflatline.mpcw.R;
import de.dixieflatline.mpcw.databinding.FragmentPlayerBinding;
import de.dixieflatline.mpcw.services.*;
import de.dixieflatline.mpcw.services.implementation.*;
import de.dixieflatline.mpcw.viewmodels.Player;

public class PlayerFragment extends AInjectableFragment implements IConnectionListener, IPlayerListener
{
    private FragmentPlayerBinding binding;
    private Player player;

    @Inject IPlayerService playerService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        player = new Player();

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false);
        binding.setPlayer(player);

        playerService.addConnectionListener(this);
        playerService.addPlayerListener(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.previousSong).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                playerService.previous();
            }
        });

        view.findViewById(R.id.togglePlayer).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                playerService.toggle();
            }
        });

        view.findViewById(R.id.nextSong).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                playerService.next();
            }
        });
    }

    public void onPreviousClicked(View v)
    {
        playerService.previous();
    }

    public void onNextClicked(View v)
    {
        playerService.next();
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
        player.setConnected(true);
    }

    @Override
    public void onDisconnected()
    {
        player.setConnected(false);
    }

    @Override
    public void onPlaylistChanged(boolean hasPrevious, boolean hasNext)
    {
        player.setHasPrevious(hasPrevious);
        player.setHasNext(hasNext);
    }

    @Override
    public void onSongChanged(String artist, String title)
    {
        player.setArtist(artist);
        player.setTitle(title);
    }

    @Override
    public void onStatusChanged(EPlayerStatus status)
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
    }
}