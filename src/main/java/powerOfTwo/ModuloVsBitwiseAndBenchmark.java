package powerOfTwo;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;

//@State(Scope.Benchmark)
@State(Scope.Thread)
//@Threads(5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ModuloVsBitwiseAndBenchmark {
    long modCount = 1;
    long bitWiseCount = 1;
    //    @Param({"16","32","64","128","256"})
    @Param({"128"})
    int cap;
    //    @Param({"929058756","530210931","1405711102","910458916","983070208"})
    @Param({"1405711102"})
    int hash;

    @Benchmark
    public int testModulo(Blackhole blackhole) {
        modCount++;
        blackhole.consume(modCount);
        return hash % cap;
    }

    @Benchmark
    public int testBitwiseAnd(Blackhole blackhole) {
        bitWiseCount++;
        blackhole.consume(bitWiseCount);
        return hash & (cap - 1);
    }

    @TearDown
    public void tearDown() {
        System.out.println("Final count of modCount : " + modCount);
        System.out.println("Final count of bitWiseCount : " + bitWiseCount);
    }

    public static void main(String[] args) throws RunnerException {
        int forkValue = 1;
        int iterations = 5;
        TimeValue timeInSeconds = TimeValue.seconds(2);
        Options opt = new OptionsBuilder()
                .include(ModuloVsBitwiseAndBenchmark.class.getSimpleName())
                .forks(forkValue)//no of separate JVM to run the benchmark
                .warmupIterations(iterations)//run n warmup iterations before actual BM measuring.
                .warmupTime(timeInSeconds)//duration of each warmup iteration.
                .measurementIterations(iterations)//perform n measurement iterations after the warmup.
                .measurementTime(timeInSeconds)//duration of each measurement iteration.
                .build();
        new Runner(opt).run();
    }
}


//            .jvmArgs("-XX:+PrintCompilation")
//             .jvmArgs("-XX:+UnlockDiagnosticVMOptions", "-XX:+PrintAssembly")
