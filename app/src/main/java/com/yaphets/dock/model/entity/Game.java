package com.yaphets.dock.model.entity;


import com.yaphets.storage.annotation.Id;

import java.sql.Timestamp;

public class Game {
    @Id
    private int id;
    private String name;
    private String description;
    private float version;
    private int category_id;
    private int firm_id;
    private float price;
    private Timestamp issue_date;
    private int purchase_times;
    private float score;

    public Game() {

    }

    public Game(int id, String name, String description, float version, int category_id,
                int firm_id, float price, Timestamp issue_date, int purchase_times, float score) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = version;
        this.category_id = category_id;
        this.firm_id = firm_id;
        this.price = price;
        this.issue_date = issue_date;
        this.purchase_times = purchase_times;
        this.score = score;
    }

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

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getFirm_id() {
        return firm_id;
    }

    public void setFirm_id(int firm_id) {
        this.firm_id = firm_id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Timestamp getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(Timestamp issue_date) {
        this.issue_date = issue_date;
    }

    public int getPurchase_times() {
        return purchase_times;
    }

    public void setPurchase_times(int purchase_times) {
        this.purchase_times = purchase_times;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Game)) {
            return false;
        }
        Game tmp = (Game) obj;
        return this.id == tmp.id;
    }
}
