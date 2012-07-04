package lv.jug.benchmark;

import java.lang.reflect.Method;

// Reflection vs direct method access
public class MyCode3 {

    public static void first() {
        Bean bean = new Bean("abc");
        String attribute = bean.getAttribute();
        //System.out.println(attribute);
    }

    public static void second() throws Exception {
        Bean bean = new Bean("abc");
        Method method = Bean.class.getMethod("getAttribute");
        Object result = method.invoke(bean);
        //System.out.println(result);
    }
    
    private static class Bean {
        
        String attribute;

        private Bean(String attribute) {
            this.attribute = attribute;
        }

        public String getAttribute() {
            return attribute;
        }

    }

}
