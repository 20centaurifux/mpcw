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
package de.dixieflatline.mpcw.services.implementation;

import android.content.*;
import android.content.pm.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.services.*;
import de.dixieflatline.mpcw.viewmodels.*;

public class AboutService implements IAboutService
{
    private final Context context;

    public AboutService(Context context)
    {
        this.context = context;
    }

    @Override
    public About getAbout()
    {
        About about = new About();

        try
        {
            PackageInfo packageInfo = getPackageInfo();

            about.setVersion(packageInfo.versionName);
        }
        catch(PackageManager.NameNotFoundException ex)
        {
            about.setVersion("n/a");
        }

        about.setDate(context.getString(R.string.app_date));
        about.setAuthor(context.getString(R.string.app_author));
        about.setLicense(context.getString(R.string.app_license));
        about.setWebsite(context.getString(R.string.app_website));

        return about;
    }

    private PackageInfo getPackageInfo() throws PackageManager.NameNotFoundException
    {
        PackageManager manager = context.getPackageManager();
        String packageName = context.getPackageName();

        return manager.getPackageInfo(packageName, 0);
    }
}