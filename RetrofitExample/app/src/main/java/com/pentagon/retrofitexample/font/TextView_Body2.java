package com.pentagon.retrofitexample.font;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class TextView_Body2 extends TextView {
    public TextView_Body2(Context context) {
        super(context);
        init();
    }

    public TextView_Body2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @SuppressLint("Instantiatable")
	public TextView_Body2(Context context, AttributeSet attrs, int defStyle) {
         super(context, attrs, defStyle);
         init();
    }

    private void init() {


        String otfName = "fonts/Roboto-Medium.ttf";
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), otfName);
        this.setTypeface(font);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//        this.setTextColor(ContextCompat.getColor(context, R.color.opaque_50_black_bg));
    }
}
