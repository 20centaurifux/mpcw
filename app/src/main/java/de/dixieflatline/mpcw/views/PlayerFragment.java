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
import de.dixieflatline.mpcw.viewmodels.Player;

public class PlayerFragment extends AInjectableFragment implements IPlayerListener
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

        playerService.addListener(this);

        return binding.getRoot();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        try
        {
            playerService.startAsync();
        }
        catch(Exception ex)
        {
            // TODO
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        playerService.stop();
    }

    @Override
    public void onConnectionChanged(boolean isConnected) { player.setConnected(isConnected); }

    @Override
    public void onArtistChanged(String artist) { player.setArtist(artist); }

    @Override
    public void onTitleChanged(String title) { player.setTitle(title); };

    @Override
    public void onStatusChanged(EPlayerStatus status)
    {
        switch(status)
        {
            case Pause:
                player.setStatus(Player.PAUSE);
                break;

            case Playing:
                player.setStatus(Player.PLAYING);
                break;

            case Stopped:
                player.setStatus(Player.STOPPED);
                break;
        }
    }

    @Override
    public void onHasPreviousChanged(boolean hasPrevious) { player.setHasPrevious(hasPrevious); }

    @Override
    public void onHasNextChanged(boolean hasNext) { player.setHasNext(hasNext); }

    @Override
    public void onException(Exception reason)
    {

    }
}