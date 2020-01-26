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
import androidx.recyclerview.widget.*;

public abstract class ABindableViewHolder<TBinding extends ViewDataBinding> extends RecyclerView.ViewHolder
{
    protected final TBinding binding;

    public ABindableViewHolder(TBinding binding)
    {
        super(binding.getRoot());

        this.binding = binding;
    }

    public static <TBinding extends ViewDataBinding> ABindableViewHolder newInstance(TBinding binding, IBinder binder)
    {
        return new ABindableViewHolder(binding)
        {
            @Override
            public void bind(BaseObservable viewModel)
            {
                binder.bind(this.binding, viewModel);
            }
        };
    }

    public abstract void bind(BaseObservable viewModel);
}
