package net.arhipov.equalsbuilder;


import org.junit.Test;

import static org.junit.Assert.*;

public class EqualsBuilderTest {

    interface Identifiable {
        int getHouse();
    }

    private static class Person implements Identifiable {
        private final int house;

        Person(int house) {
            this.house = house;
        }

        public int getHouse() {
            return house;
        }
    }

    private static class Address implements Identifiable {
        private final int house;
        private final long phone;
        private final float latitude;
        private final double longitude;
        private final String street;
        private final String city;

        public Address(int house, long phone, float latitude, double longitude, String street, String city) {
            this.house = house;
            this.phone = phone;
            this.latitude = latitude;
            this.longitude = longitude;
            this.street = street;
            this.city = city;
        }

        @Override
        public int getHouse() {
            return house;
        }

        public long getPhone() {
            return phone;
        }

        public float getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getStreet() {
            return street;
        }

        public String getCity() {
            return city;
        }
    }


    @Test
    public void comapreSameObjects() {
        Address address1 = new Address(1, 123123L, 23.4F, 25.6, "first", "Somewhere");

        assertTrue(EqualsBuilder.test(address1, address1)
                .areEqual());
    }

    @Test
    public void comapreSameClass() {
        Address address1 = new Address(1, 123123L, 23.4F, 25.6, "first", "Somewhere");
        Address address2 = new Address(1, 123123L, 23.4F, 25.6, "first", "Somewhere else");

        assertTrue(EqualsBuilder.test(address1, address2)
                .comparing(Address::getHouse)
                .areEqual());
        assertTrue(EqualsBuilder.test(address1, address2)
                .comparing(Address::getLongitude)
                .areEqual());
        assertTrue(EqualsBuilder.test(address1, address2)
                .comparing(Address::getLatitude)
                .areEqual());
        assertTrue(EqualsBuilder.test(address1, address2)
                .comparing(Address::getPhone)
                .areEqual());

        assertTrue(EqualsBuilder.test(address1, address2)
                .comparing(Address::getStreet)
                .areEqual());

        assertTrue(EqualsBuilder.test(address1, address2)
                .comparing(Address::getHouse)
                .comparing(Address::getStreet)
                .comparing(Address::getLongitude)
                .comparing(Address::getLatitude)
                .comparing(Address::getPhone)
                .areEqual());

        assertFalse(EqualsBuilder.test(address1, address2)
                .comparing(Address::getCity)
                .areEqual());

        assertFalse(EqualsBuilder.test(address1, address2)
                .comparing(Address::getHouse)
                .comparing(Address::getCity)
                .comparing(Address::getStreet)
                .comparing(Address::getLongitude)
                .comparing(Address::getLatitude)
                .comparing(Address::getPhone)
                .areEqual());

        assertTrue(EqualsBuilder.test(address1, address2)
                .comparing(Identifiable::getHouse)
                .areEqual());
    }

    @Test
    public void compareByCommonSuperType() {
        Person person = new Person(1);
        Address address = new Address(1, 123123L, 23.4F, 25.6, "first", "Somewhere");

        assertTrue(EqualsBuilder.test(person, address, Identifiable.class)
                .comparing(Identifiable::getHouse)
                .areEqual());

        assertFalse(EqualsBuilder.test(person, address)
                .comparing(Identifiable::getHouse)
                .areEqual());
    }

    @Test
    public void compareDifferentValues() {
        Address address1 = new Address(1, 123123L, 23.4F, 25.6, "first", "Somewhere");
        Address address2 = new Address(2, 432321L, 24.4F, 26.6, "first", "Somewhere else");

        assertFalse(EqualsBuilder.test(address1, address2)
                .comparing(Address::getLatitude)
                .comparing(Address::getCity)
                .comparing(Address::getLongitude)
                .areEqual());

        assertFalse(EqualsBuilder.test(address1, address2)
                .comparing(Address::getLongitude)
                .comparing(Address::getCity)
                .comparing(Address::getLatitude)
                .areEqual());

        assertFalse(EqualsBuilder.test(address1, address2)
                .comparing(Address::getHouse)
                .comparing(Address::getCity)
                .comparing(Address::getLatitude)
                .areEqual());

        assertFalse(EqualsBuilder.test(address1, address2)
                .comparing(Address::getPhone)
                .comparing(Address::getCity)
                .comparing(Address::getLatitude)
                .areEqual());
    }

}
