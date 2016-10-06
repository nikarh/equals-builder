package net.arhipov.equalsbuilder;

import java.util.Collection;
import java.util.Map;
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
 * Object fields are extracted using getter methods, which can conveniently be passed
 * as reference methods in Java 8.
 * </p>
 * <p>
 * The following example illustrates how EqualsBuilder might me used
 * to compare complex class objects:
 * </p>
 * <pre>{@code
 *  class Account {
 *      private String owner;
 *      private long balance;
 *
 *      public String getOwner() { return owner; }
 *      public long getBalance() { return balance; }
 *  }
 *  class FinancialOperation {
 *      private int amount;
 *      private Account from;
 *      private Account to;
 *
 *      public int getAmount { return amount; }
 *      public Account getFrom() { return from; }
 *      public Account getTo() { return from; }
 *  }
 *  class Payment {
 *      private int id;
 *      private List<FinancialOperation> financialOperations;
 *
 *      public int getId() { return id; }
 *      public List<FinancialOperation> getFinancialOperations() { return financialOperations; }
 *  }
 *
 *  EqualsBuilder.test(payment1, payment2)
 *      .comparing(Payment::getId)
 *      .comparingCollections(Payment::getFinancialOperations, (op1, op2) -> EqualsBuilder.test(op1, op2)
 *          .comparing(FinancialOperation::getAmount)
 *          .comparing(FinancialOperation::getFrom, (from1, from2) -> EqualsBuilder.test(from1, from2)
 *              .comparing(Account::getOwner)
 *              .comparing(Account::getBalance)
 *              .areEqual())
 *          .comparing(FinancialOperation::getTo, (to1, to2) -> EqualsBuilder.test(to1, to2)
 *              .comparing(Account::getOwner)
 *              .comparing(Account::getBalance)
 *              .areEqual())
 *          .areEqual())
 *      .areEqual();
 * }</pre>
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
     * @param getter a method applied to both objects to extract field (usually getter)
     * @return EqualsBuilder instance
     */
    public EqualsBuilder<T> comparing(Function<T, ?> getter) {
        return comparing(getter, Objects::equals);
    }

    /**
     * Compare objects fields using getter and a defined comparing function.
     * This method might be used to compare objects for equality deeply.
     * <p><b>Deep comparison example:</b>
     * <pre>{@code
     * EqualsBuilder.test(first, second)
     *     .comparing(MyClass::getInnerObject, (a, b) -> EqualsBuilder.test(a, b)
     *         .comparing(InnerClass::getFirstField)
     *         .areEqual())
     *     .areEqual();
     * }</pre>
     *
     * @param getter    a method applied to both objects to extract field (usually getter)
     * @param equalizer a method used to compare two objects
     * @param <R>       a type of the extracted field
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
     * Compares two collections of the same generic type extracted from objects with getter
     * using provided BiPredicate collections elements with each other. This method should
     * only be used for collections, where elements have order, such as List. Thus
     * this method is not suitable for comparing sets.
     * <p><b>Example:</b>
     * <pre>{@code
     * EqualsBuilder.test(first, second)
     *     .comparingCollections(MyClass::getList, (a, b) -> EqualsBuilder.test(a, b)
     *         .comparing(CollectionElement::getFirstField)
     *         .areEqual())
     *     .areEqual();
     * }</pre>
     *
     * @param getter    a method applied to both objects to extract collections (usually getter)
     * @param equalizer a method used to compare two collection elements
     * @param <R>       a collection elements type
     * @return EqualsBuilder instance
     */
    public <R> EqualsBuilder<T> comparingCollections(Function<T, Collection<R>> getter, BiPredicate<R, R> equalizer) {
        if (skip) {
            return this;
        }

        Collection<R> ca = getter.apply(a);
        Collection<R> cb = getter.apply(b);

        if (ca != cb &&
                (ca == null || cb == null || ca.size() != cb.size() || !EqualsUtils.areEqual(ca, cb, equalizer))) {
            skip = true;
            equal = false;
        }

        return this;
    }

    /**
     * Compares two iterables of the same generic type extracted from objects with getter
     * using provided BiPredicate collections elements with each other.
     * For iterables that also implement Collection interface using
     * {@link #comparingCollections(Function, BiPredicate)} method is prefered.
     * <p><b>Example:</b>
     * <pre>{@code
     * EqualsBuilder.test(first, second)
     *     .comparingIterables(MyClass::getIterable, (a, b) -> EqualsBuilder.test(a, b)
     *         .comparing(iterableElement::getFirstField)
     *         .areEqual())
     *     .areEqual();
     * }</pre>
     *
     * @param getter    a method applied to both objects to extract iterables (usually getter)
     * @param equalizer a method used to compare two iterable elements
     * @param <R>       an iterable elements type
     * @return EqualsBuilder instance
     */
    public <R> EqualsBuilder<T> comparingIterables(Function<T, Iterable<R>> getter, BiPredicate<R, R> equalizer) {
        if (skip) {
            return this;
        }

        Iterable<R> ca = getter.apply(a);
        Iterable<R> cb = getter.apply(b);

        if (ca != cb &&
                (ca == null || cb == null || !EqualsUtils.areEqual(ca, cb, equalizer))) {
            skip = true;
            equal = false;
        }

        return this;
    }

    /**
     * Compares two maps extracted with a getter by their <b>values</b> using provided
     * equality checking function. For values to be compared correctly,
     * keys should be equal (override equals and hashCode methods correctly).
     * <p><b>Example:</b>
     * <pre>{@code
     * class PaymentHistory {
     *     private Map<String, Payment> paymentIndex;
     * }
     *
     * EqualsBuilder.test(history1, history2)
     *     .comparingMaps(PaymentHistory::getPaymentIndex, (a, b) -> EqualsBuilder.test(a, b)
     *         .comparing(Payment::getAmount)
     *         .comparing(Payment::getAccountFrom)
     *         .comparing(Payment::getAccountTo)
     *         .areEqual())
     *     .areEqual();
     * }</pre>
     *
     * @param getter    a method used to extract a map (usually a getter)
     * @param equalizer a method used to compare two map elements (values only)
     * @param <K>       a key type that is the same for both maps
     * @param <R>       a value type for both maps
     * @return EqualsBuilder instance
     */
    public <K, R> EqualsBuilder<T> comparingMaps(Function<T, Map<K, R>> getter, BiPredicate<R, R> equalizer) {
        if (skip) {
            return this;
        }

        Map<K, R> ma = getter.apply(a);
        Map<K, R> mb = getter.apply(b);

        if (ma == mb) {
            return this;
        } else if (ma == null || mb == null || ma.size() != mb.size()) {
            skip = true;
            equal = false;
            return this;
        }

        for (Map.Entry<K, R> e : ma.entrySet()) {
            K key = e.getKey();
            R value = e.getValue();
            if (value == null) {
                if (mb.get(key) != null || !ma.containsKey(key)) {
                    skip = true;
                    equal = false;
                    return this;
                }
            } else if (!equalizer.test(value, mb.get(key))) {
                skip = true;
                equal = false;
                return this;
            }
        }

        return this;
    }

    /**
     * Compare primitive int fields of both objects.
     *
     * @param getter a method applied to both objects to extract int field (usually getter)
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
     * Compare primitive long fields of both objects.
     *
     * @param getter a method applied to both objects to extract long field (usually getter)
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
     * Compare primitive double fields of both objects.
     *
     * @param getter a method applied to both objects to extract double field (usually getter)
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
     * Returns true if either objects are considered equal and false otherwise.
     * Objects are equal if they actually are references to the same object,
     * both object are null, or for each {@link #comparing(Function)} call,
     * extracted objects and primitives where also considered equal.
     *
     * @return true if objects are equal and false otherwise
     */
    public boolean areEqual() {
        return equal;
    }

}
