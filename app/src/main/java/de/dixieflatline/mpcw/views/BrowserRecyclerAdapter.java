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

public class BrowserRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final int TAG= 0;
    private static final int SONG = 1;

    private final List<Object> items = new ArrayList<>();

    @Override
    public int getItemViewType(int position)
    {
        Object item = items.get(position);
        int viewType;

        if(item instanceof Tag)
        {
            viewType = TAG;
        }
        else if(item instanceof Song)
        {
            viewType = SONG;
        }
        else
        {
            throw new RuntimeException("Unexpected view type.");
        }

        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RecyclerView.ViewHolder viewHolder;

        if(viewType == TAG)
        {
            LayoutTagBinding binding = LayoutTagBinding.inflate(inflater, viewGroup, false);

            viewHolder = new TagViewHolder(binding);
        }
        else if(viewType == SONG)
        {
            LayoutSongBinding binding = LayoutSongBinding.inflate(inflater, viewGroup, false);

            viewHolder = new SongViewHolder(binding);
        }
        else
        {
            throw new RuntimeException("Unexpected view type.");
        }

        return viewHolder;
    }

    public void setItems(Iterable<Object> items)
    {
        this.items.clear();
        items.forEach((item) -> this.items.add(item));
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        Object item = items.get(position);

        if(item instanceof Tag)
        {
            ((TagViewHolder)viewHolder).bind((Tag)item);
        }
        else if(item instanceof Song)
        {
            ((SongViewHolder)viewHolder).bind((Song)item);
        }
        else
        {
            throw new RuntimeException("Unexpected instance type.");
        }
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }
}