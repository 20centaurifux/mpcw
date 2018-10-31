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

import android.databinding.*;
import android.support.v7.widget.*;
import android.view.*;

import java.util.*;

import de.dixieflatline.mpcw.*;
import de.dixieflatline.mpcw.databinding.*;
import de.dixieflatline.mpcw.databinding.LayoutMessageBinding;
import de.dixieflatline.mpcw.databinding.LayoutPasswordBinding;
import de.dixieflatline.mpcw.databinding.LayoutServerBinding;
import de.dixieflatline.mpcw.viewmodels.*;

public class WizardRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private final List<BaseObservable> items = new ArrayList<>();

    @Override
    public int getItemViewType(int position)
    {
        return items.get(position).getClass().hashCode();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RecyclerView.ViewHolder viewHolder;

        if(checkViewType(viewType, Message.class))
        {
            viewHolder = createViewHolder(R.layout.layout_message,
                                          viewGroup,
                                          (IBinder<LayoutMessageBinding, Message>)(b, m) -> b.setMessage(m));
        }
        else if(checkViewType(viewType, Server.class))
        {
            viewHolder = createViewHolder(R.layout.layout_server,
                                          viewGroup,
                                          (IBinder<LayoutServerBinding, Server>)(b, m) -> b.setServer(m));
        }
        else if(checkViewType(viewType, Confirmation.class))
        {
            viewHolder = createViewHolder(R.layout.layout_confirmation,
                                          viewGroup,
                                          (IBinder<LayoutConfirmationBinding, Confirmation>)(b, m) -> b.setConfirmation(m));
        }
        else if(checkViewType(viewType, Password.class))
        {
            viewHolder = createViewHolder(R.layout.layout_password,
                                          viewGroup,
                                          (IBinder<LayoutPasswordBinding, Password>)(b, m) -> b.setPassword(m));
        }
        else if(checkViewType(viewType, Action.class))
        {
            LayoutActionBinding binding = LayoutActionBinding.inflate(inflater, viewGroup, false);

            viewHolder = new ActionViewHolder(binding);
        }
        else
        {
            throw new RuntimeException("Unexpected view type.");
        }

        return viewHolder;
    }

    private static boolean checkViewType(int viewType, Class clazz)
    {
        return clazz.hashCode() == viewType;
    }

    private <TBinding extends ViewDataBinding, TViewModel extends BaseObservable> RecyclerView.ViewHolder
    createViewHolder(int layoutId, ViewGroup viewGroup, IBinder<TBinding, TViewModel> binder) // macros would be awesome :)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        TBinding binding = DataBindingUtil.inflate(inflater, layoutId, viewGroup, false);

        return ABindableViewHolder.newInstance(binding, binder);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        BaseObservable item = items.get(position);

        ABindableViewHolder<?> bindableViewHolder = (ABindableViewHolder<?>) viewHolder;

        bindableViewHolder.bind(item);
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public void append(BaseObservable viewModel)
    {
        items.add(viewModel);
        notifyItemInserted(items.size() - 1);
    }

    public int insertAfter(BaseObservable previous, BaseObservable viewModel)
    {
        int offset = items.indexOf(previous);

        if(offset != -1)
        {
            ++offset;

            items.add(offset, viewModel);

            notifyItemInserted(offset);
        }

        return offset;
    }

    public void remove(BaseObservable viewModel)
    {
        int offset = items.indexOf(viewModel);

        if(offset != -1)
        {
            items.remove(offset);
            notifyItemRemoved(offset);
        }
    }
}