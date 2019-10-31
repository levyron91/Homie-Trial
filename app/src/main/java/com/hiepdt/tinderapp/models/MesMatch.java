package com.hiepdt.tinderapp.models;

public class MesMatch {
    private String url;
    private String name;
    private String messenger;

    public MesMatch(String url, String name, String messenger) {
        this.url = url;
        this.name = name;
        this.messenger = messenger;
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

    public String getMessenger() {
        return messenger;
    }

    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }
}
