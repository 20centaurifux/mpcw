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

import android.os.Bundle;

import androidx.databinding.*;
import androidx.wear.widget.*;

import javax.inject.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.services.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class BrowserSelectionActivity extends AInjectableActivity
{
    @Inject
    IBrowserAvailabilityService browserAvailabilityService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        inject();

        DataBindingUtil.setContentView(this, R.layout.activity_browser_selection);

        setupRecyclerView();
    }

    private void setupRecyclerView()
    {
        WearableRecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setEdgeItemsCenteringEnabled(true);

        WearableRecyclerView.LayoutManager layoutManager = new WearableLinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        BrowserSelectionRecyclerAdapter adapter = new BrowserSelectionRecyclerAdapter();

        recyclerView.setAdapter(adapter);

        Iterable<AvailableBrowser> items = browserAvailabilityService.getAvailableBrowsers();

        adapter.setItems(items);
    }
}
