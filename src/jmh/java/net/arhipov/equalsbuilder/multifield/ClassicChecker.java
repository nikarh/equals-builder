package net.arhipov.equalsbuilder.multifield;

public class ClassicChecker {

    public static boolean areEqual(Human a, Object b) {
        if (a == b) return true;
        if (b == null || a.getClass() != b.getClass()) return false;
        Human human = (Human) b;

        if (a.age != human.age) return false;
        if (a.id != human.id) return false;
        if (a.dead != human.dead) return false;
        if (a.name != null ? !a.name.equals(human.name) : human.name != null) return false;
        if (a.surname != null ? !a.surname.equals(human.surname) : human.surname != null) return false;
        if (a.hairColor != human.hairColor) return false;
        if (a.fovirteWords != null ? !a.fovirteWords.equals(human.fovirteWords) : human.fovirteWords != null)
            return false;

        return a.attitude != null ? a.attitude.equals(human.attitude) : human.attitude == null;
    }

}
