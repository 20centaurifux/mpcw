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

import de.dixieflatline.mpcw.viewmodels.*;

public interface IBrowserService
{
    Iterable<String> getAllArtists() throws Exception;
    Iterable<String> getAlbumsByArtist(String artist) throws Exception;
    Iterable<Song> getSongsByAlbum(String artist, String album) throws Exception;
    void appendSongsFromArtist(String artist) throws Exception;
    void appendSongsFromAlbum(String artist, String album) throws Exception;
    void appendSong(Song song) throws Exception;
}
