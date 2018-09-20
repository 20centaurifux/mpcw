package de.dixieflatline.mpcw.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import de.dixieflatline.mpcw.BR;

public class Preferences extends BaseObservable
{
    private String hostname;
    private int port;
    private boolean authenticationEnabled = false;
    private String username;
    private String password;

    @Bindable
    public String getHostname()
    {
        return hostname;
    }

    public void setHostname(String hostname)
    {
        this.hostname = hostname;
        notifyPropertyChanged(BR.hostname);
    }

    @Bindable
    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
        notifyPropertyChanged(BR.port);
    }

    @Bindable
    public boolean getAuthenticationEnabled()
    {
        return authenticationEnabled;
    }

    public void setAuthenticationEnabled(boolean authenticationEnabled)
    {
        this.authenticationEnabled = authenticationEnabled;
        notifyPropertyChanged(BR.authenticationEnabled);
    }

    @Bindable
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}