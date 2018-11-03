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

import android.util.*;

import javax.inject.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.services.*;

public class AppendArtistCommand implements ITagCommand
{
    @Inject IBrowserService browserService;

    @Override
    public void run(Tag artist)
    {
        Thread thread = new Thread(() ->
        {
            try
            {
                browserService.appendSongsFromArtist(artist.getValue());
            }
            catch(Exception ex)
            {
                Log.e(Tags.COMMAND, "Couldn't append artist.", ex);
            }
        });

        thread.start();
    }
}
