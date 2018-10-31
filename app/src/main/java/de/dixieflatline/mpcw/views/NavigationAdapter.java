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

import android.content.Context;
import android.support.wear.widget.drawer.WearableNavigationDrawerView;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

import de.dixieflatline.mpcw.*;

public class NavigationAdapter extends WearableNavigationDrawerView.WearableNavigationDrawerAdapter
{
    public static final int PLAYER = 0;
    public static final int PREFERENCES = 1;
    public static final int ABOUT = 2;

    private Context context;
    private ArrayList<String> texts = new ArrayList<>();
    private ArrayList<Drawable> drawables = new ArrayList<>();

    public NavigationAdapter(Context context)
    {
        this.context = context;

        texts.add(context.getString(R.string.title_fragment_player));
        texts.add(context.getString(R.string.title_fragment_preferences));
        texts.add(context.getString(R.string.title_fragment_about));

        drawables.add(this.context.getDrawable(R.drawable.ic_audiotrack));
        drawables.add(this.context.getDrawable(R.drawable.ic_settings));
        drawables.add(this.context.getDrawable(R.drawable.ic_help_outline));
    }

    @Override
    public int getCount()
    {
        return texts.size();
    }

    @Override
    public String getItemText(int pos)
    {
        return texts.get(pos);
    }

    @Override
    public Drawable getItemDrawable(int pos)
    {
        return drawables.get(pos);
    }
}