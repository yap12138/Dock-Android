package com.yaphets.dock.model.entity;


import com.yaphets.storage.annotation.Id;

import java.sql.Timestamp;

public class Purchase_Record {
    @Id
    private int user_id;
    @Id
    private int game_id;
    private Timestamp date;

    public Purchase_Record() {

    }

    public Purchase_Record(int user_id, int game_id, Timestamp date) {
        this.user_id = user_id;
        this.game_id = game_id;
        this.date = date;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getGame_id() {
        return game_id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
