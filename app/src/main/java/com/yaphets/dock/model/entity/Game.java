package com.yaphets.dock.model.entity;


import android.os.Parcel;
import android.os.Parcelable;

import com.yaphets.storage.annotation.Id;
import com.yaphets.storage.annotation.ManyToOne;
import com.yaphets.storage.database.dao.GenericDAO;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Game implements Parcelable {
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

    @ManyToOne(name = "info_id", mappedBy = "id")
    private Game_Info info;

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

    protected Game(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        version = in.readFloat();
        category_id = in.readInt();
        firm_id = in.readInt();
        price = in.readFloat();
        issue_date = (Timestamp) in.readSerializable();
        purchase_times = in.readInt();
        score = in.readFloat();
        info = in.readParcelable(Game_Info.class.getClassLoader());
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

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

    public Game_Info getInfo() {
        return info;
    }

    public void setInfo(Game_Info info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Game)) {
            return false;
        }
        Game tmp = (Game) obj;
        return this.id == tmp.id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeFloat(version);
        dest.writeInt(category_id);
        dest.writeInt(firm_id);
        dest.writeFloat(price);
        dest.writeSerializable(issue_date);
        dest.writeInt(purchase_times);
        dest.writeFloat(score);
        dest.writeParcelable(info, flags);
    }

    private static Map<Integer, Game> mBaseGames = new HashMap<>();

    public static Game createInstance(int permarykey) throws SQLException {
        Game game = mBaseGames.get(permarykey);

        if (game == null) {
            game = new Game();
            game.setId(permarykey);
            game = GenericDAO.find(game);

            mBaseGames.put(permarykey, game);
        }

        return game;
    }
}
