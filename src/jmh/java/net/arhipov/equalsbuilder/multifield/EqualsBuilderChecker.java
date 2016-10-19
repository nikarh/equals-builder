package net.arhipov.equalsbuilder.multifield;

import net.arhipov.equalsbuilder.EqualsBuilder;

public class EqualsBuilderChecker {

    public static boolean areEqual(Human a, Object b) {
        return EqualsBuilder.test(a, b)
            .comparing(Human::getAge)
            .comparing(Human::getId)
            .comparing(Human::isDead)
            .comparing(Human::getName)
            .comparing(Human::getSurname)
            .comparing(Human::getHairColor)
            .comparing(Human::getFovirteWords)
            .comparing(Human::getAttitude)
            .areEqual();
    }

}
