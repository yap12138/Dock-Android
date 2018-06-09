package com.yaphets.dock.ui.fragment.game;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yaphets.dock.R;
import com.yaphets.dock.adapter.CommentAdapter;
import com.yaphets.dock.database.dao.CommentDAO;
import com.yaphets.dock.model.entity.Comment;
import com.yaphets.dock.model.entity.Purchase_Record;
import com.yaphets.dock.model.entity.User;
import com.yaphets.dock.model.entity.UserInfo;
import com.yaphets.dock.ui.fragment.BaseFragment;

import java.sql.Timestamp;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameCommentFragment extends BaseFragment {

    private RelativeLayout mEditLayout;
    private ImageView mThumbImg;
    private RatingBar mScoreBar;
    private EditText mCommentEditor;
    private Button mSubmitBtn;

    private RecyclerView mCommentList;
    private List<Comment> mComments;

    public GameCommentFragment() {
        // Required empty public constructor
    }

    public static BaseFragment newInstance(int gameId) {
        BaseFragment fragment = new GameCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("gameId", gameId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void loadData() {
        Bundle bundle = getArguments();
        int gameId = bundle.getInt("gameId");

        Glide.with(mContext).load(UserInfo.getInstance().getThumb()).into(mThumbImg);

        if (Purchase_Record.PurchaseRecord.containsKey(gameId)) {   //购买了此游戏
            if (!Comment.Comment.containsKey(gameId)) { //暂未评论
                mScoreBar.setIsIndicator(false); //设置可变
                mEditLayout.setVisibility(View.VISIBLE);

                mSubmitBtn.setOnClickListener(v -> {
                    int uid = UserInfo.getInstance().getUid();
                    String content = mCommentEditor.getText().toString();
                    float score = mScoreBar.getRating();

                    new CommentTask(uid, gameId, content, score).execute();
                });
            } else {    //展示评论
                mScoreBar.setRating(Comment.Comment.get(gameId).getScore());
            }
        }

        //UI Component init
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        //linearLayoutManager.setSmoothScrollbarEnabled(true);
        //linearLayoutManager.setAutoMeasureEnabled(true);

        mCommentList.setLayoutManager(linearLayoutManager);
        mCommentList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        //mCommentList.setHasFixedSize(true);
        mCommentList.setNestedScrollingEnabled(false);

        new LoadCommentTask().execute(gameId);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_game_comment, container, false);
        mEditLayout = root.findViewById(R.id.layout_comment_edit);
        mThumbImg = root.findViewById(R.id.img_thumb);
        mScoreBar = root.findViewById(R.id.rb_score);
        mCommentEditor = root.findViewById(R.id.et_comment);
        mSubmitBtn = root.findViewById(R.id.btn_comment_submit);
        mCommentList = root.findViewById(R.id.rcv_comment);
        return root;
    }

    class CommentTask extends AsyncTask<Void, Void, Boolean> {

        private int uid;
        private int gameId;
        private String content;
        private float score;
        private Comment comment;

        CommentDAO dao;

        CommentTask(int uid, int gameId, String content, float score) {
            this.uid = uid;
            this.gameId = gameId;
            this.content = content;
            this.score = score;
            this.dao = new CommentDAO();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            comment = new Comment(uid, gameId, content, score);
            comment.setTime(new Timestamp(System.currentTimeMillis()));
            return dao.insert(comment);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(mContext, R.string.toast_success_comment, Toast.LENGTH_SHORT).show();

                mScoreBar.setRating(this.score);
                mScoreBar.setIsIndicator(true); //设置可变
                mEditLayout.setVisibility(View.GONE);

                User user = new User();
                user.setNickname(UserInfo.getInstance().getNickname());
                user.setUid(UserInfo.getInstance().getUid());
                user.setThumb(UserInfo.getInstance().getThumb());
                comment.setUser(user);

                //加入评论list
                mComments.add(0, comment);
                mCommentList.getAdapter().notifyDataSetChanged();
                //加入全局comment list
                Comment.Comment.put(comment.getGame_id(), comment);
            } else {
                Toast.makeText(mContext, R.string.toast_failed_comment, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class LoadCommentTask extends AsyncTask<Integer, Void, List<Comment>> {

        @Override
        protected List<Comment> doInBackground(Integer... values) {
            CommentDAO dao = new CommentDAO();
            List<Comment> list = dao.findByProperty("game_id=?", values[0]);
            return list;
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {
            mComments = comments;
            CommentAdapter adapter = new CommentAdapter(mContext, mComments);
            mCommentList.setAdapter(adapter);
        }
    }
}
