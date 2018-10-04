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
package de.dixieflatline.mpcw.services;

public interface IPlayerService
{
    void startAsync();
    void stopService();
    void next();
    void previous();
    void toggle();
    void playFromCurrentPlaylist(int position);
    void addConnectionListener(IConnectionListener listener);
    void removeConnectionListener(IConnectionListener listener);
    void addPlayerListener(IPlayerListener listener);
    void removePlayerListener(IPlayerListener listener);
    void addPlaylistListener(IPlaylistListener listener);
    void removePlaylistListener(IPlaylistListener listener);
}