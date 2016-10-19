package net.arhipov.equalsbuilder.dispatch;

public class Human {
    public String name;
    public String surname;
    public String middleName;

    public Human(String name, String surname, String middleName) {
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getMiddleName() {
        return this.middleName;
    }
}
