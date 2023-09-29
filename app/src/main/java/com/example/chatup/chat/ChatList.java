package com.example.chatup.chat;

public class ChatList {
    private String date;
    private String message;
    private String mobile;
    private String name;
    private String time;

    public ChatList(String mobile2, String name2, String message2, String date2, String time2) {
        this.mobile = mobile2;
        this.name = name2;
        this.message = message2;
        this.date = date2;
        this.time = time2;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getName() {
        return this.name;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDate() {
        return this.date;
    }

    public String getTime() {
        return this.time;
    }
}
