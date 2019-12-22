package com.example.jsondemo;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceModel implements Parcelable {

    private int id;
    private int userId;
    private String name;
    private int roomId;
    private boolean isActive;
    private boolean relay;
    private String start_time;
    private String end_time;

    private int imageResource;

    public DeviceModel(int id, int userId, String name, int roomId, boolean isActive, boolean relay, int imageResource, String start_time, String end_time) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.roomId = roomId;
        this.isActive = isActive;
        this.relay = relay;
        this.imageResource = imageResource;
        this.start_time = start_time;
        this.end_time = end_time;

    }

    public DeviceModel(Parcel in) {
        id=in.readInt();
        userId=in.readInt();
        name=in.readString();
        roomId=in.readInt();
        isActive=in.readInt() == 1;
        relay= in.readInt() ==1;
        start_time = in.readString();
        end_time = in.readString();
    }

    public static final Creator<DeviceModel> CREATOR = new Creator<DeviceModel>() {
        @Override
        public DeviceModel createFromParcel(Parcel in) {
            return new DeviceModel(in);
        }

        @Override
        public DeviceModel[] newArray(int size) {
            return new DeviceModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public boolean isRelay() {
        return relay;
    }

    public void setRelay(boolean relay) {
        this.relay = relay;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    //methods
    public void changeText(String text) {
        name = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeString(name);
        dest.writeInt(roomId);
        dest.writeInt(isActive ? 1 : 0);
        dest.writeInt(relay ? 1 : 0);
        dest.writeString(start_time);
        dest.writeString(end_time);
    }
}
