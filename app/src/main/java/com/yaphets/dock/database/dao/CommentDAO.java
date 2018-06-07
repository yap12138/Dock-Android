package com.yaphets.dock.database.dao;

import android.util.Log;

import com.yaphets.dock.database.DataAccessObject;
import com.yaphets.dock.model.entity.Comment;

import java.sql.SQLException;
import java.util.List;

public class CommentDAO implements DataAccessObject<Comment> {
    private static final String TAG = "CommentDAO";

    @Override
    public List<Comment> findAll() {
        List<Comment> rst = null;
        try {
            rst = GenericDAO.findAll(Comment.class);
        } catch (SQLException e) {
            Log.e(TAG, "findAll: " + e.getMessage(), e);
        }
        return rst;
    }

    @Override
    public List<Comment> findByProperty(String where, Object... values) {
        List<Comment> rst = null;
        try {
            rst = GenericDAO.findAll(Comment.class,where,values);
        } catch (SQLException e) {
            Log.e(TAG, "findByProperty: " + e.getMessage(), e);
        }
        return rst;
    }

    @Override
    public Comment findById(int... ids) {
        Comment cmt = null;
        try {
            cmt = GenericDAO.findAll(Comment.class,"user_id=? and game_id=?",ids[0],ids[1]).get(0);
        } catch (SQLException e) {
            Log.e(TAG, "findById: " + e.getMessage(), e);
        }
        return cmt;
    }
}
