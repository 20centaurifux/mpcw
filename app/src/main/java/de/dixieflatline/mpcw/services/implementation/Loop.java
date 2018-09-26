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

import java.util.*;

public class Loop
{
    private final List<Runnable> tasks = new ArrayList<Runnable>();

    public void addTimeout(Runnable runnable)
    {
        tasks.add(runnable);
    }

    public void addInterval(Runnable runnable, long millis)
    {
        tasks.add(new RecurringRunnable(runnable, millis));
    }

    public void iterate()
    {
        for(Runnable runnable : tasks)
        {
            boolean run = true;

            if(runnable instanceof RecurringRunnable)
            {
                run = ((RecurringRunnable)runnable).due();
            }

            if(run)
            {
                runnable.run();
            }
        }
    }
}