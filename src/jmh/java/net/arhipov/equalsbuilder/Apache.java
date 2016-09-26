package net.arhipov.equalsbuilder;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.Objects;

public class Apache {

    private final String name;
    private final String surname;
    private final int age;

    public Apache(String name, String surname, int age) {
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
        if (o == null) { return false; }
        if (o == this) { return true; }
        if (o.getClass() != getClass()) {
            return false;
        }
        Apache rhs = (Apache) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(name, rhs.name)
                .append(surname, rhs.surname)
                .append(age, rhs.age)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, age);
    }
}
