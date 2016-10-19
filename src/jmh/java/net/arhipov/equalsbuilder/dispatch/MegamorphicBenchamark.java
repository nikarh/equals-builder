package net.arhipov.equalsbuilder.dispatch;

import net.arhipov.equalsbuilder.EqualsBuilder;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Fork(warmups = 0, value = 1)
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MegamorphicBenchamark {

    private SpaceCoordinate firstCoordinate;
    private SpaceCoordinate secondCoordinate;

    private Human firstHuman;
    private Human secondHuman;

    @Setup
    public void setup() {
        firstCoordinate = new SpaceCoordinate(1, 2, 3);
        secondCoordinate = new SpaceCoordinate(1, 2, 3);

        firstHuman = new Human("asdasd34", " nsd sdvwe", "234sdfsdf");
        secondHuman = new Human("asdasd34", " nsd sdvwe", "234sdfsdf");
    }

    @Benchmark
    public boolean equalsBuilderMonomorphic() {
        return EqualsBuilder.test(firstCoordinate, secondCoordinate)
            .comparing(SpaceCoordinate::getX)
            .areEqual();
    }

    @Benchmark
    public boolean equalsBuilderMonomorphicBoxed() {
        return EqualsBuilder.test(firstCoordinate, secondCoordinate)
            .comparing((Function<SpaceCoordinate, Integer>) SpaceCoordinate::getX)
            .areEqual();
    }


    @Benchmark
    public boolean equalsBuilderBimorphic() {
        return EqualsBuilder.test(firstCoordinate, secondCoordinate)
            .comparing(SpaceCoordinate::getX)
            .comparing(SpaceCoordinate::getY)
            .areEqual();
    }

    @Benchmark
    public boolean equalsBuilderBimorphicBoxed() {
        return EqualsBuilder.test(firstCoordinate, secondCoordinate)
            .comparing((Function<SpaceCoordinate, Integer>) SpaceCoordinate::getX)
            .comparing((Function<SpaceCoordinate, Integer>) SpaceCoordinate::getY)
            .areEqual();
    }

    @Benchmark
    public boolean equalsBuilderMegamorphic() {
        return EqualsBuilder.test(firstCoordinate, secondCoordinate)
            .comparing(SpaceCoordinate::getX)
            .comparing(SpaceCoordinate::getY)
            .comparing(SpaceCoordinate::getZ)
            .areEqual();
    }

    @Benchmark
    public boolean equalsBuilderMegamorphicStrings() {
        return EqualsBuilder.test(firstHuman, secondHuman)
            .comparing(Human::getName)
            .comparing(Human::getSurname)
            .comparing(Human::getMiddleName)
            .areEqual();
    }

    @Benchmark
    public boolean equalsBuilderMegamorphicBoxed() {
        return EqualsBuilder.test(firstCoordinate, secondCoordinate)
            .comparing((Function<SpaceCoordinate, Integer>) SpaceCoordinate::getX)
            .comparing((Function<SpaceCoordinate, Integer>) SpaceCoordinate::getY)
            .comparing((Function<SpaceCoordinate, Integer>) SpaceCoordinate::getZ)
            .areEqual();
    }

}
