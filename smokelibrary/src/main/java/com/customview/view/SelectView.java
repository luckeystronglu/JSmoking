package com.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzh.luckeylibrary.R;


/**
 * Created by Administrator on 2017/1/11.
 */

public class SelectView extends LinearLayout {

    TextView mTvTitle,mTvContent;

    ImageView mIvMark,mIvRight;

    public SelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_select, this);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvContent = (TextView) view.findViewById(R.id.tv_content);
        mIvMark = (ImageView) view.findViewById(R.id.iv_mark);
        mIvRight = (ImageView) view.findViewById(R.id.iv_indicator);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SelectView);
        String title = array.getString(R.styleable.SelectView_SelectViewTitle);
        String hint = array.getString(R.styleable.SelectView_SelectViewHint);
        int indicatorImg = array.getResourceId(R.styleable.SelectView_SelectViewIndicatorImg, R.drawable
                .icon_calendar);
        int markVisible = array.getInt(R.styleable.SelectView_SelectViewMarkVisible, 2);
        int rightImgVisible = array.getInt(R.styleable.SelectView_SelectViewRightImgVisible, 0);
        array.recycle();
        mIvRight.setImageResource(indicatorImg);
        mTvTitle.setText(title);
        mTvContent.setHint(hint);
        switch (markVisible) {
            case 0:
                mIvMark.setVisibility(VISIBLE);
                break;

            case 1:
                mIvMark.setVisibility(INVISIBLE);

                break;
            case 2:
                mIvMark.setVisibility(GONE);

                break;
        }

        switch (rightImgVisible) {
            case 0:
                mIvRight.setVisibility(VISIBLE);
                break;

            case 1:
                mIvRight.setVisibility(INVISIBLE);

                break;
            case 2:
                mIvRight.setVisibility(GONE);
                break;
        }

    }

    public String getContent() {
        return mTvContent.getText().toString();
    }

    public void setContent(String s) {
        mTvContent.setText(s);
    }

}
