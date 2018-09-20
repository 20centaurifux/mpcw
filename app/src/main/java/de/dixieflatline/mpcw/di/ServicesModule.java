package de.dixieflatline.mpcw.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

import de.dixieflatline.mpcw.services.IPlayerService;
import de.dixieflatline.mpcw.services.IPreferencesService;
import de.dixieflatline.mpcw.services.implementation.MPDPlayerService;
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
        return new MPDPlayerService();
    }
}