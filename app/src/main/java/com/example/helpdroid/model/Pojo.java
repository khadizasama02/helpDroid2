package com.example.helpdroid.model;

public class Pojo {
    private String division;
    private String address;
    private String name;
    private String number;

    public Pojo() {

    }

    public Pojo(String division, String address, String name, String number) {
        this.division = division;
        this.address = address;
        this.name = name;
        this.number = number;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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



}
