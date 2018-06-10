package com.yaphets.dock.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yaphets.dock.DockApplication;
import com.yaphets.dock.R;
import com.yaphets.dock.database.dao.PurchaseRecordDAO;
import com.yaphets.dock.model.entity.Category;
import com.yaphets.dock.model.entity.Game;
import com.yaphets.dock.model.entity.Game_Firm;
import com.yaphets.dock.model.entity.Purchase_Record;
import com.yaphets.dock.model.entity.UserInfo;
import com.yaphets.dock.ui.fragment.BaseFragment;
import com.yaphets.dock.ui.fragment.game.GameCommentFragment;
import com.yaphets.dock.ui.fragment.game.GameDetailFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GameDetailActivity extends AppCompatActivity {

    private FloatingActionButton mActionBtn;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ProgressBar mProgressView;

    private Game mGame;
    private boolean mIsOwn = false;

    private List<BaseFragment> mFragments = new ArrayList<>();

    //game detail ui
    private ImageView mCover;
    private TextView mName;
    private TextView mFirm;
    private TextView mCategory;
    private RatingBar mScore;
    private TextView mPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        //load game
        mGame = getIntent().getParcelableExtra("game");


        initView();
        initData();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressView = findViewById(R.id.loading_progress);

        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(mGame.getName());

        mActionBtn = findViewById(R.id.fab);
        mActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 购买逻辑
                if (!mIsOwn) {
                    //显示progress动画
                    showProgress(true);
                    new PurchaseTask().execute();
                } else {
                    Snackbar.make(view, "开始下载", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        //game detail ui
        mCover = findViewById(R.id.img_game_cover);
        mName = findViewById(R.id.tv_game_name);
        mFirm = findViewById(R.id.tv_game_firm);
        mCategory = findViewById(R.id.tv_game_category);
        mScore = findViewById(R.id.rb_game_score);
        mPrice = findViewById(R.id.tv_game_price);
    }

    private void initData() {
        //装载fragment
        mFragments.add(GameDetailFragment.newInstance(mGame));
        mFragments.add(GameCommentFragment.newInstance(mGame.getId()));

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        //game detail ui
        if (mGame.getInfo() != null && mGame.getInfo().getCover() != null) {
            Glide.with(this).load(mGame.getInfo().getCover()).into(mCover);
        }
        mName.setText(mGame.getName());
        mFirm.setText(Game_Firm.GameFirm.get(mGame.getFirm_id()).getName());
        mCategory.setText(Category.Categories.get(mGame.getCategory_id()).getChineseName());
        mScore.setRating(mGame.getScore());
        mPrice.setText(String.format(Locale.CHINA, "%4.1f",mGame.getPrice()));

        if (Purchase_Record.PurchaseRecord.containsKey(mGame.getId())) {
            mIsOwn = true;
            mActionBtn.setImageResource(R.drawable.game_download);
            mActionBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ufoGreen, null)));
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        String[] titles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            titles = getResources().getStringArray(R.array.title_tab_game_detail);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    class PurchaseTask extends AsyncTask<Void, Void, Boolean> {

        private Purchase_Record record;

        @Override
        protected Boolean doInBackground(Void... voids) {
            PurchaseRecordDAO purchaseRecordDAO = new PurchaseRecordDAO();
            record = new Purchase_Record();
            record.setUser_id(UserInfo.getInstance().getUid());
            record.setGame_id(mGame.getId());
            return purchaseRecordDAO.insert(record);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            showProgress(false);
            if (success) {
                Snackbar.make(mActionBtn, "购买成功", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                record.setGame(mGame);
                //保存起来
                Purchase_Record.PurchaseRecord.put(record.getGame_id(), record);
                //update my game fragment
                DockApplication.notifyDataChange();
                //FAB
                mIsOwn = true;
                mActionBtn.setImageResource(R.drawable.game_download);
                mActionBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ufoGreen, null)));
            } else {
                Snackbar.make(mActionBtn, "购买失败", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}
