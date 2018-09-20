package de.dixieflatline.mpcw.services;

public interface IPlayerService
{
    void connectAsync();
    void disconnect();
    void next();
    void previous();
    void play();
    void pause();
    void stop();
    void addListener(IPlayerListener listener);
    void removeListener(IPlayerListener listener);
}