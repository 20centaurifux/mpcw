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

import java.util.*;
import java.util.stream.*;

import javax.inject.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.databinding.*;
import de.dixieflatline.mpcw.services.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class BrowserActivity extends AInjectableActivity
{
    private final Browser browser = new Browser();
    private final Handler handler = new Handler();
    private Thread thread;

    private final IAsyncCommandListener<Tag> tagCommandListener = new IAsyncCommandListener<Tag>()
    {
        @Override
        public void onSuccess(Tag result)
        {
            postNotification(result.getValue());
        }

        @Override
        public void onFailed(Exception cause)
        {
            postNotification(R.string.notification_playlist_operation_failed);
        }
    };

    private final IAsyncCommandListener<Song> songCommandListener = new IAsyncCommandListener<Song>()
    {
        @Override
        public void onSuccess(Song result)
        {
            postNotification(result.getDisplayTitle());
        }

        @Override
        public void onFailed(Exception cause)
        {
            postNotification(R.string.notification_playlist_operation_failed);
        }
    };

    @Inject
    IBrowserService browserService;

    @Inject
    INetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        inject();

        ActivityBrowserBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_browser);
        binding.setBrowser(browser);

        setupRecyclerView();

        networkManager.addListener(new INetworkManagerListener()
        {
            @Override
            public void onConnected()
            {
                queryAsync();
            }

            @Override
            public void onFailure(Exception cause)
            {
                handler.post(() -> browser.fail(cause));
            }
        });

        networkManager.connect();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        try
        {
            thread.interrupt();
            thread.join(500);
        }
        catch(InterruptedException ex) { }

        networkManager.release();
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

                handler.post(() -> browser.finish());
            }
            catch(Exception ex)
            {
                handler.post(() -> browser.fail(ex));
            }
        });

        thread.start();
    }

    private void loadAllArtists() throws Exception
    {
        Activity activity = this;
        Iterator<String> artistIterator = browserService.getAllArtists().iterator();
        ICommand<Tag> activateTagCommand = new BrowseArtistCommand(activity);
        AAsyncCommand selectTagCommand = new AppendArtistCommand();

        inject(selectTagCommand);
        selectTagCommand.addListener(tagCommandListener);

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
                tag.setTagSelectCommand(selectTagCommand);

                return tag;
            }
        };

        handler.post(() -> browser.setTags(tags));
    }

    private void loadAlbums(String artist) throws Exception
    {
        Activity activity = this;
        Iterator<String> albumIterator = browserService.getAlbumsByArtist(artist).iterator();
        ICommand<Tag> activateTagCommand = new BrowseAlbumCommand(activity, artist);
        AAsyncCommand selectTagCommand = new AppendAlbumCommand(artist);

        inject(selectTagCommand);
        selectTagCommand.addListener(tagCommandListener);

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
                tag.setTagSelectCommand(selectTagCommand);

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
        AAsyncCommand<Song> selectSongCommand = new AppendSongCommand();

        inject(selectSongCommand);
        selectSongCommand.addListener(songCommandListener);

        List<Song> songList = new ArrayList<>();

        for(Song song : songs)
        {
            song.setSongSelectCommand(selectSongCommand);
            songList.add(song);
        }

        return songList;
    }

    private void postNotification(String message)
    {
        handler.post(() ->
        {
            Notification notification = new Notification(this);

            notification.show(message);
        });
    }

    private void postNotification(int resourceId)
    {
        handler.post(() ->
        {
            Notification notification = new Notification(this);

            notification.show(resourceId);
        });
    }
}