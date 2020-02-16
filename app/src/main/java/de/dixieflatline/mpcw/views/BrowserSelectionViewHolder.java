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

import de.dixieflatline.mpcw.databinding.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class BrowserSelectionViewHolder extends RecyclerView.ViewHolder
{
    private final LayoutAvailableBrowserBinding binding;

    public BrowserSelectionViewHolder(LayoutAvailableBrowserBinding binding)
    {
        super(binding.getRoot());

        this.binding = binding;
    }

    public void bind(AvailableBrowser browser)
    {
        binding.setBrowser(browser);

        View view = binding.getRoot();

        view.setOnClickListener(v ->
        {
            browser.getSelectBrowserCommand().run(browser);
        });
    }
}