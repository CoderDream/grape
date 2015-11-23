package guda.grape.mvc.spring;

import org.springframework.util.StringUtils;

import java.util.Properties;


public class PropertiesReplace {

    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    public static final String DEFAULT_VALUE_SEPARATOR    = ":";


    public static String resolveStringVal(String strVal, Properties props) {
        int startIndex = strVal.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
        StringBuffer buf = new StringBuffer(strVal);
        if (startIndex > -1) {
            int endIndex = findReplaceEndIndex(buf, startIndex);
            if (endIndex != -1) {
                String placeholder = buf.substring(
                    startIndex + DEFAULT_PLACEHOLDER_PREFIX.length(), endIndex);
                return props.getProperty(placeholder);
            }

        }
        return null;
    }

    public static String resolveStringKey(String strKey) {
        int startIndex = strKey.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
        StringBuffer buf = new StringBuffer(strKey);
        if (startIndex > -1) {
            int endIndex = findReplaceEndIndex(buf, startIndex);
            if (endIndex != -1) {
                String placeholder = buf.substring(
                    startIndex + DEFAULT_PLACEHOLDER_PREFIX.length(), endIndex);
                return placeholder;
            }

        }
        return strKey;
    }


    public static int findReplaceEndIndex(CharSequence buf, int startIndex) {
        int index = startIndex + DEFAULT_PLACEHOLDER_PREFIX.length();
        int withinNestedPlaceholder = 0;
        while (index < buf.length()) {
            if (StringUtils.substringMatch(buf, index, DEFAULT_PLACEHOLDER_SUFFIX)) {
                if (withinNestedPlaceholder > 0) {
                    withinNestedPlaceholder--;
                    index = index + DEFAULT_PLACEHOLDER_SUFFIX.length();
                } else {
                    return index;
                }
            } else if (StringUtils.substringMatch(buf, index, DEFAULT_PLACEHOLDER_PREFIX)) {
                withinNestedPlaceholder++;
                index = index + DEFAULT_PLACEHOLDER_PREFIX.length();
            } else {
                index++;
            }
        }
        return -1;
    }

}
