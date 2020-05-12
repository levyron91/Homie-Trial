package com.swapping.homie.profile.seekbar;

import android.content.Context;
import android.util.AttributeSet;

import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

public class RangeSeekBar extends CrystalRangeSeekbar {


    public RangeSeekBar(Context context) {
        super(context);
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RangeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public float getBarHeight() {
        return 5;
    }

    @Override
    protected float getThumbWidth() {
        return 40;
    }

    @Override
    protected float getThumbHeight() { return 40; }

    @Override
    public CrystalRangeSeekbar setMinStartValue(float minStartValue) {
        return super.setMinStartValue(minStartValue);
    }

}
