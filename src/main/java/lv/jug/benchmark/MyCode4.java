package lv.jug.benchmark;

// Primitives vs wrappers
public class MyCode4 {

    public static final int SIZE = 1000;

    public static void first() {
        int[] array = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            array[i] = i;
        }
    }

    public static void second() {
        Integer[] array = new Integer[SIZE];
        for (int i = 0; i < SIZE; i++) {
            array[i] = i;
        }
    }

}
