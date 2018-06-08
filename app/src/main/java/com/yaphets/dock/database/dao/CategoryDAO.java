package com.yaphets.dock.database.dao;

import android.util.Log;

import com.yaphets.dock.database.DataAccessObject;
import com.yaphets.dock.model.entity.Category;
import com.yaphets.storage.database.dao.GenericDAO;

import java.sql.SQLException;
import java.util.List;

public class CategoryDAO implements DataAccessObject<Category> {
    private static final String TAG = "CategoryDAO";

    @Override
    public List<Category> findAll() {
        /*Connection con = null;
        Statement state = null;
        List<Category> rst = new ArrayList<>();
        try {
            con = MySqlDAO.getConnection();
            String sql = "SELECT * FROM category";
            state = con.createStatement();
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String fullName = rs.getString("full_name");
                String chineseName = rs.getString("chinese_name");
                Category ctg = new Category(id, name, fullName, chineseName);
                rst.add(ctg);
            }
        } catch (SQLException e) {
            Log.e(TAG, "findAll: " + e.getMessage(), e);
        } finally {
            MySqlDAO.release(con, state);
        }*/
        List<Category> rst = null;
        try {
            rst = GenericDAO.findAll(Category.class);
        } catch (SQLException e) {
            Log.e(TAG, "findAll: " + e.getMessage(), e);
        }

        return rst;
    }

    @Override
    public List<Category> findByProperty(String where, Object... values) {
        List<Category> rst = null;
        try {
            rst = GenericDAO.findAll(Category.class, where, values);
        } catch (SQLException e) {
            Log.e(TAG, "findAll: " + e.getMessage(), e);
        }

        return rst;
    }


    @Override
    public Category findById(int... ids) {
        Category ctg = null;
        try {
            ctg = GenericDAO.findAll(Category.class, "id=?", ids[0]).get(0);
        } catch (SQLException e) {
            Log.e(TAG, "findById: " + e.getMessage(), e);
        }
        return ctg;
    }
}
