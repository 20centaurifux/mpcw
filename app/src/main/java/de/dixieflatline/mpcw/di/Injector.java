package de.dixieflatline.mpcw.di;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Injector
{
    private static final String TAG = "Injector";

    private Context context;

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
            Log.d(TAG, "inject() not found.");
        }
        catch(IllegalAccessException ex)
        {
            Log.w(TAG, ex);
        }
        catch(InvocationTargetException ex)
        {
            Log.wtf(TAG, ex);
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