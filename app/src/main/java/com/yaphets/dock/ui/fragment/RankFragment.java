package com.yaphets.dock.ui.fragment;


import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.yaphets.dock.R;
import com.yaphets.dock.adapter.RankAdapter;
import com.yaphets.dock.database.dao.GameDAO;
import com.yaphets.dock.model.entity.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RankFragment extends BaseFragment {

    private RadioGroup mRankGroup;
    private RecyclerView mRankList;
    private RankAdapter mRankAdapter;

    private List<Game> mDownloadRankList;
    private List<Game> mScoreRankList;
    private List<Game> mPriceRankList;

    public RankFragment() {
        // Required empty public constructor
    }

    @Override
    protected void loadData() {
        mRankGroup.setOnCheckedChangeListener((group, checkedId) -> {
            setRankDataSet(checkedId);
            mRankList.scrollToPosition(0);
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRankList.setLayoutManager(linearLayoutManager);
        mRankList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        List<Game> tmpList = new ArrayList<>(0);
        mRankAdapter = new RankAdapter(mContext, tmpList);
        mRankList.setAdapter(mRankAdapter);

        new LoadRankTask().execute();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_rank, container, false);
        mRankGroup = root.findViewById(R.id.rg_rank);
        mRankList = root.findViewById(R.id.rcv_rank);
        return root;
    }

    private class LoadRankTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... values) {
            GameDAO dao = new GameDAO();
            mDownloadRankList = dao.findRankDownload();
            mScoreRankList = dao.findRankScore();
            mPriceRankList = dao.findRankPrice();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            int checkedId = mRankGroup.getCheckedRadioButtonId();
            setRankDataSet(checkedId);
        }
    }

    private void setRankDataSet(int checkedId) {
        switch (checkedId) {
            case R.id.rbtn_download_rank:
                mRankAdapter.changeDataSet(mDownloadRankList);
                break;
            case R.id.rbtn_score_rank:
                mRankAdapter.changeDataSet(mScoreRankList);
                break;
            case R.id.rbtn_price_rank:
                mRankAdapter.changeDataSet(mPriceRankList);
                break;
        }
    }
}
