package com.example.jsondemo;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {

    private int id;
    private String name;
    private String username;
    private int parentId;

    //CONSTRUCTOR
    public UserModel() {

    }

    public UserModel(int id, String username, String name, int parentId) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.parentId = parentId;
    }

    protected UserModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        username = in.readString();
        parentId = in.readInt();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    //GETTERS and SETTERS
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getParentId() {
        return parentId;
    }

    public void setAdmin(boolean admin) {
        this.parentId = parentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(username);
        dest.writeInt(parentId);
    }
}
