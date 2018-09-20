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

public class PreferencesFragment extends AInjectableFragment
{
    private FragmentPreferencesBinding binding;
    private Preferences preferences;

    @Inject IPreferencesService service;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
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