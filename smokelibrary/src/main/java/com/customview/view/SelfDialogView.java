package com.customview.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzh.luckeylibrary.R;


/**
 * Created by Administrator on 2017/09/05.
 */

public class SelfDialogView extends Dialog{
    private Context mContext;

    private OnSelfDialogCancelListener onSelfDialogCancelListener;
    private OnSelfDialogSureListener onSelfDialogSureListener;
    public void setOnSelfDialogCancelListener(OnSelfDialogCancelListener onSelfDialogCancelListener) {
        this.onSelfDialogCancelListener = onSelfDialogCancelListener;
    }

    public void setOnSelfDialogSureListener(OnSelfDialogSureListener onSelfDialogSureListener) {
        this.onSelfDialogSureListener = onSelfDialogSureListener;
    }

    private TextView self_dialog_tv_title,self_dialog_tv_desc,self_dialog_cancel,self_dialog_sure;
    private LinearLayout self_dialog_ll_title;
    private TextView self_dialog_horizontal_line,self_dialog_divider_vline; //分割线

    public SelfDialogView(Context context) {
        super(context);
        this.mContext = context;
    }

    public SelfDialogView(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }


    public SelfDialogView(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext,R.layout.dialog_self_ll,null);
        setContentView(view);
        initView(view);
    }

    private void initView(View view) {

        self_dialog_tv_title = (TextView) view.findViewById(R.id.self_dialog_tv_title);
        self_dialog_tv_desc = (TextView) view.findViewById(R.id.self_dialog_tv_desc);
        self_dialog_cancel = (TextView) view.findViewById(R.id.self_dialog_cancel);
        self_dialog_sure = (TextView) view.findViewById(R.id.self_dialog_sure);

        self_dialog_ll_title = (LinearLayout) view.findViewById(R.id.self_dialog_ll_title);

        self_dialog_horizontal_line = (TextView) view.findViewById(R.id.self_dialog_horizontal_line);
        self_dialog_divider_vline = (TextView) view.findViewById(R.id.self_dialog_divider_vline);

        setCanceledOnTouchOutside(false);

        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        win.setBackgroundDrawable(new BitmapDrawable());
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
//        int mHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        lp.width = width - 160;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.y = -120;
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;

        win.setAttributes(lp);

//        TypedArray array = mContext.obtainStyledAttributes(R.style.my_self_dialog,R.styleable.SelfDialogView);
//        String title = array.getString(R.styleable.SelfDialogView_SelfDialogTitle);
//        String dlg_desc = array.getString(R.styleable.SelfDialogView_SelfDialogDesc);
//        int descColor = array.getColor(R.styleable.SelfDialogView_SelfDialogDescTextColor, mContext.getResources().getColor(R.color.gray));

//        array.recycle();
//        self_dialog_tv_title.setText(title);
//        self_dialog_tv_desc.setText(dlg_desc);
//        self_dialog_tv_desc.setTextColor(descColor);

        initEvent();
    }

    private void initEvent() {
        self_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSelfDialogCancelListener != null) {
                    onSelfDialogCancelListener.DialogCancelClickListener();
                }
//                dismiss();
            }
        });

        self_dialog_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSelfDialogSureListener != null) {
                    onSelfDialogSureListener.DialogSureClickListener();
                }
//                dismiss();
            }
        });

    }

    //  标题
    public void setDialogTitleBackGround(int color) {
        self_dialog_ll_title.setBackgroundColor(color);
    }

    public void setDialogTitleName(String s) {
        self_dialog_tv_title.setText(s);
    }

    public void setDialogTitleGrivity(int grivity) {
        self_dialog_tv_title.setGravity(grivity);
    }

    public void setDialogTitlePadding(int left,int top,int right,int bottom) {
        self_dialog_tv_title.setPadding(left,top,right,bottom);
    }

    public void setDialogTitleColor(int color) {
        self_dialog_tv_title.setTextColor(color);
    }

    public void setDialogTitleTextSize(float textSize) {
        self_dialog_tv_title.setTextSize(textSize);
    }


    //  内容
    public void setDialogDescContent(String s) {
        self_dialog_tv_desc.setText(s);
    }

    public void setDialogDescColor(int color) {
        self_dialog_tv_desc.setTextColor(color);
    }

    public void setDialogDescGrivity(int grivity) {
        self_dialog_tv_title.setGravity(grivity);
    }

    public void setDialogDescPadding(int left,int top,int right,int bottom) {
        self_dialog_tv_title.setPadding(left,top,right,bottom);
    }

    public void setDialogDescTextSize(float textSize) {
        self_dialog_tv_desc.setTextSize(textSize);
    }


    //  取消
    public void setDialogCancelText(String s) {
        self_dialog_cancel.setText(s);
    }

    public void setDialogCancelTextColor(int color) {
        self_dialog_cancel.setTextColor(color);
    }

    public void setDialogCancelTextSize(float textSize) {
        self_dialog_cancel.setTextSize(textSize);
    }


    //  确定
    public void setDialogSureText(String s) {
        self_dialog_sure.setText(s);
    }

    public void setDialogSureTextColor(int color) {
        self_dialog_sure.setTextColor(color);
    }

    public void setDialogSureTextSize(float textSize) {
        self_dialog_sure.setTextSize(textSize);
    }

    //分割线
    public void setDividerLineColor(int color){
        self_dialog_horizontal_line.setBackgroundColor(color);
        self_dialog_divider_vline.setBackgroundColor(color);
    }

    public void setDividerLineSize(int size){
        self_dialog_horizontal_line.setHeight(size);
        self_dialog_divider_vline.setWidth(size);
    }


    public interface OnSelfDialogCancelListener {
        void DialogCancelClickListener();
    }

    public interface OnSelfDialogSureListener {
        void DialogSureClickListener();
    }

}
