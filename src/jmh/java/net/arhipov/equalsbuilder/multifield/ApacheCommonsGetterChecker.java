package net.arhipov.equalsbuilder.multifield;

public class ApacheCommonsGetterChecker {

    public static boolean areEqual(Human a, Object b) {
        if (a == b) return true;
        if (b == null || a.getClass() != b.getClass()) return false;
        Human human = (Human) b;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
            .append(a.getAge(), human.getAge())
            .append(a.getId(), human.getId())
            .append(a.isDead(), human.isDead())
            .append(a.getName(), human.getName())
            .append(a.getSurname(), human.getSurname())
            .append(a.getHairColor(), human.getHairColor())
            .append(a.getFovirteWords(), human.getFovirteWords())
            .append(a.getAttitude(), human.getAttitude())
            .isEquals();
    }

}
