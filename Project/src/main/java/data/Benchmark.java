package data;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(time = 1, timeUnit = TimeUnit.SECONDS)
public class Benchmark {


    @State(Scope.Benchmark)
    public static class FullMap {

        List<String> ids;
        List<String> ids_2;
        List<String> data;
        List<String> data_2;
        RBT<String, String> tree;
        RBT<String, String> tree_2;

        @Setup(Level.Iteration)
        public void generateIdsAndCars(BenchmarkParams params) {
            ids = Benchmark.generateKeys(Integer.parseInt(params.getParam("elementCount")));
            data = Benchmark.generateValues(Integer.parseInt(params.getParam("elementCount")));
            ids_2 = Benchmark.generateKeys(Integer.parseInt(params.getParam("elementCount")));
            data_2 = Benchmark.generateValues(Integer.parseInt(params.getParam("elementCount")));
        }

        @Setup(Level.Invocation)
        public void fillTree(BenchmarkParams params) {
            tree = new RBT<>();
            addOne(ids, data, tree);
            tree_2 = new RBT<>();
            addOne(ids_2, data_2, tree_2);
        }
    }

    @Param({ "10000",  "20000",  "30000",  "40000",
             "50000",  "60000",  "70000",  "80000",
             "90000",   "100000",  "110000",  "120000",
             "130000",  "140000",  "150000",  "160000"})
    public int elementCount;

    List<String> ids;
    List<String> ids_2;
    List<String> data;
    List<String> data_2;
    RBT<String, String> RedBlackTree;

    @Setup(Level.Iteration)
    public void generateKeysAndValues() {
        ids = generateKeys(elementCount);
        ids_2 = generateKeys(elementCount);
        data = generateValues(elementCount);
        data_2 = generateValues(elementCount);
        RedBlackTree = new RBT<>();
        addOne(ids_2, data_2, RedBlackTree);
    }

    static List<String> generateKeys(int count) {
        return new ArrayList<>(Generator.generateShuffleKeys(count));
    }

    static List<String> generateValues(int count) {
        return new ArrayList<>(Generator.generateShuffleValues(count));
    }

    @org.openjdk.jmh.annotations.Benchmark
    public RBT<String, String> AddToTree() {
        RBT<String, String> tree = new RBT<>();
        addOne(ids, data, tree);
        return tree;
    }
    @org.openjdk.jmh.annotations.Benchmark
    public RBT<String, String> GetFromTree() {
        RBT<String, String> tree = RedBlackTree;
        getOne(ids_2, tree);
        return tree;
    }
    @org.openjdk.jmh.annotations.Benchmark
    public RBT<String, String> RemoveFromTree() {
        RBT<String, String> tree = RedBlackTree;
        removeOne(ids, tree);
        return tree;
    }

    public static void addOne(List<String> ids, List<String> data, RBT<String, String> tree) {
        for (int i = 0; i < data.size(); i++) {
            tree.Add(ids.get(i), data.get(i));
        }
    }
    public static void removeOne(List<String> ids, RBT<String, String> tree) {
        for (int i = 0; i < ids.size(); i++) {
            tree.Remove(ids.get(i));
        }
    }
    public static void getOne(List<String> ids, RBT<String, String> base) {
        for (int i = 0; i < ids.size(); i++) {
            String a = base.Get(ids.get(i));
        }
    }
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Benchmark.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
