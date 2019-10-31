package com.hiepdt.tinderapp.models;

public class NewMatch {
    private String chatId;
    private String url;
    private String name;


    public NewMatch(String chatId, String url, String name) {
        this.chatId = chatId;
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
