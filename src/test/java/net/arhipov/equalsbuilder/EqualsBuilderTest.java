package net.arhipov.equalsbuilder;


import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

public class EqualsBuilderTest {

    interface Identifiable {
        int getId();
    }

    private static class Person implements Identifiable {

        private final int id;

        Person(int id) {
            this.id = id;
        }

        @Override
        public int getId() {
            return id;
        }
    }

    private static class Address implements Identifiable {

        private final int id;
        private final String name;
        private final String address;

        Address(int id, String name, String address) {
            this.id = id;
            this.name = name;
            this.address = address;
        }

        @Override
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Address address1 = (Address) o;
            return id == address1.id &&
                    Objects.equals(name, address1.name) &&
                    Objects.equals(address, address1.address);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, address);
        }
    }

    @Test
    public void comapreSameClass() {
        Address address1 = new Address(1, "first", "Somewhere");
        Address address2 = new Address(1, "first", "Somewhere else");

        assertTrue(EqualsBuilder.test(address1, address2)
                .comparing(Address::getId)
                .areEqual());

        assertTrue(EqualsBuilder.test(address1, address2)
                .comparing(Address::getName)
                .areEqual());

        assertTrue(EqualsBuilder.test(address1, address2)
                .comparing(Address::getId)
                .comparing(Address::getName)
                .areEqual());

        assertFalse(EqualsBuilder.test(address1, address2)
                .comparing(Address::getAddress)
                .areEqual());

        assertFalse(EqualsBuilder.test(address1, address2)
                .comparing(Address::getId)
                .comparing(Address::getName)
                .comparing(Address::getAddress)
                .areEqual());

        assertTrue(EqualsBuilder.test(address1, address2)
                .comparing(Identifiable::getId)
                .areEqual());
    }

    @Test
    public void compareByCommonSuperType() {
        Person person = new Person(1);
        Address address = new Address(1, "Test", "Somewhere");

        assertTrue(EqualsBuilder.test(person, address, Identifiable.class)
                .comparing(Identifiable::getId)
                .areEqual());

        assertFalse(EqualsBuilder.test(person, address)
                .comparing(Identifiable::getId)
                .areEqual());
    }

}
