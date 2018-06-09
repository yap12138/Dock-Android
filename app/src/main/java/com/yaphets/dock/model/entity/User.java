package com.yaphets.dock.model.entity;

import com.yaphets.storage.annotation.Id;
import com.yaphets.storage.database.dao.GenericDAO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class User {
    @Id
    private int uid;

    private String nickname;

    private byte[] thumb;

    public User() {

    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public byte[] getThumb() {
        return thumb;
    }

    public void setThumb(byte[] thumb) {
        this.thumb = thumb;
    }

    private static Map<Integer, User> mBaseUsers = new HashMap<>();

    public static User createInstance(int permarykey) throws SQLException {
        User user = mBaseUsers.get(permarykey);

        if (user == null) {
            user = new User();
            user.setUid(permarykey);
            user = GenericDAO.find(user);

            mBaseUsers.put(permarykey, user);
        }

        return user;
    }
}
