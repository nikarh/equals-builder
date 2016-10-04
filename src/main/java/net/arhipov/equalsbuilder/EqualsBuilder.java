package net.arhipov.equalsbuilder;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * Compares two java objects comparing common superclass.
 * <p>
 * EqualsBuilder allows you to compare objects using fluent builder pattern.
 * Objects are compared by getter methods, which can conveniently be passed
 * as reference methods in Java 8.
 *
 * @param <T> common supertype of objects being compared
 */
public class EqualsBuilder<T> {

    private T a;
    private T b;
    private boolean equal = true;
    private boolean skip = false;

    @SuppressWarnings("unchecked")
    private EqualsBuilder(T a, Object b, Class<T> commonType) {
        if (a == b) {
            skip = true;
        } else if (a == null || b == null || !commonType.isAssignableFrom(b.getClass())) {
            equal = false;
            skip = true;
        } else {
            this.a = a;
            this.b = (T) b;
        }
    }

    /**
     * Creates EqualsBuilder instance for two objects.
     * <p>
     * For objects to be equal, b should be the same type as a.
     *
     * @param a   Typed object to be compared
     * @param b   Untyped object to be compared
     * @param <T> Class of first object.
     * @return EqualsBuilder instance for T class
     */
    @SuppressWarnings("unchecked")
    public static <T> EqualsBuilder<T> test(T a, Object b) {
        return new EqualsBuilder<>(a, b, a == null ? null : (Class<T>) a.getClass());
    }

    /**
     * Creates EqualsBuilder instance for two objects comparing a common superclass.
     * <p>
     * B object's type should be a superclass of commonType.
     *
     * @param a          Typed object to be compared
     * @param b          Untyped object to be compared
     * @param commonType Common super class for both a and b objects
     * @param <T>        Common superclass type. In most cases should be inferred.
     * @param <U>        Actual class of first object.
     * @return EqualsBuilder instance for T class
     */
    public static <T, U extends T> EqualsBuilder<T> test(U a, Object b, Class<T> commonType) {
        return new EqualsBuilder<>(a, b, commonType);
    }

    /**
     * Compare objects fields using getter.
     *
     * @param getter getter method for objects field
     * @return EqualsBuilder instance
     */
    public EqualsBuilder<T> comparing(Function<T, ?> getter) {
        return comparing(getter, Objects::equals);
    }

    /**
     * Compare objects fields using getter and a defined comparing function.
     * This should be used to compare objects for equality deeply:
     *
     * {@code
     *     EqualsBuilder.test(first, second)
     *         .comparing(MyClass::getInnerObject, (a, b) -> EqualsBuilder.test(a, b)
     *             .comparing(InnerClass::getFirstField)
     *             .areEqual())
     *         .areEqual();
     * }
     *
     * @param getter    getter method for objects field
     * @param equalizer function used to compare two objects
     * @param <R>       type of an object returned by the getter
     * @return EqualsBuilder instance
     */
    public <R> EqualsBuilder<T> comparing(Function<T, R> getter, BiPredicate<R, R> equalizer) {
        if (!skip && !equalizer.test(getter.apply(a), getter.apply(b))) {
            skip = true;
            equal = false;
        }
        return this;
    }

    /**
     * Compare objects primitive int fields.
     *
     * @param getter getter method for objects field
     * @return EqualsBuilder instance
     */
    public EqualsBuilder<T> comparing(ToIntFunction<T> getter) {
        if (!skip && getter.applyAsInt(a) != getter.applyAsInt(b)) {
            skip = true;
            equal = false;
        }
        return this;
    }

    /**
     * Compare objects primitive long fields.
     *
     * @param getter getter method for objects field
     * @return EqualsBuilder instance
     */
    public EqualsBuilder<T> comparing(ToLongFunction<T> getter) {
        if (!skip && getter.applyAsLong(a) != getter.applyAsLong(b)) {
            skip = true;
            equal = false;
        }
        return this;
    }

    /**
     * Compare objects primitive double fields.
     *
     * @param getter getter method for objects field
     * @return EqualsBuilder instance
     */
    public EqualsBuilder<T> comparing(ToDoubleFunction<T> getter) {
        if (!skip && getter.applyAsDouble(a) != getter.applyAsDouble(b)) {
            skip = true;
            equal = false;
        }
        return this;
    }

    /**
     * Returns true if either objects are equal by reference, or for each getter
     * provided, returned values are equal for both objects.
     *
     * @return true if objects are equal and false otherwise
     */
    public boolean areEqual() {
        return equal;
    }

}
