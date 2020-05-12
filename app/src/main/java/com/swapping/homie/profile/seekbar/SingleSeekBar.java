package com.swapping.homie.profile.seekbar;

import android.content.Context;
import android.util.AttributeSet;

import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;

public class SingleSeekBar extends CrystalSeekbar {




    public SingleSeekBar(Context context) {
        super(context);
    }

    public SingleSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SingleSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    public float getBarHeight() {
        return 5;
    }

    @Override
    public float getThumbWidth() {
        return 40;
    }

    @Override
    public float getThumbHeight() {
        return 40;
    }


}
