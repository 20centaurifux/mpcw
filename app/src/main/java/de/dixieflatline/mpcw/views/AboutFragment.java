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

import androidx.databinding.*;
import android.os.Bundle;
import android.view.*;

import javax.inject.*;

import de.dixieflatline.mpcw.R;
import de.dixieflatline.mpcw.databinding.*;
import de.dixieflatline.mpcw.services.*;

public class AboutFragment extends AInjectableFragment
{
    @Inject
    IAboutService aboutService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        inject();

        FragmentAboutBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);
        binding.setAbout(aboutService.getAbout());

        return binding.getRoot();
    }
}
