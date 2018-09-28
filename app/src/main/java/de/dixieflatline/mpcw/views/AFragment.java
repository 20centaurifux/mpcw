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
import android.content.Context;
import android.os.Bundle;
import android.support.wear.widget.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import de.dixieflatline.mpcw.di.Injector;

public abstract class AFragment extends Fragment
{
    private final List<OnSwipeListener> listeners = new ArrayList<OnSwipeListener>();

    public void addOnSwipeListener(OnSwipeListener listener)
    {
        listeners.add(listener);
    }

    public void removeOnSwipeListener(OnSwipeListener listener)
    {
        listeners.remove(listener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        inject();

        View view = createView(inflater, container, savedInstanceState);

        return wrapView(view);
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    private void inject()
    {
        Activity activity = getActivity();
        Context context = activity.getApplicationContext();
        Injector injector = new Injector(context);

        injector.inject(this);
    }

    private View wrapView(View view)
    {
        AFragment self = this;
        SwipeDismissFrameLayout swipeLayout = new SwipeDismissFrameLayout(getActivity());

        swipeLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                 ViewGroup.LayoutParams.MATCH_PARENT));

        swipeLayout.addView(view);

        swipeLayout.addCallback(new SwipeDismissFrameLayout.Callback()
        {
            @Override
            public void onDismissed(SwipeDismissFrameLayout layout)
            {
                FragmentManager manager = getFragmentManager();

                manager.beginTransaction()
                       .remove(self)
                       .commit();

                listeners.forEach((l) -> l.onSwipe(self));
            }
        });

        return swipeLayout;
    }
}