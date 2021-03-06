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

import android.os.*;
import androidx.wear.widget.drawer.*;
import android.view.*;

import javax.inject.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.services.*;

public class MainActivity extends AInjectableActivity
{
    @Inject
    IPreferencesService service;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        inject();

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        MainNavigation navigation = new MainNavigation(this);

        if(service.isConfigured())
        {
            setupDrawer(navigation);
            navigation.openPlayer();
        }
        else
        {
            navigation.startWizard();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        findViewById(R.id.fragment_container).setVisibility(View.GONE);
    }

    private void setupDrawer(MainNavigation navigation)
    {
        WearableNavigationDrawerView wearableNavigationDrawer = findViewById(R.id.top_navigation_drawer);

        wearableNavigationDrawer.setAdapter(new NavigationAdapter(this));
        wearableNavigationDrawer.getController().peekDrawer();
        wearableNavigationDrawer.addOnItemSelectedListener(navigation);
    }
}
