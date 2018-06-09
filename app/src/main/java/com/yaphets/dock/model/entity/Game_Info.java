package com.yaphets.dock.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.yaphets.storage.annotation.Id;
import com.yaphets.storage.database.dao.GenericDAO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Game_Info implements Parcelable{
    @Id
    private int id;
    private String cover;
    private String img;

    public Game_Info() {

    }

    public Game_Info(int id, String cover, String img) {
        this.id = id;
        this.cover = cover;
        this.img = img;
    }

    protected Game_Info(Parcel in) {
        id = in.readInt();
        cover = in.readString();
        img = in.readString();
    }

    public static final Creator<Game_Info> CREATOR = new Creator<Game_Info>() {
        @Override
        public Game_Info createFromParcel(Parcel in) {
            return new Game_Info(in);
        }

        @Override
        public Game_Info[] newArray(int size) {
            return new Game_Info[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    private static Map<Integer, Game_Info> mBaseGameInfos = new HashMap<>();

    public static Game_Info createInstance(int permarykey) throws SQLException {
        Game_Info info = mBaseGameInfos.get(permarykey);

        if (info == null) {
            info = new Game_Info();
            info.setId(permarykey);
            info = GenericDAO.find(info);

            mBaseGameInfos.put(permarykey, info);
        }

        return info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(cover);
        dest.writeString(img);
    }
}
