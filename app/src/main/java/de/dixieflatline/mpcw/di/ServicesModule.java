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

import android.content.Context;

import dagger.Module;
import dagger.Provides;

import de.dixieflatline.mpcw.services.IPlayerService;
import de.dixieflatline.mpcw.services.IPreferencesService;
import de.dixieflatline.mpcw.services.implementation.PlayerService;
import de.dixieflatline.mpcw.services.implementation.SharedPreferencesService;

import javax.inject.Singleton;

@Module
public class ServicesModule
{
    private Context context;

    public ServicesModule(Context context)
    {
        this.context = context;
    }

    @Provides
    @Singleton
    public IPreferencesService providePreferencesService()
    {
        return new SharedPreferencesService(context);
    }

    @Provides
    @Singleton
    public IPlayerService providePlayerService()
    {
        IPreferencesService preferencesService = new SharedPreferencesService(context);

        return new PlayerService(preferencesService.load());
    }
}