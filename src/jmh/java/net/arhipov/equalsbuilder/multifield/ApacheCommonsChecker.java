package net.arhipov.equalsbuilder.multifield;

public class ApacheCommonsChecker {

    public static boolean areEqual(Human a, Object b) {
        if (a == b) return true;
        if (b == null || a.getClass() != b.getClass()) return false;
        Human human = (Human) b;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
            .append(a.age, human.age)
            .append(a.id, human.id)
            .append(a.dead, human.dead)
            .append(a.name, human.name)
            .append(a.surname, human.surname)
            .append(a.hairColor, human.hairColor)
            .append(a.fovirteWords, human.fovirteWords)
            .append(a.attitude, human.attitude)
            .isEquals();
    }

}
