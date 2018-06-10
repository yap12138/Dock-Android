package com.yaphets.dock.database.dao;

import android.util.Log;

import com.yaphets.dock.database.DataAccessObject;
import com.yaphets.dock.model.entity.Game;
import com.yaphets.storage.database.dao.GenericDAO;
import com.yaphets.storage.database.dao.MySqlDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
            //rst = GenericDAO.findAll(Game.class, "id=?", ids[0]).get(0);
            rst = Game.createInstance(ids[0]);
        } catch (SQLException e) {
            Log.e(TAG, "findAll: " + e.getMessage(), e);
        }

        return rst;
    }

    public List<Game> findRankDownload() {
        String sql = "SELECT id FROM game_rank_download";
        List<Integer> rankList = findRank(sql);
        List<Game> rst = new ArrayList<>();
        try {
            Game game = null;
            for (Integer id : rankList) {
                game = Game.createInstance(id);
                rst.add(game);
            }
        } catch (SQLException e) {
            Log.e(TAG, "findRankDownload: " + e.getMessage(), e);
        }
        return rst;
    }

    public List<Game> findRankScore() {
        String sql = "SELECT id FROM game_rank_score";
        List<Integer> rankList = findRank(sql);
        List<Game> rst = new ArrayList<>();
        try {
            Game game = null;
            for (Integer id : rankList) {
                game = Game.createInstance(id);
                rst.add(game);
            }
        } catch (SQLException e) {
            Log.e(TAG, "findRankScore: " + e.getMessage(), e);
        }
        return rst;
    }

    public List<Game> findRankPrice() {
        String sql = "SELECT id FROM game_rank_price";
        List<Integer> rankList = findRank(sql);
        List<Game> rst = new ArrayList<>();
        try {
            Game game = null;
            for (Integer id : rankList) {
                game = Game.createInstance(id);
                rst.add(game);
            }
        } catch (SQLException e) {
            Log.e(TAG, "findRankPrice: " + e.getMessage(), e);
        }
        return rst;
    }

    /**
     * 获取游戏排行，返回按照sql字符串rank排序的id列表
     * @param sql
     * SQL字符串，选择数据库视图
     * @return
     * 排序的游戏id列表
     */
    private List<Integer> findRank(String sql) {
        List<Integer> rst = new ArrayList<>();
        Connection con = null;
        Statement stm = null;

        int id;

        try {
            con = MySqlDAO.getConnection();
            stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                id = rs.getInt(1);
                //Game game = Game.createInstance(id);
                rst.add(id);
            }

        } catch (SQLException e) {
            Log.e(TAG, "isValid: ", e);
        } finally {
            MySqlDAO.release(con, stm);
        }
        return rst;
    }
}
