package net.arhipov.equalsbuilder;


import org.junit.Test;

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
        private final int house;
        private final long phone;
        private final float latitude;
        private final double longitude;
        private final String street;
        private final String city;

        Address(int house, long phone, float latitude, double longitude, String street, String city) {
            this.house = house;
            this.phone = phone;
            this.latitude = latitude;
            this.longitude = longitude;
            this.street = street;
            this.city = city;
        }

        @Override
        public int getId() {
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
    public void compareSameObjects() {
        Address address1 = new Address(1, 123123L, 23.4F, 25.6, "first", "Somewhere");

        assertTrue(EqualsBuilder.test(address1, address1)
                .areEqual());
    }

    @Test
    public void compareSameClass() {
        Address address1 = new Address(1, 123123L, 23.4F, 25.6, "first", "Somewhere");
        Address address2 = new Address(1, 123123L, 23.4F, 25.6, "first", "Somewhere else");

        assertTrue(EqualsBuilder.test(address1, address2)
                .comparing(Address::getId)
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
                .comparing(Address::getId)
                .comparing(Address::getStreet)
                .comparing(Address::getLongitude)
                .comparing(Address::getLatitude)
                .comparing(Address::getPhone)
                .areEqual());

        assertFalse(EqualsBuilder.test(address1, address2)
                .comparing(Address::getCity)
                .areEqual());

        assertFalse(EqualsBuilder.test(address1, address2)
                .comparing(Address::getId)
                .comparing(Address::getCity)
                .comparing(Address::getStreet)
                .comparing(Address::getLongitude)
                .comparing(Address::getLatitude)
                .comparing(Address::getPhone)
                .areEqual());

        assertTrue(EqualsBuilder.test(address1, address2)
                .comparing(Identifiable::getId)
                .areEqual());
    }


    @Test
    public void nullChecks() {
        Address address = new Address(1, 123123L, 23.4F, 25.6, "first", "Somewhere");

        assertTrue(EqualsBuilder.test((Address) null, null)
                .comparing(Address::getCity)
                .comparing(Address::getId)
                .areEqual());

        assertTrue(EqualsBuilder.test(null, null, Address.class)
                .comparing(Address::getCity)
                .comparing(Address::getId)
                .areEqual());

        assertFalse(EqualsBuilder.test(address, null)
                .comparing(Address::getCity)
                .comparing(Address::getId)
                .areEqual());

        assertFalse(EqualsBuilder.test((Address) null, address)
                .comparing(Address::getCity)
                .comparing(Address::getId)
                .areEqual());

        assertFalse(EqualsBuilder.test(null, address, Address.class)
                .comparing(Address::getCity)
                .comparing(Address::getId)
                .areEqual());

        assertFalse(EqualsBuilder.test(address, null, Identifiable.class)
                .comparing(Identifiable::getId)
                .areEqual());
    }

    @Test
    public void compareByCommonSuperType() {
        Person person = new Person(1);
        Address address = new Address(1, 123123L, 23.4F, 25.6, "first", "Somewhere");

        assertTrue(EqualsBuilder.test(person, address, Identifiable.class)
                .comparing(Identifiable::getId)
                .areEqual());

        assertFalse(EqualsBuilder.test(person, address)
                .comparing(Identifiable::getId)
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
                .comparing(Address::getId)
                .comparing(Address::getCity)
                .comparing(Address::getLatitude)
                .areEqual());

        assertFalse(EqualsBuilder.test(address1, address2)
                .comparing(Address::getPhone)
                .comparing(Address::getCity)
                .comparing(Address::getLatitude)
                .areEqual());
    }

    private static class Registry {
        private final Address address;
        private final Person person;

        Registry(Address address, Person person) {
            this.address = address;
            this.person = person;
        }

        public Address getAddress() {
            return address;
        }

        public Person getPerson() {
            return person;
        }
    }

    @Test
    public void deepCompare() {
        Address address1 = new Address(1, 123123L, 23.4F, 25.6, "first", "Somewhere");
        Address address2 = new Address(2, 123123L, 23.4F, 25.6, "first", "Somewhere else");
        Person person1 = new Person(1);
        Person person2 = new Person(1);

        Registry registry1 = new Registry(address1, person1);
        Registry registry2 = new Registry(address2, person2);

        assertTrue(EqualsBuilder.test(registry1, registry2)
                .comparing(Registry::getAddress, (a, b) -> EqualsBuilder.test(a, b)
                        .comparing(Address::getPhone)
                        .comparing(Address::getLatitude)
                        .comparing(Address::getLongitude)
                        .comparing(Address::getStreet)
                        .areEqual())
                .comparing(Registry::getPerson, (a, b) -> EqualsBuilder.test(a, b)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        assertFalse(EqualsBuilder.test(registry1, registry2)
                .comparing(Registry::getPerson, (a, b) -> EqualsBuilder.test(a, b)
                        .comparing(Person::getId)
                        .areEqual())
                .comparing(Registry::getAddress, (a, b) -> EqualsBuilder.test(a, b)
                        .comparing(Address::getPhone)
                        .comparing(Address::getLatitude)
                        .comparing(Address::getLongitude)
                        .comparing(Address::getStreet)
                        .comparing(Address::getCity)
                        .areEqual())
                .areEqual());

    }

}
