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
import android.os.Bundle;
import android.support.wear.widget.drawer.WearableNavigationDrawerView;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;

import de.dixieflatline.mpcw.NavigationAdapter;
import de.dixieflatline.mpcw.R;

public class MainActivity extends WearableActivity
{
    private WearableNavigationDrawerView wearableNavigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null)
        {
            setContentView(R.layout.activity_main);

            wearableNavigationDrawer = findViewById(R.id.top_navigation_drawer);
            wearableNavigationDrawer.setAdapter(new NavigationAdapter(this));
            wearableNavigationDrawer.getController().peekDrawer();
            wearableNavigationDrawer.addOnItemSelectedListener((id) -> openFragment(id));

            openFragment(NavigationAdapter.PLAYER);

            setAmbientEnabled();
        }
    }

    void openFragment(int id)
    {
        FragmentManager manager = getFragmentManager();

        try
        {
            AFragment fragment = createFragmentById(id);

            if(fragment != null)
            {
                fragment.addOnSwipeListener((f) -> finish());

                manager.beginTransaction()
                       .replace(R.id.fragment_container, fragment)
                       .commit();
            }
        }
        catch(Exception ex)
        {
            Log.wtf("Navigation", ex);
        }
    }

    static AFragment createFragmentById(int id)
    {
        AFragment fragment;

        switch(id)
        {
            case NavigationAdapter.PLAYER:
                Log.v("Navigation", "Creating player.");
                fragment = new PlayerFragment();
                break;

            case NavigationAdapter.PREFERENCES:
                Log.v("Navigation", "Creating Preferences.");
                fragment = new PreferencesFragment();
                break;

            default:
                Log.v("Navigation", "Requested fragment not found.");
                fragment = null;
        }

        return fragment;
    }
}