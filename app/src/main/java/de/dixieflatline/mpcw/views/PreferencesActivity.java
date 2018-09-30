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

import javax.inject.Inject;

import de.dixieflatline.mpcw.R;
import de.dixieflatline.mpcw.databinding.*;
import de.dixieflatline.mpcw.services.IPreferencesService;
import de.dixieflatline.mpcw.viewmodels.Preferences;

public class PreferencesActivity extends AActivity
{
    private ActivityPreferencesBinding binding;
    private Preferences preferences;

    @Inject IPreferencesService service;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        preferences = service.load();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_preferences);
        binding.setPreferences(preferences);

        setupDrawer(NavigationAdapter.PREFERENCES);
        setAmbientEnabled();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        service.save(preferences);
    }
}