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

import androidx.recyclerview.widget.*;
import android.view.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.databinding.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class PlaylistItemViewHolder extends RecyclerView.ViewHolder
{
    private final LayoutPlaylistitemBinding binding;

    public PlaylistItemViewHolder(LayoutPlaylistitemBinding binding)
    {
        super(binding.getRoot());

        this.binding = binding;
    }

    public void bind(PlaylistItem item)
    {
        binding.setItem(item);

        View view = binding.getRoot();

        view.setOnClickListener(v ->
        {
            item.getSelectCommand().run(item, getAdapterPosition() - 1);
        });
    }
}
