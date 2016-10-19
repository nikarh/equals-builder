package net.arhipov.equalsbuilder.multifield;

import java.util.Objects;

public class Java7Checker {

    public static boolean areEqual(Human a, Object b) {
        if (a == b) return true;
        if (b == null || a.getClass() != b.getClass()) return false;
        Human human = (Human) b;
        return a.age == human.age &&
            a.id == human.id &&
            a.dead == human.dead &&
            Objects.equals(a.name, human.name) &&
            Objects.equals(a.surname, human.surname) &&
            a.hairColor == human.hairColor &&
            Objects.equals(a.fovirteWords, human.fovirteWords) &&
            Objects.equals(a.attitude, human.attitude);
    }

}
