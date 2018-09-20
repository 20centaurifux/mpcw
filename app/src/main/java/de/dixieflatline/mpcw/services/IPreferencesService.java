package de.dixieflatline.mpcw.services;

import de.dixieflatline.mpcw.viewmodels.Preferences;

public interface IPreferencesService
{
    Preferences load();

    void save(Preferences preferences);
}