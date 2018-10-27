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

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;

import android.support.v7.widget.*;
import android.view.View;
import android.widget.*;

import java.io.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class BindingAdapters
{
    @BindingAdapter("visibleIf")
    public static void changeVisibilityIfTrue(View view, boolean visible)
    {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("visibleIfNot")
    public static void changeVisibilityIfFalse(View view, boolean notVisible)
    {
        view.setVisibility(!notVisible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("enableIf")
    public static void enableIfTrue(View view, boolean enabled)
    {
        float alpha = 1;

        if(!enabled)
        {
            alpha = .5f;
        }

        view.setAlpha(alpha);
        view.setEnabled(enabled);
    }

    @BindingAdapter({"text", "placeholder"})
    public static void setText(TextView view, String text, String placeHolder)
    {
        if(text == null || text.isEmpty())
        {
            view.setText(placeHolder);
        }
        else
        {
            view.setText(text);
        }
    }

    @BindingAdapter("textFromInteger")
    public static void setTextAttrFromInteger(TextView view, int value)
    {
        if(textChanged(view, value))
        {
            String newText = "";

            if(value != 0)
            {
                newText = Integer.toString(value);
            }

            view.setText(newText);
        }
    }

    private static boolean textChanged(TextView view, int newValue)
    {
        String oldText = view.getText().toString();
        String newText = Integer.toString(newValue);
        boolean changed = !oldText.equals(newText);

        if(changed)
        {
            changed = newValue != 0 || !oldText.isEmpty();
        }

        return changed;
    }

    @InverseBindingAdapter(attribute = "textFromInteger", event = "android:textAttrChanged")
    public static int getIntegerFromTextAttr(TextView view)
    {
        String text = view.getText().toString();
        int n = 0;

        try
        {
            n = Integer.parseInt(text);
        }
        catch(Exception ex) { }

        return n;
    }

    @BindingAdapter("playerStatusBackground")
    public static void setBackgroundFromPlayerStatus(View view, int status)
    {
        int id;

        if(status == Player.PLAY)
        {
            id = R.drawable.ic_pause_circle_outline;
        }
        else
        {
            id = R.drawable.ic_play_circle_outline;
        }

        view.setBackground(view.getContext().getDrawable(id));
    }

    @BindingAdapter("foundItems")
    public static void insertFoundItems(RecyclerView view, Iterable<Object> items)
    {
        RecyclerView.Adapter adapter = view.getAdapter();

        if(adapter instanceof  BrowserRecyclerAdapter)
        {
            ((BrowserRecyclerAdapter)adapter).setItems(items);
        }
    }
}