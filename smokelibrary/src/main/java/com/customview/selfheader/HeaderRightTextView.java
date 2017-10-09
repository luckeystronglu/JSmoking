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

public class HeaderRightTextView extends RelativeLayout implements View.OnClickListener {
    private ClickHeaderListener mListener;
    private final ImageView mIvLeft;
    private final TextView mtvRight;
    private final TextView mTvTitle;
    private final LinearLayout mLLleft;


    public HeaderRightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HeaderRightTextView);
        int leftIcon = array.getResourceId(R.styleable.HeaderRightTextView_h2leftIcon, R.drawable.icon_header_back);
//        int rightIcon = array.getResourceId(R.styleable.header_right_imageview_rightIcon, R.drawable.header_share);
        String rightText = array.getString(R.styleable.HeaderRightTextView_rightText);
        String middleTitle = array.getString(R.styleable.HeaderRightTextView_h2middleTitle);
        int leftVisible = array.getInt(R.styleable.HeaderRightTextView_h2leftIconVisible, 0);
        int rightVisible = array.getInt(R.styleable.HeaderRightTextView_rightTextVisible, 0);
        array.recycle();
        View v = LayoutInflater.from(context).inflate(R.layout.header_right_textview, this);
        mIvLeft = (ImageView) v.findViewById(R.id.iv_left);
        mtvRight = (TextView) v.findViewById(R.id.tv_right);
        mTvTitle = (TextView) v.findViewById(R.id.tv_title);
        mLLleft = (LinearLayout) v.findViewById(R.id.ll_left);
        mIvLeft.setImageResource(leftIcon);
        mtvRight.setText(rightText);
        mTvTitle.setText(middleTitle);
        mLLleft.setOnClickListener(this);
        mtvRight.setOnClickListener(this);
        switch (leftVisible) {
            case 0:
                mIvLeft.setVisibility(VISIBLE);
                mLLleft.setVisibility(VISIBLE);
                break;

            case 1:
                mIvLeft.setVisibility(INVISIBLE);

                break;
            case 2:
                mIvLeft.setVisibility(GONE);
                mLLleft.setVisibility(GONE);
                break;
        }
        switch (rightVisible) {
            case 0:
                mtvRight.setVisibility(VISIBLE);
                break;
            case 1:
                mtvRight.setVisibility(INVISIBLE);

                break;
            case 2:
                mtvRight.setVisibility(GONE);
                break;
        }
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }


    public void setonclickListener(ClickHeaderListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_left) {
            if (mListener != null) {
                mListener.onClickLeftIcon();
            }
        }
        if (view.getId() == R.id.tv_right) {
            if (mListener != null) {
                mListener.onClickRightText(view);
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

    public interface ClickHeaderListener {
        void onClickLeftIcon();

        void onClickRightText(View view);
    }


}
