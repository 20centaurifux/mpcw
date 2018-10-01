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
import android.support.wear.widget.drawer.*;

public class NavigationItemSelectionListener implements WearableNavigationDrawerView.OnItemSelectedListener
{
    private final Activity currentActivity;

    public NavigationItemSelectionListener(Activity currentActivity)
    {
        this.currentActivity = currentActivity;
    }

    @Override
    public void onItemSelected(int index)
    {
        Activity activity;

        switch(index)
        {
            case NavigationAdapter.PLAYER:
                activity = new PlayerActivity();
                break;

            case NavigationAdapter.PREFERENCES:
                activity = new PreferencesActivity();
                break;

            default:
                throw new RuntimeException("Couldn't map activity.");
        }

        Intent intent = new Intent(currentActivity, activity.getClass());

        currentActivity.startActivity(intent);
        currentActivity.finish();
    }
}