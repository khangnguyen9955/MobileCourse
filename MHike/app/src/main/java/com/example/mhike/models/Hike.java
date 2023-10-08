package com.example.mhike.models;

import java.util.Date;

public class Hike {
    private int id;
    private String name;
    private String location;
    private Date date;
    private boolean parkingAvailable;
    private String length;
    private String difficulty;
    private String description;
    private String imagePath;
    private Date createdAt;
    private Date updatedAt;

    public Hike(){}

    public Hike(int id, String name, String location,Date date, boolean parkingAvailable, String length, String difficulty, String description, String imagePath) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.parkingAvailable = parkingAvailable;
        this.length = length;
        this.difficulty = difficulty;
        this.description = description;
        this.imagePath = imagePath;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty() {
        return difficulty;

    }

    public String getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public boolean getParkingAvailable() {
        return parkingAvailable;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParkingAvailable(boolean parkingAvailable) {
        this.parkingAvailable = parkingAvailable;
    }

    public boolean isParkingAvailable() {
        return parkingAvailable;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
