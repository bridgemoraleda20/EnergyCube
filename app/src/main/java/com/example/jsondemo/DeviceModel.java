package com.example.jsondemo;

public class DeviceModel {

    private int id;
    private int userId;
    private String name;
    private int roomId;
    private boolean isActive;
    private boolean relay;
    private int imageResource;

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public boolean isRelay() {
        return relay;
    }

    public void setRelay(boolean relay) {
        this.relay = relay;
    }

    public DeviceModel(int id, int userId, String name, int roomId, boolean isActive, boolean relay, int imageResource) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.roomId = roomId;
        this.isActive = isActive;
        this.relay = relay;
        this.imageResource = imageResource;
    }

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

    //methods
    public void changeText(String text) {
        name = text;
    }
}
