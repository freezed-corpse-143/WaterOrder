package com.example.qiupys.wateroder.tool;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qiupys.wateroder.R;

//import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/20.
 */

public class AdderView extends LinearLayout implements View.OnClickListener, TextWatcher {


    private int value = 0;
    private int minValue = 0;
    private int maxValue = 100;
    private final TextView tvCount;

    public AdderView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        LayoutInflater.from(context).inflate(R.layout.number_adder, this);

        View view = View.inflate(context, R.layout.number_adder, this);
        ImageView btn_reduce= (ImageView) view.findViewById(R.id.btn_reduce);
        tvCount = (TextView) view.findViewById(R.id.tv_count);
        ImageView btn_add = (ImageView) view.findViewById(R.id.btn_add);
        btn_reduce.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        //设置默认值
        int value = getValue();
        setValue(value);
    }



    /**
     * 如果当前值大于最小值   减
     */
    private void reduce() {
        if (value > minValue) {
            value--;
        }
        setValue(value);
        if (onValueChangeListener != null) {
            onValueChangeListener.onValueChange(value,false);
        }
    }

    /**
     * 如果当前值小于最小值  加
     */
    private void add() {
        if (value < maxValue) {
            value++;
        }
        setValue(value);
        if (onValueChangeListener != null) {
            onValueChangeListener.onValueChange(value,true);
        }
    }

    //获取具体值
    public int getValue() {
        String countStr = tvCount.getText().toString().trim();
        if (countStr != null) {
            value = Integer.valueOf(countStr);
        }
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        tvCount.setText(value + "");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reduce://减
                add();
                break;
            case R.id.btn_add://加
                reduce();
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    //监听回调
    public interface OnValueChangeListener {
        public void onValueChange(int value,boolean added);
    }

    private OnValueChangeListener onValueChangeListener;

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListene) {
        this.onValueChangeListener = onValueChangeListene;
    }
}


