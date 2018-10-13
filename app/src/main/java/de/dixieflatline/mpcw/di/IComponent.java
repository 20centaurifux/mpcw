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
package de.dixieflatline.mpcw.di;

import javax.inject.Singleton;

import dagger.Component;

import de.dixieflatline.mpcw.viewmodels.*;
import de.dixieflatline.mpcw.views.*;

@Singleton
@Component(modules = { ServicesModule.class })
public interface IComponent
{
    void inject(PlayerFragment fragment);
    void inject(PreferencesFragment fragment);
    void inject(AboutFragment fragment);
    void inject(BrowserActivity fragment);
    void inject(AppendArtistCommand command);
    void inject(AppendAlbumCommand command);
}