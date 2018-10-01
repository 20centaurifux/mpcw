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
import android.support.wear.widget.drawer.*;
import android.support.wearable.activity.*;

import de.dixieflatline.mpcw.*;

public class MainActivity extends WearableActivity
{
    private NavigationUtil navigationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        navigationUtil = new NavigationUtil(this);

        setupDrawer();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        navigationUtil.openStartScreen();
    }

    private void setupDrawer()
    {
        WearableNavigationDrawerView wearableNavigationDrawer = findViewById(R.id.top_navigation_drawer);

        wearableNavigationDrawer.setAdapter(new NavigationAdapter(this));
        wearableNavigationDrawer.getController().peekDrawer();
        wearableNavigationDrawer.addOnItemSelectedListener(navigationUtil);
    }
}