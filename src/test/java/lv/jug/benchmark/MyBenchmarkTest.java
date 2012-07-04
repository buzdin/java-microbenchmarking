package lv.jug.benchmark;

import java.lang.management.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author dmitry buzdin
 * @since 03.07.2012
 */
public class MyBenchmarkTest {

    public static void main(String[] args) throws Exception {
        BenchmarkRunner runner = new BenchmarkRunner();
        runner.run(new Runnable() {
            @Override
            public void run() {
                try {
                    MyCode4.second();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static class BenchmarkRunner {

        public static final int WARM_UPS = 10000;
        public static final int ITERATIONS = 1000;
        public static final int DRY_RUNS = 10;

        private final ClassLoadingMXBean classLoading;
        private CompilationMXBean compilation;
        private MemoryMXBean memory;
        private List<GarbageCollectorMXBean> garbageCollectors;
        private final ThreadMXBean threadBean;

        public BenchmarkRunner() {
            classLoading = ManagementFactory.getClassLoadingMXBean();
            compilation = ManagementFactory.getCompilationMXBean();
            memory = ManagementFactory.getMemoryMXBean();
            garbageCollectors = ManagementFactory.getGarbageCollectorMXBeans();
            threadBean = ManagementFactory.getThreadMXBean();
        }

        public void run(Runnable runnable) throws Exception {
            Stats startStats;
            long startTime;
            long endTime;
            Stats endStats;

            warmUp(runnable, WARM_UPS);
            jvmClean();
            long[] timings = new long[ITERATIONS];
            int skips = 0;

            for (int i = 0; i < ITERATIONS + DRY_RUNS; i++) {

                startStats = stats();
                startTime = timeNs();

                runnable.run();

                endTime = timeNs();
                endStats = stats();

                if (i < DRY_RUNS) {
                    continue;
                }

                if (startStats.equals(endStats)) {
                    timings[i - DRY_RUNS] = endTime - startTime;
                } else {
                    System.out.println(startStats);
                    System.out.println("---------");
                    System.out.println(endStats);
                    skips++;
                    i--;
                }
            }

            long total = 0;
            for (long timing : timings) {
                total += timing;
            }
            System.out.println(Arrays.toString(timings));
            System.out.println("Skipped : " + skips);
            System.out.println("Average value : " + total / ITERATIONS);
        }

        private void jvmClean() throws Exception {
            System.runFinalization();
            System.gc();
            Thread.sleep(100);
        }

        private void warmUp(Runnable runnable, int times) {
            for (int i = 0; i < times; i++) {
                runnable.run();
            }
        }

        private Stats stats() {
            Stats stats = new Stats();
            stats.setLoadedClassCount(classLoading.getTotalLoadedClassCount());
            stats.setCompilationTime(compilation.getTotalCompilationTime());

            long[] collectionCount = new long[garbageCollectors.size()];
            for (int i = 0, garbageCollectorsSize = garbageCollectors.size(); i < garbageCollectorsSize; i++) {
                GarbageCollectorMXBean collector = garbageCollectors.get(i);
                collectionCount[i] = collector.getCollectionCount();
            }
            stats.setGC(collectionCount);
            return stats;
        }

        private long timeNs() {
            return threadBean.getCurrentThreadCpuTime();
        }

    }

    private static class Stats {

        private long loadedClassCount;
        private long compilationTime;
        private long[] collectionCount;

        public void setLoadedClassCount(long loadedClassCount) {
            this.loadedClassCount = loadedClassCount;
        }

        public void setCompilationTime(long compilationTime) {
            this.compilationTime = compilationTime;
        }

        public void setGC(long[] collectionCount) {
            this.collectionCount = collectionCount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Stats stats = (Stats) o;

            if (compilationTime != stats.compilationTime) return false;
            if (loadedClassCount != stats.loadedClassCount) return false;
            if (!Arrays.equals(collectionCount, stats.collectionCount)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (int) (loadedClassCount ^ (loadedClassCount >>> 32));
            result = 31 * result + (int) (compilationTime ^ (compilationTime >>> 32));
            result = 31 * result + (collectionCount != null ? Arrays.hashCode(collectionCount) : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Stats{" +
                    "loadedClassCount=" + loadedClassCount +
                    ", compilationTime=" + compilationTime +
                    ", collectionCount=" + Arrays.toString(collectionCount) +
                    '}';
        }
    }

}
