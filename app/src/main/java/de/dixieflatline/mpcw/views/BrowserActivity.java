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
import android.view.*;
import android.widget.*;

import java.util.*;
import java.util.stream.*;

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
                        loadSongs(extras.getString("ARTIST_FILTER"), extras.getString("ALBUM_FILTER"));
                    }
                    else
                    {
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
        Iterator<String> artistIterator = browserService.getAllArtists().iterator();
        ITagCommand activateTagCommand = new BrowseArtistCommand(activity);
        ITagCommand selectTagCommand = new AppendArtistCommand();
        ITagCommand selectTagCommandWrapper = tag ->
        {
            postNotification(tag.getValue());
            selectTagCommand.run(tag);
        };

        inject(selectTagCommand);

        Iterable<Tag> tags = () -> new Iterator<Tag>()
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
                tag.setTagSelectCommand(selectTagCommandWrapper);

                return tag;
            }
        };

        handler.post(() -> browser.setTags(tags));
    }

    private void loadAlbums(String artist) throws Exception
    {
        Activity activity = this;
        Iterator<String> albumIterator = browserService.getAlbumsByArtist(artist).iterator();
        ITagCommand activateTagCommand = new BrowseAlbumCommand(activity, artist);
        ITagCommand selectTagCommand = new AppendAlbumCommand(artist);
        ITagCommand selectTagCommandWrapper = tag ->
        {
            postNotification(tag.getValue());
            selectTagCommand.run(tag);
        };

        inject(selectTagCommand);

        Iterable<Tag> tags = () -> new Iterator<Tag>()
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
                tag.setTagSelectCommand(selectTagCommandWrapper);

                return tag;
            }
        };

        Iterable<Tag> filteredTags = StreamSupport.stream(tags.spliterator(), false)
                                                  .filter(t -> !t.getValue().isEmpty())
                                                  ::iterator;

        Iterable<Song> songs = browserService.getSongsByAlbum(artist, "");

        handler.post(() ->
        {
            browser.setTags(filteredTags);
            browser.setSongs(injectSongs(songs));
        });
    }

    private void loadSongs(String artist, String album) throws Exception
    {
        Iterable<Song> songs = browserService.getSongsByAlbum(artist, album);

        handler.post(() ->
        {
            browser.setSongs(injectSongs(songs));
        });
    }

    private Iterable<Song> injectSongs(Iterable<Song> songs)
    {
        ISongCommand selectSongCommand = new AppendSongCommand();
        ISongCommand selectSongCommandWrapper = song ->
        {
            postNotification(song.getDisplayTitle());
            selectSongCommand.run(song);
        };

        inject(selectSongCommand);

        List<Song> songList = new ArrayList<Song>();

        for(Song song : songs)
        {
            song.setSongSelectCommand(selectSongCommandWrapper);
            songList.add(song);
        }

        return songList;
    }

    private void postNotification(String message)
    {
        handler.post(() ->
        {
            Toast toast = new Toast(getApplicationContext());

            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 15);

            LayoutToastBinding binding =  DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_toast, findViewById(R.id.toast_root), false);

            binding.setMessage(new ToastMessage(message));

            toast.setView(binding.getRoot());
            toast.show();
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