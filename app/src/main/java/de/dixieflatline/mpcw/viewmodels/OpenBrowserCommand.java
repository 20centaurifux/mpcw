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
        Bundle bundle = new Bundle();

        switch(browser)
        {
            case Artists:
                bundle.putSerializable("VIEW", EBrowserViewKind.ARTIST_ALBUM_SONG);
                break;

            case Albums:
                bundle.putSerializable("VIEW", EBrowserViewKind.ALBUM_ARTIST_SONG);
                break;
        }

        Intent intent = new Intent(context, BrowserActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);

        context.startActivity(intent);
    }
}