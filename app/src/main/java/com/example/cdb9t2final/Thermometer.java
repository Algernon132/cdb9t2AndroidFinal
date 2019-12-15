package com.example.cdb9t2final;

public class Thermometer {
    private int id;
    private int temperature;
    private String name;

    public Thermometer(int id, int temperature, String name){
        this.id = id;
        this.temperature = temperature;
        this.name = name;
    }

    public void setTemp(int temperature){
        this.temperature = temperature;
    }

    public int getTemp(){
        return this.temperature;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

}
