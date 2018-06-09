package com.yaphets.dock.model.entity;


import com.yaphets.storage.annotation.Id;

import java.util.HashMap;
import java.util.Map;

public class Game_Firm {
    @Id
    private int id;
    private String name;
    private String description;

    public Game_Firm() {

    }

    public Game_Firm(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static Map<Integer, Game_Firm> GameFirm = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
