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

import androidx.databinding.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.databinding.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class ActionViewHolder extends ABindableViewHolder<LayoutActionBinding>
{
    public ActionViewHolder(LayoutActionBinding binding)
    {
        super(binding);
    }

    public void bind(BaseObservable viewModel)
    {
        FloatingActionButton button = binding.getRoot().findViewById(R.id.button);
        Action action = (Action)viewModel;

        button.setOnClickListener(v ->
        {
            action.getCommand().run();
        });

        binding.setAction(action);
    }
}
