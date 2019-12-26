package com.havaz.transport.core.utils;

import org.apache.commons.lang3.StringUtils;

public final class Strings {
    public static final String EMPTY = StringUtils.EMPTY;
    public static final String SPACE = StringUtils.SPACE;
    public static final String DOT = ".";
    public static final String COMMA = ",";
    public static final String DASH = "-";
    public static final String SLASH = "/";
    public static final String UNDER_LINE = "_";
    public static final String COLON_DOT = " : ";

    public static String reverseStringByWords(String string) {
        String result = "";
        boolean b = string.contains("<");
        if (string.contains("<") && string.contains(">") || !string.contains(DASH)) {
            result = string;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            String[] words = string.split(DASH);
            for (int j = words.length - 1; j >= 0; j--) {
                if (j > 0) {
                    stringBuilder.append(words[j].trim()).append(" - ");
                } else {
                    stringBuilder.append(words[j].trim());
                }
            }
            result = stringBuilder.toString();
        }
        return result;
    }
}
