package com.yaphets.dock.model.entity;


import com.yaphets.storage.annotation.Id;
import com.yaphets.storage.annotation.ManyToOne;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Purchase_Record {
    @Id
    private int user_id;
    @Id
    private int game_id;
    private Timestamp date;

    @ManyToOne(name = "game_id", mappedBy = "id")
    private Game game;

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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * 登陆玩家的购买记录
     * key = game_id
     * value = record
     */
    public static Map<Integer, Purchase_Record> PurchaseRecord = new HashMap<>();
}
