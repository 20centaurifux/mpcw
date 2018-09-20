package de.dixieflatline.mpcw.services;

public interface IPlayerListener
{
    void onConnectionChanged(boolean isConnected);
    void onArtistChanged(String artist);
    void onTitleChanged(String title);
    void onStatusChanged(EPlayerStatus status);
    void onHasPreviousChanged(boolean hasPrevious);
    void onHasNextChanged(boolean hasNext);
    void onException(Exception reason);
}