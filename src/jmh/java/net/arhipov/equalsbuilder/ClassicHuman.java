package net.arhipov.equalsbuilder;

import java.util.Objects;

public class ClassicHuman {

    private final String name;
    private final String surname;
    private final int age;

    public ClassicHuman(String name, String surname, int age) {
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassicHuman that = (ClassicHuman) o;

        if (age != that.age) return false;
        if (!name.equals(that.name)) return false;
        return surname.equals(that.surname);

    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, age);
    }
}
