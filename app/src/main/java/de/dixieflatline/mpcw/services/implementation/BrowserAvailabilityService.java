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
package de.dixieflatline.mpcw.services.implementation;

import android.content.*;

import java.util.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.services.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class BrowserAvailabilityService implements IBrowserAvailabilityService
{
    private final Context context;

    public BrowserAvailabilityService(Context context)
    {
        this.context = context;
    }

    public Iterable<AvailableBrowser> getAvailableBrowsers()
    {
        ArrayList<AvailableBrowser> available = new ArrayList();

        AvailableBrowser browser = new AvailableBrowser(EBrowser.Artists,
                                                        context.getResources().getString(R.string.browse_artists),
                                                        context.getDrawable(R.drawable.ic_artist_outline));

        browser.setSelectBrowserCommand(wrapCommand(new OpenBrowserCommand(context)));

        available.add(browser);

        browser = new AvailableBrowser(EBrowser.Albums,
                                       context.getResources().getString(R.string.browse_albums),
                                       context.getDrawable(R.drawable.ic_album_outline));

        browser.setSelectBrowserCommand(wrapCommand(new OpenBrowserCommand(context)));

        available.add(browser);

        return  available;
    }

    private static ICommand<AvailableBrowser> wrapCommand(OpenBrowserCommand cmd)
    {
        return browser ->
        {
            cmd.run(browser.getBrowserType());
        };
    }
}