package com.yaphets.dock.ui.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yaphets.dock.DockApplication;
import com.yaphets.dock.R;
import com.yaphets.dock.adapter.MyGameAdapter;
import com.yaphets.dock.database.dao.CommentDAO;
import com.yaphets.dock.database.dao.PurchaseRecordDAO;
import com.yaphets.dock.model.entity.Comment;
import com.yaphets.dock.model.entity.Game;
import com.yaphets.dock.model.entity.Purchase_Record;
import com.yaphets.dock.model.entity.UserInfo;
import com.yaphets.dock.util.NotifyTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyGameFragment extends BaseFragment implements NotifyTask {

    private TextView mGuideText;
    private RecyclerView mMyGameList;
    private ProgressBar mProgressView;

    private MyGameAdapter mMyGameAdapter;

    public MyGameFragment() {
        // Required empty public constructor
    }

    @Override
    protected void loadData() {
        showProgress(true);
        //加载我的游戏和评论
        new LoadDataTask().execute();

        //game list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mMyGameList.setLayoutManager(linearLayoutManager);
        mMyGameList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        List<Game> tmpList = new ArrayList<>(0);
        mMyGameAdapter = new MyGameAdapter(mContext, tmpList);
        mMyGameList.setAdapter(mMyGameAdapter);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_my_game, container, false);
        mGuideText = root.findViewById(R.id.tv_guide);
        mMyGameList = root.findViewById(R.id.rcv_my_game);
        mProgressView = root.findViewById(R.id.loading_progress);
        return root;
    }

    @Override
    public void notifyDataChange() {
        showProgress(false);
        mGuideText.setText(String.format(Locale.CHINA, "%s,您目前拥有%d款游戏，其中评论了%d款哦", UserInfo.getInstance().getNickname(),
                Purchase_Record.PurchaseRecord.size(), Comment.Comment.size()));
        List<Game> games = new ArrayList<>();
        for (Purchase_Record record : Purchase_Record.PurchaseRecord.values()) {
            games.add(record.getGame());
        }
        mMyGameAdapter.replaceDataSet(games);
    }

    private class LoadDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            String uid = String.valueOf(UserInfo.getInstance().getUid());
            PurchaseRecordDAO purchaseRecordDAO = new PurchaseRecordDAO();
            CommentDAO commentDAO = new CommentDAO();
            List<Purchase_Record> purchaseList = purchaseRecordDAO.findByProperty("user_id LIKE ? and game_id LIKE ?",
                    uid, "%");
            List<Comment> commentList = commentDAO.findByProperty("user_id LIKE ? and game_id LIKE ?",
                    uid, "%");

            //把玩家有的游戏加载回来
            Purchase_Record.PurchaseRecord.putAll(purchaseList.stream().collect(Collectors.toMap(Purchase_Record::getGame_id, a->a)));
            //把玩家所有的评论加载回来
            Comment.Comment.putAll(commentList.stream().collect(Collectors.toMap(Comment::getGame_id, a->a)));
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            notifyDataChange();
            //output interface
            DockApplication.setNotifyTask(MyGameFragment.this);
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
