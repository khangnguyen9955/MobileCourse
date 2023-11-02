package com.example.mhike.models;

import java.util.Date;

public class Hike {
    private int id;
    private String name;
    private String location;
    private Date date;
    private boolean parkingAvailable;
    private float length;
    private String difficulty;
    private String description;
    private byte[] imageBlob;
    private Date createdAt;
    private Date updatedAt;
    private float rating;

    public Hike(int id, String name, String location,Date date, boolean parkingAvailable, float length, String difficulty, String description, byte[] imageBlob, float rating){
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.parkingAvailable = parkingAvailable;
        this.length = length;
        this.difficulty = difficulty;
        this.description = description;
        this.imageBlob= imageBlob;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.rating = rating;
    }

    public Hike(){}

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
    public byte[] getImageBlob() {
        return imageBlob;
    }
    public void setImageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
    }    public int getId() {
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

    public float getLength() {
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

    public void setLength(float length) {
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
