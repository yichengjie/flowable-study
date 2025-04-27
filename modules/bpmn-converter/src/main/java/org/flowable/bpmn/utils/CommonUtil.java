package org.flowable.bpmn.utils;

public class CommonUtil {

    public static String uuid() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}
