
package com.wind.parser;

/**
 * Created by sunhuihui on 2015/11/27.
 */
public class ParserUtils {

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
}
