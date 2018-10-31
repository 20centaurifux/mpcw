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
package de.dixieflatline.mpcw.utils;

import java.util.*;

public class Loop
{
    private final List<RecurringRunnable> recurringRunnables = new ArrayList<>();
    private final Queue<Runnable> runnables = new LinkedList<>();

    public void add(Runnable runnable)
    {
        runnables.add(runnable);
    }

    public void add(RecurringRunnable runnable)
    {
        recurringRunnables.add(runnable);
    }

    public void iterate()
    {
        for(RecurringRunnable runnable : recurringRunnables)
        {
            if(runnable.due())
            {
                runnable.run();
            }
        }

        while(!runnables.isEmpty())
        {
            Runnable runnable = runnables.peek();

            try
            {
                runnable.run();
                runnables.remove();
            }
            catch(Exception ex)
            {
                throw ex;
            }
        }
    }
}