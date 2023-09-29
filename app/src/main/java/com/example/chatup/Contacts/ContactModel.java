package com.example.chatup.Contacts;

public class ContactModel {
    String name,number,Groupname;

    public String getGroupname() {
        return Groupname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setGroupname(String groupName) {
        this.Groupname=groupName;
    }
}
