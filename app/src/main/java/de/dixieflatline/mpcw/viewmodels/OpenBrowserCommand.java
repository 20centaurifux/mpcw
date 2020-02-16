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
package de.dixieflatline.mpcw.viewmodels;

import android.content.*;
import android.os.*;

import de.dixieflatline.mpcw.views.*;

public class OpenBrowserCommand implements ICommand<EBrowser>
{
    private final Context context;

    public OpenBrowserCommand(Context context)
    {
        this.context = context;
    }

    @Override
    public void run(EBrowser browser)
    {
        Intent intent = null;

        switch(browser)
        {
            case Artists:
                intent = new Intent(context, BrowserActivity.class);
                break;

            case Albums:
                Bundle bundle = new Bundle();

                bundle.putString("ARTIST_FILTER", "");

                intent = new Intent(context, BrowserActivity.class);

                intent.putExtras(bundle);
                break;
        }

        context.startActivity(intent);
    }
}