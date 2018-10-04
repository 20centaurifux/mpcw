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

import android.databinding.*;
import android.os.*;
import android.support.wear.widget.*;
import android.util.*;

import javax.inject.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.databinding.*;
import de.dixieflatline.mpcw.services.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class BrowserActivity extends AInjectableActivity
{
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
                loadAllArtists();

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
        Iterable<String> artists = browserService.getAllArtists();

        handler.post(() -> browser.setTags(artists));
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