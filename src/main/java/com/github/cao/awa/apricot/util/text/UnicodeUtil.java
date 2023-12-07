package com.github.cao.awa.apricot.util.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnicodeUtil {
    private static final Pattern PATTERN = Pattern.compile("\\\\u[\\da-f]{4}");

    public static String stringToUnicode(String s) {
        StringBuilder result = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        for (int integer = 0; integer < s.length(); integer++) {
            result.append("\\u");
            builder.setLength(0);
            builder.append(Integer.toHexString(s.charAt(integer))
                                  .toLowerCase());
            while (builder.length() < 4) {
                builder.insert(0,
                               0
                );
            }
            result.append(builder);
        }
        return result.toString();
    }

    public static String unicodeToString(String s) {
        StringBuilder result = new StringBuilder();
        s = s.toLowerCase();
        result.setLength(0);
        Matcher m = PATTERN.matcher(s);
        while (m.find()) {
            result.append((char) Integer.parseInt(m.group()
                                                   .substring(2),
                                                  16
            ));
        }
        return result.toString();
    }
}
