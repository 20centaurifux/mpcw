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
package de.dixieflatline.mpcw.viewmodels;

import androidx.databinding.*;

import de.dixieflatline.mpcw.*;

public class About extends BaseObservable
{
    private String version;
    private String author;
    private String date;
    private String website;
    private String license;

    @Bindable
    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
        notifyPropertyChanged(BR.version);
    }

    @Bindable
    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        notifyPropertyChanged(BR.author);
        this.author = author;
    }

    @Bindable
    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        notifyPropertyChanged(BR.date);
        this.date = date;
    }

    @Bindable
    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String website)
    {
        notifyPropertyChanged(BR.website);
        this.website = website;
    }

    @Bindable
    public String getLicense()
    {
        return license;
    }

    public void setLicense(String license)
    {
        notifyPropertyChanged(BR.license);
        this.license = license;
    }
}
