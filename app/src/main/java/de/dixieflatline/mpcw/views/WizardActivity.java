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
import android.content.res.*;
import android.databinding.*;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.support.wear.widget.*;

import javax.inject.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.services.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class WizardActivity extends AInjectableActivity
{
    private LinearLayoutManager layoutManager;
    private WizardRecyclerAdapter adapter;

    private final Server server = new Server();
    private final Confirmation enableAuthentication = new Confirmation();
    private final Password password = new Password();

    @Inject
    IPreferencesService service;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        inject();

        setContentView(R.layout.activity_wizard);
        setupRecyclerView();
        appendScreens(adapter);
    }

    private void setupRecyclerView()
    {
        WearableRecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setEdgeItemsCenteringEnabled(true);

        layoutManager = new LinearLayoutManager(this);

        layoutManager.setOrientation(WearableLinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new WizardRecyclerAdapter();

        recyclerView.setAdapter(adapter);

        PagerSnapHelper snapHelper = new PagerSnapHelper();

        snapHelper.attachToRecyclerView(recyclerView);
    }

    private void appendScreens(WizardRecyclerAdapter adapter)
    {
        appendWelcomeMessage();
        appendServerScreens();
        appendPasswordScreens();
        appendFinalScreen();
    }

    private void appendWelcomeMessage()
    {
        adapter.append(createMessage(R.string.wizard_welcome));
    }

    private void appendServerScreens()
    {
        adapter.append(createMessage(R.string.wizard_enter_server_details));
        adapter.append(server);
    }

    private void appendPasswordScreens()
    {
        enableAuthentication.setQuestion(getResources().getString(R.string.wizard_enable_authentication));
        adapter.append(enableAuthentication);

        Message passwordMessage = new Message(getString(R.string.wizard_enter_password));

        enableAuthentication.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback()
        {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId)
            {
                if(propertyId == BR.confirmed)
                {
                    if(enableAuthentication.getConfirmed())
                    {
                        int position = adapter.insertAfter(enableAuthentication, passwordMessage);
                        adapter.insertAfter(passwordMessage, password);

                        layoutManager.scrollToPosition(position);
                    }
                    else
                    {
                        adapter.remove(passwordMessage);
                        adapter.remove(password);
                    }
                }
            }
        });
    }

    private void appendFinalScreen()
    {
        Resources resources = getResources();
        Action action = new Action(resources.getString(R.string.wizard_complete));

        action.setDrawable(resources.getDrawable(R.drawable.ic_done));
        action.setCommand(() ->
        {
            Preferences preferences = service.load();

            preferences.setHostname(server.getHostname());
            preferences.setPort(server.getPort());

            if(enableAuthentication.getConfirmed())
            {
                preferences.setAuthenticationEnabled(true);
                preferences.setPassword(password.getPassword());
            }
            else
            {
                preferences.setAuthenticationEnabled(false);
            }

            service.save(preferences);

            Intent intent  = new Intent(this, MainActivity.class);

            startActivity(intent);
            finish();
        });

        adapter.append(action);
    }

    private Message createMessage(int id)
    {
        Resources resources = getResources();

        return new Message(resources.getString(id));
    }
}
