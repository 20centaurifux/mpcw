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

public class RecurringRunnable implements Runnable
{
    private final long interval;
    private final Runnable runnable;
    private long lastDone;

    public RecurringRunnable(Runnable runnable, long interval)
    {
        this.interval = interval;
        this.runnable = runnable;
    }

    public boolean due()
    {
        long now = System.nanoTime() / 1000000;

        return lastDone == 0 || now - lastDone >= interval;
    }

    @Override
    public void run()
    {
        runnable.run();
        lastDone = System.nanoTime() / 1000000;
    }
}