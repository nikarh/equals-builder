package net.arhipov.equalsbuilder;


import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;
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

    static class Humanity {
        private final List<Person> people;

        Humanity() {
            this.people = null;
        }

        Humanity(List<Person> people) {
            this.people = people;
        }

        public List<Person> getPeople() {
            return people;
        }
    }

    @Test
    public void compareWithCollections() {
        assertTrue(EqualsBuilder.test(new Humanity(), new Humanity())
                .comparingCollections(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        assertTrue(EqualsBuilder.test(new Humanity(emptyList()), new Humanity(emptyList()))
                .comparingCollections(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        assertTrue(EqualsBuilder.test(new Humanity(emptyList()), new Humanity(emptyList()))
                .comparingCollections(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        assertFalse(EqualsBuilder.test(new Humanity(), new Humanity(emptyList()))
                .comparingCollections(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        assertFalse(EqualsBuilder.test(new Humanity(emptyList()), new Humanity())
                .comparingCollections(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        assertFalse(EqualsBuilder.test(new Humanity(emptyList()), null)
                .comparingCollections(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        Person person1 = new Person(1);
        Person person2 = new Person(1);
        Person person3 = new Person(2);
        Person person4 = new Person(2);

        assertTrue(EqualsBuilder.test(new Humanity(asList(person1, person3)), new Humanity(asList(person1, person3)))
                .comparingCollections(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        assertTrue(EqualsBuilder.test(new Humanity(asList(person1, person3)), new Humanity(asList(person2, person4)))
                .comparingCollections(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        assertFalse(EqualsBuilder.test(new Humanity(asList(person1, person3)), new Humanity(asList(person2)))
                .comparingCollections(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        assertFalse(EqualsBuilder.test(new Humanity(asList(person1, person1)), new Humanity(asList(person2, person4)))
                .comparingCollections(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());
    }

    @Test
    public void compareWithIterables() {
        assertTrue(EqualsBuilder.test(new Humanity(), new Humanity())
                .comparingIterables(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        assertTrue(EqualsBuilder.test(new Humanity(emptyList()), new Humanity(emptyList()))
                .comparingIterables(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        assertTrue(EqualsBuilder.test(new Humanity(emptyList()), new Humanity(emptyList()))
                .comparingIterables(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        assertFalse(EqualsBuilder.test(new Humanity(), new Humanity(emptyList()))
                .comparingIterables(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        assertFalse(EqualsBuilder.test(new Humanity(emptyList()), new Humanity())
                .comparingIterables(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        assertFalse(EqualsBuilder.test(new Humanity(emptyList()), null)
                .comparingIterables(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        Person person1 = new Person(1);
        Person person2 = new Person(1);
        Person person3 = new Person(2);
        Person person4 = new Person(2);

        assertTrue(EqualsBuilder.test(new Humanity(asList(person1, person3)), new Humanity(asList(person1, person3)))
                .comparingIterables(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        assertTrue(EqualsBuilder.test(new Humanity(asList(person1, person3)), new Humanity(asList(person2, person4)))
                .comparingIterables(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        assertFalse(EqualsBuilder.test(new Humanity(asList(person1, person3)), new Humanity(asList(person2)))
                .comparingIterables(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());

        assertFalse(EqualsBuilder.test(new Humanity(asList(person1, person1)), new Humanity(asList(person2, person4)))
                .comparingIterables(Humanity::getPeople, (p1, p2) -> EqualsBuilder.test(p1, p2)
                        .comparing(Person::getId)
                        .areEqual())
                .areEqual());
    }

    static class Index {
        private final Map<Integer, Address> addressMap;

        public Index() {
            this.addressMap = null;
        }

        public Index(List<Address> addresses) {
            this.addressMap = addresses.stream().collect(toMap(Address::getId, identity()));
        }

        public Map<Integer, Address> getAddressMap() {
            return addressMap;
        }
    }

    @Test
    public void compareWithMaps() {
        assertTrue(EqualsBuilder.test(new Index(), new Index())
                .comparingMaps(Index::getAddressMap, (a, b) -> EqualsBuilder.test(a, b)
                        .comparing(Address::getPhone)
                        .areEqual())
                .areEqual());

        assertTrue(EqualsBuilder.test(new Index(emptyList()), new Index(emptyList()))
                .comparingMaps(Index::getAddressMap, (a, b) -> EqualsBuilder.test(a, b)
                        .comparing(Address::getPhone)
                        .areEqual())
                .areEqual());

        assertFalse(EqualsBuilder.test(new Index(), new Index(emptyList()))
                .comparingMaps(Index::getAddressMap, (a, b) -> EqualsBuilder.test(a, b)
                        .comparing(Address::getPhone)
                        .areEqual())
                .areEqual());

        assertFalse(EqualsBuilder.test(new Index(emptyList()), new Index())
                .comparingMaps(Index::getAddressMap, (a, b) -> EqualsBuilder.test(a, b)
                        .comparing(Address::getPhone)
                        .areEqual())
                .areEqual());

        assertFalse(EqualsBuilder.test(new Index(emptyList()), null)
                .comparingMaps(Index::getAddressMap, (a, b) -> EqualsBuilder.test(a, b)
                        .comparing(Address::getPhone)
                        .areEqual())
                .areEqual());

        Address address1 = new Address(1, 123123L, 23.4F, 25.6, "first", "Somewhere");
        Address address2 = new Address(2, 123123L, 23.4F, 25.6, "second", "Somewhere else");
        Address address3 = new Address(1, 123123L, 23.4F, 25.6, "third", "Somewhere");
        Address address4 = new Address(2, 123123L, 23.4F, 25.6, "forth", "Somewhere else");

        assertTrue(EqualsBuilder.test(new Index(asList(address1, address2)), new Index(asList(address1, address2)))
                .comparingMaps(Index::getAddressMap, (a, b) -> EqualsBuilder.test(a, b)
                        .comparing(Address::getPhone)
                        .areEqual())
                .areEqual());

        assertTrue(EqualsBuilder.test(new Index(asList(address1, address2)), new Index(asList(address3, address4)))
                .comparingMaps(Index::getAddressMap, (a, b) -> EqualsBuilder.test(a, b)
                        .comparing(Address::getPhone)
                        .areEqual())
                .areEqual());

        assertFalse(EqualsBuilder.test(new Index(asList(address1)), new Index(emptyList()))
                .comparingMaps(Index::getAddressMap, (a, b) -> EqualsBuilder.test(a, b)
                        .comparing(Address::getPhone)
                        .areEqual())
                .areEqual());

        assertFalse(EqualsBuilder.test(new Index(asList(address1, address2)), new Index(asList(address3, address4)))
                .comparingMaps(Index::getAddressMap, (a, b) -> EqualsBuilder.test(a, b)
                        .comparing(Address::getPhone)
                        .comparing(Address::getStreet)
                        .areEqual())
                .areEqual());
    }

    @Test
    public void compareWithMapsHavingNullKeysAndNullValues() {
        Map<Integer, Integer> map1 = new HashMap<>();
        Map<Integer, Integer> map2 = new HashMap<>();
        map1.put(null, null);
        map1.put(1, null);
        map2.put(null, null);
        map2.put(1, null);

        assertTrue(EqualsBuilder.test(new AtomicReference<>(map1), new AtomicReference<>(map2))
                .comparingMaps(AtomicReference::get, Objects::equals)
                .areEqual());

        map2.replace(null, 1);

        assertFalse(EqualsBuilder.test(new AtomicReference<>(map1), new AtomicReference<>(map2))
                .comparingMaps(AtomicReference::get, Objects::equals)
                .areEqual());

        map1.remove(null);

        assertFalse(EqualsBuilder.test(new AtomicReference<>(map1), new AtomicReference<>(map2))
                .comparingMaps(AtomicReference::get, Objects::equals)
                .areEqual());

        map2.remove(null);
        map2.replace(1, 42);

        assertFalse(EqualsBuilder.test(new AtomicReference<>(map1), new AtomicReference<>(map2))
                .comparingMaps(AtomicReference::get, Objects::equals)
                .areEqual());
    }
}
