package com.github.cao.awa.trtr.util.string;

public class StringConcat {
    public static String concat(String s1, Object... concat) {
        for (Object o : concat) {
            s1 = s1.concat(o.toString());
        }
        return s1;
    }
}
