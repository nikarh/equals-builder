package net.arhipov.equalsbuilder.multifield;

public class ClassicGetterChecker {

    public static boolean areEqual(Human a, Object b) {
        if (a == b) return true;
        if (b == null || a.getClass() != b.getClass()) return false;
        Human human = (Human) b;

        if (a.getAge() != human.getAge()) return false;
        if (a.getId() != human.getId()) return false;
        if (a.isDead() != human.isDead()) return false;
        if (a.getName() != null
            ? !a.getName().equals(human.getName())
            : human.getName() != null) return false;
        if (a.getSurname() != null
            ? !a.getSurname().equals(human.getSurname())
            : human.getSurname() != null) return false;
        if (a.getHairColor() != human.getHairColor()) return false;
        if (a.getFovirteWords() != null
            ? !a.getFovirteWords().equals(human.getFovirteWords())
            : human.getFovirteWords() != null)
            return false;

        return a.getAttitude() != null
            ? a.getAttitude().equals(human.getAttitude())
            : human.getAttitude() == null;
    }

}
