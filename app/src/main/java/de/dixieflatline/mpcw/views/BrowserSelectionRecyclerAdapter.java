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

import android.view.*;

import androidx.recyclerview.widget.*;

import java.util.*;

import de.dixieflatline.mpcw.databinding.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class BrowserSelectionRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private final List<AvailableBrowser> items = new ArrayList();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        LayoutAvailableBrowserBinding binding = LayoutAvailableBrowserBinding.inflate(inflater, viewGroup, false);
        RecyclerView.ViewHolder viewHolder = new BrowserSelectionViewHolder(binding);

        return viewHolder;
    }

    public void setItems(Iterable<AvailableBrowser> items)
    {
        this.items.clear();
        items.forEach((item) -> this.items.add(item));
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        AvailableBrowser item = (AvailableBrowser)items.get(position);

        ((BrowserSelectionViewHolder)viewHolder).bind(item);
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }
}