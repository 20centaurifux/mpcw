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

import de.dixieflatline.mpcw.databinding.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class SongViewHolder extends RecyclerView.ViewHolder
{
    private final LayoutSongBinding binding;

    public SongViewHolder(LayoutSongBinding binding)
    {
        super(binding.getRoot());

        this.binding = binding;
    }

    public void bind(Song song)
    {
        binding.setSong(song);

        View view = binding.getRoot();

        view.setOnLongClickListener(v ->
        {
            song.getSongSelectCommand().run(song);

            return true;
        });
    }
}