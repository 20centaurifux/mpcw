package de.dixieflatline.mpcw.di;

import javax.inject.Singleton;

import dagger.Component;

import de.dixieflatline.mpcw.views.PlayerFragment;
import de.dixieflatline.mpcw.views.PreferencesFragment;

@Singleton
@Component(modules = { ServicesModule.class })
public interface IComponent
{
    void inject(PreferencesFragment fragment);
    void inject(PlayerFragment fragment);
}