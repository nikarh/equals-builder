package net.arhipov.equalsbuilder;

import java.util.Iterator;
import java.util.function.BiPredicate;

class EqualsUtils {

    static <T> boolean areEqual(Iterable<T> a, Iterable<T> b, BiPredicate<T, T> equalizer) {
        Iterator<T> aIterator = a.iterator();
        Iterator<T> bIterator = b.iterator();
        while (aIterator.hasNext() && bIterator.hasNext()) {
            if (!equalizer.test(aIterator.next(), bIterator.next())) {
                return false;
            }
        }

        // Additional size check
        return aIterator.hasNext() == bIterator.hasNext();
    }

}
