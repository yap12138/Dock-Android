package com.yaphets.dock.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yaphets.dock.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyGameFragment extends BaseFragment {


    public MyGameFragment() {
        // Required empty public constructor
    }



    @Override
    protected void loadData() {

    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_my_game, container, false);
        return root;
    }
}