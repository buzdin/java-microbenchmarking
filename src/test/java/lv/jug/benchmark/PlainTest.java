package lv.jug.benchmark;

import org.junit.Test;

/**
 * @author dmitry buzdin
 * @since 03.07.2012
 */
public class PlainTest {
    
    @Test
    public void simpleTest() {
        long start = System.currentTimeMillis();
        MyCode.run();
        long stop = System.currentTimeMillis();
        System.out.println(stop - start);
    }
    
}
