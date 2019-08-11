package org.evan.libraries.utils;

/**
 *
 */
public class DateParseException extends RuntimeException {
    public DateParseException(String strDate, String pattern, Throwable cause) {
        super("输入日期[" + strDate + "]日期格式不正确,正确的格式为[" + pattern + "]", cause);
    }
}
