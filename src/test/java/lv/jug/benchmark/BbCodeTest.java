package lv.jug.benchmark;

import bb.util.Benchmark;
import org.junit.Test;

/**
 * @author dmitry buzdin
 * @since 03.07.2012
 */
public class BbCodeTest {

    @Test
    public void benchmark() throws Exception {
        Benchmark benchmark = new Benchmark(new Runnable() {
            @Override
            public void run() {
                MyCode.run();
            }
        });
        System.out.println(benchmark.getFirst());
        System.out.println(benchmark.getStats());
        System.out.println(benchmark.getCallResult());
    }
    

}
