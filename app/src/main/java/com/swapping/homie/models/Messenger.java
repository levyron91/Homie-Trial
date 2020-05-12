package com.swapping.homie.models;

public class Messenger {
    private String createBy;
    private String content;
    private String url;

    public Messenger(String createBy, String content, String url) {
        this.createBy = createBy;
        this.content = content;
        this.url = url;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
