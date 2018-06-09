package com.yaphets.dock.ui.fragment;


import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yaphets.dock.R;
import com.yaphets.dock.database.dao.CommentDAO;
import com.yaphets.dock.database.dao.PurchaseRecordDAO;
import com.yaphets.dock.model.entity.Comment;
import com.yaphets.dock.model.entity.Purchase_Record;
import com.yaphets.dock.model.entity.UserInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyGameFragment extends BaseFragment {


    public MyGameFragment() {
        // Required empty public constructor
    }



    @Override
    protected void loadData() {
        new LoadDataTask().execute();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_my_game, container, false);
        return root;
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
    }
}
