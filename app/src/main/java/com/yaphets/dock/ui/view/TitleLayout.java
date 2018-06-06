package com.yaphets.dock.ui.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yaphets.dock.R;


public class TitleLayout extends LinearLayout {
    private TextView _title_tv;
    private ImageButton _back_btn;
    private Button _save_btn;

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.titlebar, this);

        _title_tv = findViewById(R.id.title_tv_text);
        _back_btn = findViewById(R.id.title_btn_back);
        _save_btn = findViewById(R.id.title_btn_save);

        _back_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).finish();
            }
        });
    }

    public void setTitle(String title) {
        _title_tv.setText(title);
    }

    public void setSaveCallBack(OnClickListener listener) {
        _save_btn.setOnClickListener(listener);
    }

    public void enableSaveBtn(boolean enable) {
        _save_btn.setEnabled(enable);
    }
}
