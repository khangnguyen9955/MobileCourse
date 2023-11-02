package com.example.mhike.models;

import java.util.Date;

public class Observation {
    private int id;
    private int hikeId;
    private String name;
    private Date date;
    private String comments;
    private Date createdAt;
    private Date updatedAt;

    public Observation(int id, int hikeId, String name, Date date, String comments) {
        this.id = id;
        this.hikeId = hikeId;
        this.name = name;
        this.date = date;
        this.comments = comments;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Observation(){}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHikeId() {
        return hikeId;
    }

    public void setHikeId(int hikeId) {
        this.hikeId = hikeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
