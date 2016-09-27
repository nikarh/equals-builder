equals-builder: fluent builder API for you .equals() methods
============================================================

[![Build Status](https://img.shields.io/travis/nikarh/equals-builder.svg)](https://travis-ci.org/nikarh/equals-builder)
[![Code Coverage](https://img.shields.io/codecov/c/github/nikarh/equals-builder.svg)](https://codecov.io/gh/nikarh/equals-builder)

What is equals-builder?
-----------------------
Compare objects in Java using the following DSL:
```java
@Override
public boolean equals(Object o) {
    return EqualsBuilder.test(this, o)
            .comparing(Human::getName)
            .comparing(Human::getSurname)
            .comparing(Human::getAge)
            .areEqual();
}
```

Motivation
----------

In Java there are three methods inherited from `Object` class that are often overloaded:
- `java boolean equals(Object o)`
- `int hashCode()`
- `String toString()`

The most simple implementation for `toString`, generated by IDE might look as follows:
```java
@Override
public String toString() {
    return "Address{" +
            "house=" + house +
            ", street='" + street + '\'' +
            ", city='" + city + '\'' +
            '}';
}
```
This code is not exactly what is considered 'pretty' by most developers.
More appealing implementation, using a helper from [guava](https://github.com/google/guava) 
would look like:
```java
@Override
public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("house", house)
            .add("street", street)
            .add("city", city)
            .toString();
}
```
With better readability and maintainability, it is usually
favored by the developers more.

For `.hashCode()` overloads in Java 7 `Objects.hash(Object...)` method was introduced, 
which allowed developers to write the following code:
```java
@Override
public int hashCode() {
    return Objects.hash(house, street, city);
}
```

At the same time `Objects.equals(a, b)` was also introduced which 
made .equals overloads somewhat more readable.
```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Address that = (Address) o;
    return Objects.equals(house, that.house) &&
            Objects.equals(street, that.street) &&
            Objects.equals(city, that.city);
}
```

Still, this implementation has some nasty boilerplate code like
null checks, type checks, and type casts.

As an alternative one might consider using `EqualsBuilder` class from [Apache Commons](https://commons.apache.org/)
library:
```java
@Override
public boolean equals(Object o) {
    if (o == null) { return false; }
    if (o == this) { return true; }
    if (o.getClass() != getClass()) {
        return false;
    }
    Address rhs = (Address) o;
    return new EqualsBuilder()
            .appendSuper(super.equals(o))
            .append(house, rhs.house)
            .append(street, rhs.street)
            .append(city, rhs.city)
            .isEquals();
}
```
Unfortunately, not only does this implementation not help with casts and type checks,
but it also takes more space on your screen and attempts to fix English language
by introducing method named "isEquals".

To compliment already existing helpers for overloading `.hashCode` and `.toString`,
an attempt was made to create a DSL for comparing two Java objects.

Goals
-----
The goals for this project are simple:
- Make simple yet good DSL for comparing objects by multiple fields
- Reduce boilerplate required to use this DSL to zero
- Reduce field repetition (`.append(street, other.street)` to `.append(street)`)
- Make it comparable to naive implementation generated by IDE performance-wise
