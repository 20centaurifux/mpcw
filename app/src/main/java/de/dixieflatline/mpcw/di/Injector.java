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
package de.dixieflatline.mpcw.di;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.dixieflatline.mpcw.*;

public class Injector
{
    private final Context context;

    public Injector(Context context)
    {
        this.context = context;
    }

    public void inject(Object cls)
    {
        IComponent component = buildComponent();

        try
        {
            Method method = IComponent.class.getMethod("inject", cls.getClass());

            method.invoke(component, cls);
        }
        catch(NoSuchMethodException ex)
        {
            Log.d(Tags.APOCALYPSE, "inject() not found.");
        }
        catch(IllegalAccessException ex)
        {
            Log.w(Tags.APOCALYPSE, ex);
        }
        catch(InvocationTargetException ex)
        {
            Log.wtf(Tags.APOCALYPSE, ex);
        }
    }

    private IComponent buildComponent()
    {
        IComponent component = DaggerIComponent.builder()
                                               .servicesModule(new ServicesModule(context))
                                               .build();

        return component;
    }
}