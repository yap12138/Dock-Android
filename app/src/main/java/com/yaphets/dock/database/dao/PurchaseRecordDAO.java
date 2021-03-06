package com.yaphets.dock.database.dao;

import android.util.Log;

import com.yaphets.dock.database.DataAccessObject;
import com.yaphets.dock.database.DataManipulationObject;
import com.yaphets.dock.model.entity.Purchase_Record;
import com.yaphets.storage.database.dao.GenericDAO;

import java.sql.SQLException;
import java.util.List;

public class PurchaseRecordDAO implements DataAccessObject<Purchase_Record>, DataManipulationObject<Purchase_Record> {
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

    @Override
    public boolean insert(Purchase_Record Transient) {
        int nums = 0;
        try {
            nums = GenericDAO.insert(Transient);
        } catch (SQLException e) {
            Log.e(TAG, "insert: " + e.getMessage(), e);
        }
        return nums > 0;
    }

    @Override
    public boolean delete(Purchase_Record persistent) {
        return false;
    }

    @Override
    public boolean update(Purchase_Record persistent) {
        return false;
    }

    @Override
    public int updateAll(String where, Object... values) {
        return 0;
    }
}
