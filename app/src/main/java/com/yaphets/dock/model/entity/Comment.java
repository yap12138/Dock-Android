package com.yaphets.dock.model.entity;


import com.yaphets.storage.annotation.Id;
import com.yaphets.storage.annotation.ManyToOne;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Comment {
    @Id
    private int user_id;
    @Id
    private int game_id;
    private String content;
    private float score;
    private Timestamp time;

    @ManyToOne(name = "user_id", mappedBy = "uid")
    private User user;

    public Comment() {

    }

    public Comment(int user_id,int game_id,String content,float score) {
        this.user_id = user_id;
        this.game_id = game_id;
        this.content = content;
        this.score = score;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getGame_id() {
        return game_id;
    }

    public String getContent() {
        return content;
    }

    public float getScore() {
        return score;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    /**
     * 登陆玩家的评论记录
     * key = game_id
     * value = record
     */
    public static Map<Integer, Comment> Comment = new HashMap<>();
}
