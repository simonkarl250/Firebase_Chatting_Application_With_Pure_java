package com.example.chatup.groupMessages;

public class GroupList {
    private final String grouplastMessage;
    private final String groupname;
    private final String groupprofilepic;
    private final int groupunseenMessages;
    private final String mobile;
    private final String myname;



    public GroupList(String grouplastMessage, String groupname, String groupprofilepic, int groupunseenMessages, String mobile, String myname) {
        this.grouplastMessage = grouplastMessage;
        this.groupname = groupname;
        this.groupprofilepic = groupprofilepic;
        this.groupunseenMessages = groupunseenMessages;
        this.mobile = mobile;
        this.myname=myname;
    }

    public String getMyname() {
        return myname;
    }

    public String getMobile() {
        return mobile;
    }

    public String getGrouplastMessage() {
        return grouplastMessage;
    }

    public String getGroupname() {
        return groupname;
    }

    public String getGroupprofilepic() {
        return groupprofilepic;
    }

    public int getGroupunseenMessages() {
        return groupunseenMessages;
    }
}
