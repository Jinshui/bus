package com.bus.services.util;

import org.apache.commons.lang3.StringUtils;

public class BooleanUtil {
    public static boolean isTrue(Boolean value) {
        return !(value == null || !value);
    }

    public static boolean isTrue(String value) {
        return isTrue(createBoolean(value));
    }

    public static Boolean createBoolean(String value) {
        if (StringUtils.isNotBlank(value)) {
            if (StringUtils.equalsIgnoreCase("y", value) || StringUtils.equalsIgnoreCase("yes", value) || StringUtils.equalsIgnoreCase("t", value)) {
                return Boolean.TRUE;
            }
            try {
                return Boolean.valueOf(value);
            }
            catch (Exception e) {
                return Boolean.FALSE;
            }
        }
        return null;
    }
}
