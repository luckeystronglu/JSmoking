package com.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by lenovo on 2016/10/8.
 */
public class SelfListview extends ListView {
    public SelfListview(Context context) {
        this(context,null);
    }

    public SelfListview(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SelfListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
