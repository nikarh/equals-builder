package net.arhipov.equalsbuilder;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class EqualsBuilderBenchmark {

    private ClassicHuman classicHuman1;
    private ClassicHuman classicHuman2;

    private BuilderHuman builderHuman1;
    private BuilderHuman builderHuman2;

    private Apache apache1;
    private Apache apache2;

    @Setup
    public void setup() {
        classicHuman1 = new ClassicHuman("Darth", "Vader", 35);
        classicHuman2 = new ClassicHuman("Darth", "Vader", 35);

        builderHuman1 = new BuilderHuman("Darth", "Vader", 35);
        builderHuman2 = new BuilderHuman("Darth", "Vader", 35);

        apache1 = new Apache("Darth", "Vader", 35);
        apache2 = new Apache("Darth", "Vader", 35);
    }


    @Benchmark
    public void classic(Blackhole bh) {
        bh.consume(classicHuman1.equals(classicHuman2));
    }

    @Benchmark
    public void eager(Blackhole bh) {
        bh.consume(builderHuman1.equals(builderHuman2));
    }

    @Benchmark
    public void apache(Blackhole bh) {
        bh.consume(apache1.equals(apache2));
    }

}
