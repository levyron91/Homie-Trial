package com.swapping.homie.models;

public class Infomation {
    String about, job, company, city;

    public Infomation(){}

    public Infomation(String about, String job, String company, String school, String city, String gender) {
        this.about = about;
        this.job = job;
        this.company = company;
        this.city = city;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
