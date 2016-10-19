package net.arhipov.equalsbuilder;

import java.util.List;
import java.util.Map;

public class Human {

    public enum Color {
        RED,
        BLONDE,
        BROWN
    }

    public String name;
    public String surname;
    public int age;
    public long id;
    public Color hairColor;
    public boolean dead;
    public List<String> fovirteWords;
    public Map<String, String> attitude;

    public Human(String name, String surname, int age) {
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Human.Color getHairColor() {
        return this.hairColor;
    }

    public void setHairColor(Human.Color hairColor) {
        this.hairColor = hairColor;
    }

    public boolean isDead() {
        return this.dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public List<String> getFovirteWords() {
        return this.fovirteWords;
    }

    public void setFovirteWords(List<String> fovirteWords) {
        this.fovirteWords = fovirteWords;
    }

    public Map<String, String> getAttitude() {
        return this.attitude;
    }

    public void setAttitude(Map<String, String> attitude) {
        this.attitude = attitude;
    }

}
