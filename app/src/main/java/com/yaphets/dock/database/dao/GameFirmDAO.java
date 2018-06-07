package com.yaphets.dock.database.dao;

import android.util.Log;

import com.yaphets.dock.database.DataAccessObject;
import com.yaphets.dock.model.entity.Game_Firm;

import java.sql.SQLException;
import java.util.List;

public class GameFirmDAO implements DataAccessObject<Game_Firm> {
    private static final String TAG = "GameFirmDAO";

    @Override
    public List<Game_Firm> findAll() {
        List<Game_Firm> list = null;
        try {
            list = GenericDAO.findAll(Game_Firm.class);
        } catch (SQLException e) {
            Log.e(TAG, "findAll: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Game_Firm> findByProperty(String where, Object... values) {
        List<Game_Firm> rst = null;
        try {
            rst = GenericDAO.findAll(Game_Firm.class,where,values);
        } catch (SQLException e) {
            Log.e(TAG, "findByProperty: " + e.getMessage(), e);
        }

        return rst;
    }

    @Override
    public Game_Firm findById(int... ids) {
        Game_Firm gf = null;
        try {
            gf = GenericDAO.findAll(Game_Firm.class,"id=?",ids[0]).get(0);
        } catch (SQLException e) {
            Log.e(TAG, "findById: " + e.getMessage(), e);
        }
        return null;
    }
}
