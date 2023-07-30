package com.github.cao.awa.trtr.util.string;

public class StringConcat {
    public static String concat(String s1, Object... concat) {
        StringBuilder s1Builder = new StringBuilder(s1);
        for (Object o : concat) {
            s1Builder.append(o);
        }
        s1 = s1Builder.toString();
        return s1;
    }
}
