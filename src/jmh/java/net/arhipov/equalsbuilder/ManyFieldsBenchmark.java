package net.arhipov.equalsbuilder;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static net.arhipov.equalsbuilder.Human.Color.*;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ManyFieldsBenchmark {

    private Human first;
    private Object second;

    @Setup
    public void setup() {
        first = generateHuman();
        second = generateHuman();
    }


    @Benchmark
    public void classic(Blackhole bh) {
        bh.consume(classicEquals(first, second));
    }

    @Benchmark
    public void java7(Blackhole bh) {
        bh.consume(java7Equals(first, second));
    }

    @Benchmark
    public void equalsBuilder(Blackhole bh) {
        bh.consume(equalsBuilderEquals(first, second));
    }

    @Benchmark
    public void apacheCommons(Blackhole bh) {
        bh.consume(apacheCommonsEquals(first, second));
    }

    public boolean equalsBuilderEquals(Human a, Object b) {
        return EqualsBuilder.test(first, second)
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

    private boolean apacheCommonsEquals(Human a, Object b) {
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

    private boolean classicEquals(Human a, Object b) {
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

    private boolean java7Equals(Human a, Object b) {
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

    private Human generateHuman() {
        Human human = new Human("super name", "super surname", 33);

        HashMap<String, String> attitude = new HashMap<>();
        attitude.put("food", "love");
        attitude.put("alcohole", "dislike");
        attitude.put("tobacco", "disapprove");
        human.setAttitude(attitude);
        human.setDead(false);
        human.setFovirteWords(Arrays.asList("mumble", "humble", "bundle"));
        human.setHairColor(BLONDE);

        return human;
    }

}
