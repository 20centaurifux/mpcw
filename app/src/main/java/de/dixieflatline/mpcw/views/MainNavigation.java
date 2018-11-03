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

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.wear.widget.drawer.*;

import de.dixieflatline.mpcw.*;

public class MainNavigation implements WearableNavigationDrawerView.OnItemSelectedListener
{
    private final Activity currentActivity;

    public MainNavigation(Activity currentActivity)
    {
        this.currentActivity = currentActivity;
    }

    @Override
    public void onItemSelected(int index)
    {
        Fragment fragment;

        switch(index)
        {
            case NavigationAdapter.PLAYER:
                fragment = new PlayerFragment();
                break;

            case NavigationAdapter.PREFERENCES:
                fragment = new PreferencesFragment();
                break;

            case NavigationAdapter.ABOUT:
                fragment = new AboutFragment();
                break;

            default:
                throw new RuntimeException("Couldn't map fragment.");
        }

        navigateTo(fragment);
    }

    public void openPlayer()
    {
        navigateTo(new PlayerFragment());
    }

    public void openConnectionFailure(Exception ex)
    {
        Bundle bundle = new Bundle();

        bundle.putString("message", ex.getLocalizedMessage());

        Fragment fragment = new ConnectionFailureFragment();

        fragment.setArguments(bundle);

        navigateTo(fragment);
    }

    private void navigateTo(Fragment fragment)
    {
        FragmentManager manager = currentActivity.getFragmentManager();

        manager.beginTransaction()
               .replace(R.id.fragment_container, fragment)
               .commit();
    }

    public void startWizard()
    {
        Intent intent  = new Intent(currentActivity, WizardActivity.class);

        currentActivity.startActivity(intent);
        currentActivity.finish();
    }
}