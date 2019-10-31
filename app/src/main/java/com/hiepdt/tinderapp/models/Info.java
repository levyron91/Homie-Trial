package com.hiepdt.tinderapp.models;

public class Info {
    String uid;
    long age;
    String name;
    String school;
    String url;

    public Info(String uid, long age, String name, String school, String url) {
        this.uid = uid;
        this.age = age;
        this.name = name;
        this.school = school;
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
