package net.arhipov.equalsbuilder.multifield;

import java.util.Objects;

public class Java7GetterChecker {

    public static boolean areEqual(Human a, Object b) {
        if (a == b) return true;
        if (b == null || a.getClass() != b.getClass()) return false;
        Human human = (Human) b;
        return a.getAge() == human.getAge() &&
            a.getId() == human.getId() &&
            a.isDead() == human.isDead() &&
            Objects.equals(a.getName(), human.getName()) &&
            Objects.equals(a.getSurname(), human.getSurname()) &&
            a.getHairColor() == human.getHairColor() &&
            Objects.equals(a.getFovirteWords(), human.getFovirteWords()) &&
            Objects.equals(a.getAttitude(), human.getAttitude());
    }

}
