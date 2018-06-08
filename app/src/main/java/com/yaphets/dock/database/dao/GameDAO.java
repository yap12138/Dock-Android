package com.yaphets.dock.database.dao;

import android.util.Log;

import com.yaphets.dock.database.DataAccessObject;
import com.yaphets.dock.model.entity.Game;
import com.yaphets.storage.database.dao.GenericDAO;

import java.sql.SQLException;
import java.util.List;

public class GameDAO implements DataAccessObject<Game>  {
    private static final String TAG = "GameDAO";


    @Override
    public List<Game> findAll() {
        List<Game> rst = null;
        try {
            rst = GenericDAO.findAll(Game.class);
        } catch (SQLException e) {
            Log.e(TAG, "findAll: " + e.getMessage(), e);
        }

        return rst;
    }

    @Override
    public List<Game> findByProperty(String where, Object... values) {
        List<Game> rst = null;
        try {
            rst = GenericDAO.findAll(Game.class, where, values);
        } catch (SQLException e) {
            Log.e(TAG, "findAll: " + e.getMessage(), e);
        }

        return rst;
    }

    @Override
    public Game findById(int... ids) {
        Game rst = null;
        try {
            rst = GenericDAO.findAll(Game.class, "id=?", ids[0]).get(0);
        } catch (SQLException e) {
            Log.e(TAG, "findAll: " + e.getMessage(), e);
        }

        return rst;
    }
}
