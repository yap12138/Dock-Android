package com.yaphets.dock.model.entity;

import com.yaphets.dock.model.annotation.Id;

public class Comment {
    @Id
    private int user_id;
    @Id
    private int game_id;
    private String content;
    private float score;

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
}
