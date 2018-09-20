package de.dixieflatline.mpcw;

import android.content.Context;
import android.support.wear.widget.drawer.WearableNavigationDrawerView;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class NavigationAdapter extends WearableNavigationDrawerView.WearableNavigationDrawerAdapter
{
    public static final int PLAYER = 0;
    public static final int PREFERENCES = 1;

    private Context context;
    private ArrayList<String> texts = new ArrayList<String>();
    private ArrayList<Drawable> drawables = new ArrayList<Drawable>();

    public NavigationAdapter(Context context)
    {
        this.context = context;

        texts.add("Play");
        texts.add("Preferences");

        drawables.add(this.context.getDrawable(android.R.drawable.ic_media_play));
        drawables.add(this.context.getDrawable(android.R.drawable.ic_menu_preferences));
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