package de.dixieflatline.mpcw.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;

import de.dixieflatline.mpcw.di.Injector;

public abstract class AInjectableFragment extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();
        Context context = activity.getApplicationContext();
        Injector injector = new Injector(context);

        injector.inject(this);
    }
}