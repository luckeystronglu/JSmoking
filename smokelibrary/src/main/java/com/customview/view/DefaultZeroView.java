package com.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.libraryutils.ToastUtils;
import com.yzh.luckeylibrary.R;


/**
 * Created by Administrator on 2017/09/05.
 */

public class DefaultZeroView extends LinearLayout {
    private int defaultNum = 0;

    TextView tv_start_zero_name;
    LinearLayout ll_amount_minus,ll_amount_plus;
    EditText et_input_from_zero;

    public DefaultZeroView(Context context) {
        this(context,null);
    }

    public DefaultZeroView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }


    public DefaultZeroView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs,defStyleAttr);
        initView(context, attrs,defStyleAttr);

    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_edit_zero, this);
        tv_start_zero_name = (TextView) view.findViewById(R.id.tv_start_zero_name);
        ll_amount_minus = (LinearLayout) view.findViewById(R.id.ll_amount_minus);
        ll_amount_plus = (LinearLayout) view.findViewById(R.id.ll_amount_plus);
        et_input_from_zero = (EditText) view.findViewById(R.id.et_input_from_zero);

        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.DefaultZeroView);
        String title = array.getString(R.styleable.DefaultZeroView_defaultZeroViewTitle);
//        boolean editable = array.getBoolean(R.styleable.DefaultZeroView_DefaultZeroViewEditable, true);

        array.recycle();
        tv_start_zero_name.setText(title);
        et_input_from_zero.setText(String.valueOf(defaultNum));
        et_input_from_zero.setSelection(1);

        initEvent();
    }

    private void initEvent() {
        //减
        ll_amount_minus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (defaultNum > 0) {
                    defaultNum -= 1;
                    et_input_from_zero.setText(String.valueOf(defaultNum));
                    et_input_from_zero.setSelection(et_input_from_zero.getText().length());
                }else {
                    ToastUtils.showTextToast(getContext(),"最小值不能小于0");
                }
            }
        });

        //加
        ll_amount_plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultNum += 1;
                et_input_from_zero.setText(String.valueOf(defaultNum));
                et_input_from_zero.setSelection(et_input_from_zero.getText().length());
            }
        });

        et_input_from_zero.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    defaultNum = Integer.parseInt(s.toString());
                }
                if (s.toString().equals("00")) {
                    defaultNum = 0;
                    et_input_from_zero.setText(String.valueOf(defaultNum));
                    et_input_from_zero.setSelection(1);
                }
                if (s.toString().length() == 2 && s.toString().startsWith("0")) {
                    defaultNum = Integer.parseInt(s.toString().substring(1));
                    et_input_from_zero.setText(String.valueOf(defaultNum));
                    et_input_from_zero.setSelection(et_input_from_zero.getText().length());
                }
            }
        });
    }


    public void setTitleName(String s) {
        tv_start_zero_name.setText(s);
    }


    public String getCurrentValue() {
        return et_input_from_zero.getText().toString();
    }


}
