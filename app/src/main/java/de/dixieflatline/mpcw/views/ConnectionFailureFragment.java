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
import android.databinding.*;
import android.os.*;
import android.view.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.databinding.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class ConnectionFailureFragment extends Fragment
{
    private FragmentConnectionfailureBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle arguments = getArguments();
        String message = arguments.getString("message");
        Failure failure = new Failure();

        failure.setMessage(message);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_connectionfailure, container, false);
        binding.setFailure(failure);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.retry).setOnClickListener(v ->
        {
            NavigationUtil navigationUtil = new NavigationUtil(getActivity());

            navigationUtil.openStartScreen();
        });
    }
}