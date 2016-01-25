package com.wind.parser;

/**
 * Created by sunhuihui on 2015/11/27.
 */
public class ParserUtils {

    public static final String SPLIT_FLAG = "[,/]";

    public static void log(Object tag, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(tag);
        sb.append(":");
        sb.append(msg);
        System.out.println(sb.toString());
    }

    public static boolean isEmptyText(String text) {
        return text == null || text.matches("\\s*");
    }

    /**
     * 判断{@link jxl.Cell}中内容是否包含无效字符，例如:null,none都是不需要关注的.
     *
     * @param text
     * @return
     */
    public static boolean isInvalidChar(String text) {
        return isEmptyText(text) || "null".equalsIgnoreCase(text)
                || "none".equalsIgnoreCase(text);
    }

    public static String filterInvalidChars(String text) {
        //1. \n
        if (text.contains("\n")) {
            text = text.replaceAll("\\n", "");
        }
        // 2. 65533
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.toCharArray().length; i++) {
            char c = text.charAt(i);
            if (c == 65533) {
                continue;
            }
            sb.append(text.charAt(i));
        }
        return sb.toString();
    }
}
