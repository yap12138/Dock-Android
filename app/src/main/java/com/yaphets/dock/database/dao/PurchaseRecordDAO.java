package com.yaphets.dock.database.dao;

import android.util.Log;

import com.yaphets.dock.database.DataAccessObject;
import com.yaphets.dock.model.entity.Purchase_Record;

import java.sql.SQLException;
import java.util.List;

public class PurchaseRecordDAO implements DataAccessObject<Purchase_Record>{
    private static final String TAG = "PurchaseRecordDAO";

    @Override
    public List<Purchase_Record> findAll() {
        List<Purchase_Record> rst = null;
        try {
            rst = GenericDAO.findAll(Purchase_Record.class);
        } catch (SQLException e) {
            Log.e(TAG, "findAll: " + e.getMessage(), e);
        }
        return rst;
    }

    @Override
    public List<Purchase_Record> findByProperty(String where, Object... values) {
        List<Purchase_Record> rst = null;
        try {
            rst = GenericDAO.findAll(Purchase_Record.class,where,values);
        } catch (SQLException e) {
            Log.e(TAG, "findByProperty: " + e.getMessage(), e);
        }
        return rst;
    }

    @Override
    public Purchase_Record findById(int... ids) {
        Purchase_Record pr = null;
        try {
            pr = GenericDAO.findAll(Purchase_Record.class,"user_id=? and game_id=?",ids[0],ids[1]).get(0);
        } catch (SQLException e) {
            Log.e(TAG, "findById: " + e.getMessage(), e);
        }
        return pr;
    }
}
