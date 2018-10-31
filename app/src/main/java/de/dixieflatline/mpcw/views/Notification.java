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
import android.databinding.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.databinding.*;
import de.dixieflatline.mpcw.viewmodels.Message;

public class Notification
{
    private final Handler handler = new Handler();
    private final Activity activity;

    public Notification(Activity activity)
    {
        this.activity = activity;
    }

    public void show(String message)
    {
        show(message, Gravity.CENTER, 0, 0);
    }

    private void show(String message, int gravity, int xOffset, int yOffset)
    {
        handler.post(() ->
        {
            Toast toast = new Toast(this.activity.getApplicationContext());

            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(gravity, xOffset, yOffset);

            LayoutToastBinding binding = DataBindingUtil.inflate(activity.getLayoutInflater(),
                                                                 R.layout.layout_toast,
                                                                 activity.findViewById(R.id.toast_root),
                                                  false);

            binding.setMessage(new Message(message));

            toast.setView(binding.getRoot());
            toast.show();
        });
    }
}