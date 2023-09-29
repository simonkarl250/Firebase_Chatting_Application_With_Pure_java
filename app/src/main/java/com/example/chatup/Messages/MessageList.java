package com.example.chatup.Messages;

public class MessageList {
    private final String chatKey;
    private final String lastMessage;
    private final String mobile;
    private final String name;
    private final String profilepic;
    private final int unseenMessages;

    public MessageList(String name2, String mobile2, String lastMessage2, String profilepic2, int unseenMessages2, String chatKey2) {
        this.name = name2;
        this.mobile = mobile2;
        this.chatKey = chatKey2;
        this.profilepic = profilepic2;
        this.lastMessage = lastMessage2;
        this.unseenMessages = unseenMessages2;
    }

    public String getName() {
        return this.name;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getLastMessage() {
        return this.lastMessage;
    }

    public String getProfilepic() {
        return this.profilepic;
    }

    public String getChatKey() {
        return this.chatKey;
    }

    public int getUnseenMessages() {
        return this.unseenMessages;
    }
}
