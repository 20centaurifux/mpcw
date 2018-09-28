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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.dixieflatline.mpcw.R;
import de.dixieflatline.mpcw.databinding.FragmentPreferencesBinding;
import de.dixieflatline.mpcw.services.IPreferencesService;
import de.dixieflatline.mpcw.viewmodels.Preferences;

public class PreferencesFragment extends AFragment
{
    private FragmentPreferencesBinding binding;
    private Preferences preferences;

    @Inject IPreferencesService service;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        preferences = service.load();

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_preferences, container, false);
        binding.setPreferences(preferences);

        return binding.getRoot();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        service.save(preferences);
    }
}