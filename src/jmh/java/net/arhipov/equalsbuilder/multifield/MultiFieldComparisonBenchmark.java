package net.arhipov.equalsbuilder.multifield;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Fork(warmups = 0, value = 1)
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MultiFieldComparisonBenchmark {

    private Human first;
    private Object second;

    @Setup
    public void setup() {
        first = generateHuman();
        second = generateHuman();
    }

    @Benchmark
    public void baseline() {
    }

    @Benchmark
    public boolean equalsBuilder() {
        return EqualsBuilderChecker.areEqual(first, second);
    }

    @Benchmark
    public boolean classic() {
        return ClassicChecker.areEqual(first, second);
    }

    @Benchmark
    public boolean java7() {
        return Java7Checker.areEqual(first, second);
    }

    @Benchmark
    public boolean apacheCommons() {
        return ApacheCommonsChecker.areEqual(first, second);
    }

    @Benchmark
    public boolean classicWithGetters() {
        return ClassicGetterChecker.areEqual(first, second);
    }

    @Benchmark
    public boolean java7WithGetters() {
        return Java7GetterChecker.areEqual(first, second);
    }

    @Benchmark
    public boolean apacheCommonsWithGetters() {
        return ApacheCommonsGetterChecker.areEqual(first, second);
    }


    private Human generateHuman() {
        Human human = new Human("super name", "super surname", 33);

        HashMap<String, String> attitude = new HashMap<>();
        attitude.put("food", "love");
        attitude.put("alcohol", "dislike");
        attitude.put("tobacco", "disapprove");
        human.setAttitude(attitude);
        human.setDead(false);
        human.setFovirteWords(Arrays.asList("mumble", "humble", "bundle"));
        human.setHairColor(Human.Color.BLONDE);

        return human;
    }

}
