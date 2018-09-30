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

import android.content.*;
import android.os.*;
import android.support.wear.widget.drawer.*;
import android.support.wearable.activity.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.di.*;

public abstract class AActivity extends WearableActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        inject();
    }

    private void inject()
    {
        Context context = getApplicationContext();
        Injector injector = new Injector(context);

        injector.inject(this);
    }

    protected void setupDrawer(int currentItem)
    {
        WearableNavigationDrawerView wearableNavigationDrawer = findViewById(R.id.top_navigation_drawer);

        wearableNavigationDrawer.setAdapter(new NavigationAdapter(this));
        wearableNavigationDrawer.setCurrentItem(currentItem, false);
        wearableNavigationDrawer.getController().peekDrawer();
        wearableNavigationDrawer.addOnItemSelectedListener(new NavigationItemSelectionListener(this));
    }
}