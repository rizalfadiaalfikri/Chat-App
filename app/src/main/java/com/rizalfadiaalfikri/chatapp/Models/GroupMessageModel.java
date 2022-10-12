package com.rizalfadiaalfikri.chatapp.Models;

public class GroupMessageModel {
    private String date, from, message, name, time;

    public GroupMessageModel() {
    }

    public GroupMessageModel(String date, String from,String message, String name, String time) {
        this.date = date;
        this.from = from;
        this.message = message;
        this.name = name;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
