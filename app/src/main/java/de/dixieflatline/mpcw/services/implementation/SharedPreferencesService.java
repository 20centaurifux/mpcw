package de.dixieflatline.mpcw.services.implementation;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import de.dixieflatline.mpcw.services.IPreferencesService;
import de.dixieflatline.mpcw.viewmodels.Preferences;

public class SharedPreferencesService implements IPreferencesService
{
    public static final String DEFAULT_MPD_HOSTNAME = "127.0.0.1";
    public static final int DEFAULT_MPD_PORT = 6000;

    private static final String NAME = "UserPreferences";
    private static final String MPD_HOSTNAME = "mpd.hostname";
    private static final String MPD_PORT = "mpd.port";
    private static final String MPD_AUTHENTICATION = "mpd.authentication";
    private static final String MPD_USERNAME = "mpd.username";
    private static final String MPD_PASSWORD = "mpd.password";

    private Context context;

    public SharedPreferencesService(Context context)
    {
        this.context = context;
    }

    @Override
    public Preferences load()
    {
        Preferences preferences = new Preferences();

        preferences.setHostname(getString(MPD_HOSTNAME, DEFAULT_MPD_HOSTNAME));
        preferences.setPort(getInt(MPD_PORT, DEFAULT_MPD_PORT));
        preferences.setAuthenticationEnabled(getBoolean(MPD_AUTHENTICATION, false));
        preferences.setUsername(getString(MPD_USERNAME, null));
        preferences.setPassword(getString(MPD_PASSWORD, null));

        return preferences;
    }

    String getString(String key, String defValue)
    {
        return getSharedPreferences().getString(key, defValue);
    }

    int getInt(String key, int defValue)
    {
        return getSharedPreferences().getInt(key, defValue);
    }

    boolean getBoolean(String key, boolean defValue)
    {
        return getSharedPreferences().getBoolean(key, defValue);
    }

    @Override
    public void save(Preferences preferences)
    {
        Editor editor = getSharedPreferences().edit();

        editor.putString(MPD_HOSTNAME, preferences.getHostname());
        editor.putInt(MPD_PORT, preferences.getPort());
        editor.putBoolean(MPD_AUTHENTICATION, preferences.getAuthenticationEnabled());
        editor.putString(MPD_USERNAME, preferences.getUsername());
        editor.putString(MPD_PASSWORD, preferences.getPassword());

        editor.apply();
    }

    SharedPreferences getSharedPreferences()
    {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }
}
