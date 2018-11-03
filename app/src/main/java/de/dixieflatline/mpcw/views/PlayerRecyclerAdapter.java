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

import android.support.v7.widget.*;
import android.view.*;

import java.util.*;

import de.dixieflatline.mpcw.databinding.*;
import de.dixieflatline.mpcw.viewmodels.*;
import de.dixieflatline.mpcw.viewmodels.Player;

public class PlayerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final int PLAYER = 0;
    private static final int PLAYLISTITEM = 1;
    private final Player player;
    private final List<PlaylistItem> items = new ArrayList<>();

    public PlayerRecyclerAdapter(Player player)
    {
        this.player = player;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position == 0 ? PLAYER : PLAYLISTITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RecyclerView.ViewHolder viewHolder;

        if(viewType == PLAYER)
        {
            LayoutPlayerBinding binding = LayoutPlayerBinding.inflate(inflater, viewGroup, false);

            viewHolder = new PlayerViewHolder(binding);
        }
        else if(viewType == PLAYLISTITEM)
        {
            LayoutPlaylistitemBinding binding = LayoutPlaylistitemBinding.inflate(inflater, viewGroup, false);

            viewHolder = new PlaylistItemViewHolder(binding);
        }
        else
        {
            throw new RuntimeException("Unexpected view type.");
        }

        return viewHolder;
    }

    public void insert(PlaylistItem item, int offset)
    {
        items.add(offset, item);

        notifyItemInserted(offset + 1);
    }

    public void remove(int from, int count)
    {
        items.subList(from, from + count).clear();

        notifyItemRangeRemoved(from + 1, count);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        if(position == 0)
        {
            ((PlayerViewHolder)viewHolder).bind(player);
        }
        else
        {
            ((PlaylistItemViewHolder)viewHolder).bind(items.get(position - 1));
        }
    }

    @Override
    public int getItemCount()
    {
        return 1 + items.size();
    }
}