package com.yaphets.dock.ui.fragment.game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yaphets.dock.R;
import com.yaphets.dock.model.entity.Game;
import com.yaphets.dock.ui.fragment.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameDetailFragment extends BaseFragment {

    private Game mGame;

    private ImageView mGameImage;
    private TextView mGameDescription;
    private TextView mGameIssue;

    public GameDetailFragment() {
        // Required empty public constructor
    }

    public static BaseFragment newInstance(Game game) {
        BaseFragment fragment = new GameDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("game", game);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void loadData() {
        Bundle bundle = getArguments();
        mGame = bundle.getParcelable("game");
        if (mGame.getInfo() != null && mGame.getInfo().getImg() != null) {
            Glide.with(mContext).load(mGame.getInfo().getImg()).into(mGameImage);
        }
        if (mGame.getDescription() != null) {
            mGameDescription.setText(mGame.getDescription());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        mGameIssue.setText(sdf.format(mGame.getIssue_date()));
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_game_detail, container, false);
        mGameImage = root.findViewById(R.id.img_game_image);
        mGameDescription = root.findViewById(R.id.tv_game_description);
        mGameIssue = root.findViewById(R.id.tv_game_issue);
        return root;
    }

}
