package de.dixieflatline.mpcw;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;

import android.view.View;
import android.widget.TextView;

public class BindingAdapters
{
    @BindingAdapter("visibleIf")
    public static void changeVisibilityIfTrue(View view, boolean visible)
    {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("visibleIfNot")
    public static void changeVisibilityIfFalse(View view, boolean notVisible)
    {
        view.setVisibility(!notVisible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("enableIf")
    public static void enableIfTrue(View view, boolean enabled)
    {
        float alpha = 1;

        if(!enabled)
        {
            alpha = .5f;
        }

        view.setAlpha(alpha);
        view.setEnabled(enabled);
    }

    @BindingAdapter("textFromInteger")
    public static void setTextAttrFromInteger(TextView view, int value)
    {
        if(textChanged(view, value))
        {
            String newText = "";

            if(value != 0)
            {
                newText = Integer.toString(value);
            }

            view.setText(newText);
        }
    }

    private static boolean textChanged(TextView view, int newValue)
    {
        String oldText = view.getText().toString();
        String newText = Integer.toString(newValue);
        boolean changed = !oldText.equals(newText);

        if(changed)
        {
            changed = newValue != 0 || !oldText.isEmpty();
        }

        return changed;
    }

    @InverseBindingAdapter(attribute = "textFromInteger", event = "android:textAttrChanged")
    public static int getIntegerFromTextAttr(TextView view)
    {
        String text = view.getText().toString();
        int n = 0;

        try
        {
            n = Integer.parseInt(text);
        }
        catch(Exception ex) { }

        return n;
    }
}