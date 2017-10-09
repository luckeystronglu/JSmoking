package com.customview.selfheader;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzh.luckeylibrary.R;


/**
 * Created by Administrator on 2016/12/27.
 */

public class HeaderRightImageView extends RelativeLayout implements View.OnClickListener {
    private clickHeaderListener mListener;
    private final ImageView mLeftImg;
    private final ImageView mRightImg;
    private final TextView mMiddleText;
    private final LinearLayout mLeftLayout;
    private final LinearLayout mRightLayout;

    public HeaderRightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HeaderRightImageView);
        int leftIcon = array.getResourceId(R.styleable.HeaderRightImageView_leftIcon, R.drawable.icon_header_back);
        int rightIcon = array.getResourceId(R.styleable.HeaderRightImageView_rightIcon, R.drawable.header_share);
        String title = array.getString(R.styleable.HeaderRightImageView_middleTitle);
        int leftVisible = array.getInt(R.styleable.HeaderRightImageView_leftIconVisible, 0);
        int rightVisible = array.getInt(R.styleable.HeaderRightImageView_rightIconVisible, 0);
        array.recycle();
        View v = LayoutInflater.from(context).inflate(R.layout.header_right_imageview, this);
        mLeftImg = (ImageView) v.findViewById(R.id.iv_left);
        mRightImg = (ImageView) v.findViewById(R.id.iv_right);
        mMiddleText = (TextView) v.findViewById(R.id.tv_title);
        mLeftLayout = (LinearLayout) v.findViewById(R.id.ll_left);
        mRightLayout = (LinearLayout) v.findViewById(R.id.rl_right);
        mLeftImg.setImageResource(leftIcon);
        mRightImg.setImageResource(rightIcon);
        mMiddleText.setText(title);
        mLeftLayout.setOnClickListener(this);
        mRightLayout.setOnClickListener(this);
        switch (leftVisible) {
            case 0:
                mLeftImg.setVisibility(VISIBLE);
                mLeftLayout.setVisibility(VISIBLE);
                break;

            case 1:
                mLeftImg.setVisibility(INVISIBLE);

                break;
            case 2:
                mLeftImg.setVisibility(GONE);
                mLeftLayout.setVisibility(GONE);
                break;
        }
        switch (rightVisible) {
            case 0:
                mRightImg.setVisibility(VISIBLE);
                mRightLayout.setVisibility(VISIBLE);
                break;
            case 1:
                mRightImg.setVisibility(INVISIBLE);

                break;
            case 2:
                mRightImg.setVisibility(GONE);
                mRightLayout.setVisibility(GONE);
                break;
        }
    }

    public void setTitle(String title) {
        mMiddleText.setText(title);
    }


    public void setOnHeaderClickListener(clickHeaderListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_left) {
            if (mListener != null) {
                mListener.onClickLeftIcon();
            }
        }
        if (view.getId() == R.id.rl_right) {
            if (mListener != null) {
                mListener.onClickRightIcon(view);
            }
        }

//        switch (view.getId()) {
//            case R.id.ll_left:
//                if (mListener != null) {
//                    mListener.onClickLeftIcon();
//                }
//                break;
//            case R.id.rl_right:
//                if (mListener != null) {
//                    mListener.onClickRightIcon(view);
//                }
//                break;
//            default:
//                break;
//        }

    }

    public interface clickHeaderListener {
        void onClickLeftIcon();

        void onClickRightIcon(View view);
    }


}
