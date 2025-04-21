package org.example.core;

public enum TypeEnum {

    HELLO_WORLD("HELLO_WORLD") ;
    private String type;
    TypeEnum(String type) {
        this.type = type;
    }


    public static String getProcessKey(String type) {
        // 根据type返回对应的流程key
        return "process_" + type;
    }
}
