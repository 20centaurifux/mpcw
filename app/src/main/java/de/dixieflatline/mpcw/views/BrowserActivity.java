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
package de.dixieflatline.mpcw.views;

import android.app.*;
import android.content.*;
import android.databinding.*;
import android.os.*;
import android.support.wear.widget.*;
import android.util.*;

import java.util.*;

import javax.inject.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.databinding.*;
import de.dixieflatline.mpcw.services.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class BrowserActivity extends AInjectableActivity
{
    private static final int ARTISTS = 0;
    private static final int ALBUMS = 1;
    private static final int SONGS = 2;

    private ActivityBrowserBinding binding;
    private Browser browser = new Browser();
    private Thread thread;
    private final Handler handler = new Handler();
    private int mode = ARTISTS;

    @Inject IBrowserService browserService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        inject();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_browser);
        binding.setBrowser(browser);

        setupRecyclerView();
        queryAsync();
    }

    private void setupRecyclerView()
    {
        WearableRecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setEdgeItemsCenteringEnabled(true);

        WearableRecyclerView.LayoutManager layoutManager = new WearableLinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new BrowserRecyclerAdapter());
    }

    private void queryAsync()
    {
        thread = new Thread(() ->
        {
            try
            {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();

                if(extras != null && extras.containsKey("ARTIST_FILTER"))
                {
                    if(extras.containsKey("ALBUM_FILTER"))
                    {
                        mode = SONGS;
                        loadSongs(extras.getString("ARTIST_FILTER"), extras.getString("ALBUM_FILTER"));
                    }
                    else
                    {
                        mode = ALBUMS;
                        loadAlbums(extras.getString("ARTIST_FILTER"));
                    }
                }
                else
                {
                    loadAllArtists();
                }

                handler.post(() -> browser.setLoaded(true));
            }
            catch(Exception ex)
            {
                Log.e("Browser", ex.getMessage());
            }
        });

        thread.start();
    }

    private void loadAllArtists() throws Exception
    {
        Activity activity = this;

        Iterable<Tag> tags = new Iterable<Tag>()
        {
            private final Iterator<String> artistIterator = browserService.getAllArtists().iterator();
            private final ITagActivateCommand activateTagCommand = new BrowseArtistCommand(activity);

            public Iterator<Tag> iterator()
            {
                return new Iterator<Tag>()
                {
                    @Override
                    public boolean hasNext()
                    {
                        return artistIterator.hasNext();
                    }

                    @Override
                    public Tag next()
                    {
                        Tag tag = Tag.Artist(artistIterator.next());

                        tag.setTagActivateCommand(activateTagCommand);

                        return tag;
                    }
                };
            }
        };

        handler.post(() -> browser.setTags(tags));
    }

    private void loadAlbums(String artist) throws Exception
    {
        Activity activity = this;

        Iterable<Tag> tags = new Iterable<Tag>()
        {
            private final Iterator<String> albumIterator = browserService.getAlbumsByArtist(artist).iterator();
            private final ITagActivateCommand activateTagCommand = new BrowseAlbumCommand(activity, artist);

            public Iterator<Tag> iterator()
            {
                return new Iterator<Tag>()
                {
                    @Override
                    public boolean hasNext()
                    {
                        return albumIterator.hasNext();
                    }

                    @Override
                    public Tag next()
                    {
                        Tag tag = Tag.Album(albumIterator.next());

                        tag.setTagActivateCommand(activateTagCommand);

                        return tag;
                    }
                };
            }
        };

        Iterable<Song> songs = browserService.getSongsByAlbum(artist, "");

        handler.post(() ->
        {
            browser.setTags(tags);
            browser.setSongs(songs);
        });
    }

    private void loadSongs(String artist, String album) throws Exception
    {
        Iterable<Song> songs = browserService.getSongsByAlbum(artist, album);

        handler.post(() ->
        {
            browser.setSongs(songs);
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        thread.interrupt();

        try
        {
            thread.join();
        }
        catch(InterruptedException ex) { }
    }
}