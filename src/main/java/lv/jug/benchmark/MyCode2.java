package lv.jug.benchmark;

// Getter vs cached attribute
public class MyCode2 {

    public static void first() {
        Bean abc = new Bean("abc");
        System.out.println(abc.getAttribute());
        System.out.println(abc.getAttribute());
        System.out.println(abc.getAttribute());
    }

    public static void second() {
        final Bean abc = new Bean("abc");
        final String attribute = abc.attribute;
        System.out.println(attribute);
        System.out.println(attribute);
        System.out.println(attribute);
    }

    public static class Bean {

        String attribute;

        public Bean(String attribute) {
            this.attribute = attribute;
        }

        public String getAttribute() {
            return attribute;
        }
    }

}
