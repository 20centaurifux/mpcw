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

import android.content.*;
import androidx.databinding.*;
import android.os.*;
import android.util.*;

import androidx.wear.widget.*;

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
    INetworkManager networkManager;

    @Inject
    IBrowserService browserService;

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
    }

    protected void onResume()
    {
        super.onResume();

        if(networkManager != null && !browser.getFinished())
        {
            networkManager.connect();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        try
        {
            if(thread != null)
            {
                thread.interrupt();
                thread.join(500);
            }
        }
        catch(InterruptedException _)
        {
            /* /\_/\
              ( o.o )
               > ^ <  */
        }
        finally
        {
            thread = null;
        }

        if(networkManager != null)
        {
            networkManager.release();
        }
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
                EBrowserViewKind viewKind = EBrowserViewKind.ARTIST_ALBUM_SONG;

                if(extras != null && extras.containsKey("VIEW"))
                {
                    viewKind = (EBrowserViewKind) extras.getSerializable("VIEW");
                }

                switch(viewKind)
                {
                    case ARTIST_ALBUM_SONG:
                        artistAlbumView(extras);
                        break;

                    case ALBUM_ARTIST_SONG:
                        albumArtistView(extras);
                        break;

                    default:
                        Log.d(Tags.APOCALYPSE, "View not found: " + viewKind);
                }

                handler.post(() -> browser.finish());
            }
            catch(InterruptedException _)
            {
                /* /\_/\
                  ( o o )
                  ==_Y_==
                    `-'  */
            }
            catch(Exception ex)
            {
                handler.post(() -> browser.fail(ex));
            }
        });

        thread.start();
    }

    private void artistAlbumView(Bundle extras) throws Exception
    {
        boolean hasArtistFilter = extras.containsKey("ARTIST_FILTER");

        if(hasArtistFilter)
        {
            String artist = extras.getString("ARTIST_FILTER");
            boolean hasAlbumFilter = extras.containsKey("ALBUM_FILTER");

            if(hasAlbumFilter)
            {
                String album = extras.getString("ALBUM_FILTER");

                loadSongs(artist, album);
            }
            else
            {
                loadAlbumsByArtist(artist);
            }
        }
        else
        {
            loadAllArtists();
        }
    }

    private void albumArtistView(Bundle extras) throws Exception
    {
        boolean hasAlbumFilter = extras.containsKey("ALBUM_FILTER");

        if(hasAlbumFilter)
        {
            String album = extras.getString("ALBUM_FILTER");
            boolean hasArtistFilter = extras.containsKey("ARTIST_FILTER");

            if(hasArtistFilter)
            {
                String artist = extras.getString("ARTIST_FILTER");

                loadSongs(artist, album);
            }
            else
            {
                loadArtistsByAlbum(album);
            }
        }
        else
        {
            loadAllAlbums();
        }
    }

    private void loadAllArtists() throws Exception
    {
        Iterator<String> artistIterator = browserService.getAllArtists()
                                                        .iterator();
        ICommand<Tag> activateTagCommand = new BrowseAlbumCommand(this);
        AAsyncCommand selectTagCommand = AppendArtistCommand.all();

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

    private void loadArtistsByAlbum(String album) throws Exception
    {
        Iterator<String> artistIterator = browserService.getArtistsByAlbum(album)
                                                        .iterator();
        ICommand<Tag> activateTagCommand = BrowseSongsCommand.byAlbumAndArtist(this, album);
        AAsyncCommand selectTagCommand = AppendArtistCommand.withAlbum(album);

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

    private void loadAllAlbums() throws Exception
    {
        Iterator<String> albumIterator = browserService.getAllAlbums()
                                                       .iterator();
        ICommand<Tag> activateTagCommand = new BrowseArtistCommand(this);
        AAsyncCommand selectTagCommand = AppendAlbumCommand.all();

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

        handler.post(() -> browser.setTags(tags));
    }

    private void loadAlbumsByArtist(String artist) throws Exception
    {
        Iterator<String> albumIterator = browserService.getAlbumsByArtist(artist)
                                                       .iterator();
        ICommand<Tag> activateTagCommand = BrowseSongsCommand.byArtistAndAlbum(this, artist);
        AAsyncCommand selectTagCommand = AppendAlbumCommand.withArtist(artist);

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

        Iterable<Song> songs = browserService.getSongsByArtistAndAlbum(artist, "");

        handler.post(() ->
        {
            browser.setTags(filteredTags);
            browser.setSongs(injectSongs(songs));
        });
    }

    private void loadSongs(String artist, String album) throws Exception
    {
        Iterable<Song> songs;

        if(artist == null)
        {
            artist = "";
        }

        if(album == null)
        {
            album = "";
        }

        if(!artist.isEmpty() && album.isEmpty())
        {
            songs = browserService.getSongsByArtist(artist);
        }
        else if(!album.isEmpty() && artist.isEmpty())
        {
            songs = browserService.getSongsByAlbum(album);
        }
        else
        {
            songs = browserService.getSongsByArtistAndAlbum(artist, album);
        }

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