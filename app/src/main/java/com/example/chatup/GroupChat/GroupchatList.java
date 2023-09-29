package com.example.chatup.GroupChat;

public class GroupchatList {
    private String date;
    private String message;
    private String mobile;
    private String name;
    private String time;
    private String myname;

    public GroupchatList(String date, String message, String mobile, String name, String time, String myName) {
        this.date = date;
        this.message = message;
        this.mobile = mobile;
        this.name = name;
        this.time = time;
        this.myname=myName;
    }

    public String getMyname() {
        return myname;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }
}
