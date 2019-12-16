package com.example.jsondemo;

public class CurrentModel {

    private int id;
    private String device;
    private double current;
    private String datetime;

    //CONSTRUCTORS
    public CurrentModel() {

    }

    public CurrentModel(int id, String device, double current, String datetime) {
        this.id = id;
        this.device = device;
        this.current = current;
        this.datetime = datetime;
    }

    //GETTERS and SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
