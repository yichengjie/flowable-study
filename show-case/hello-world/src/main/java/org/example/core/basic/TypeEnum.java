package org.example.core.basic;

import lombok.Getter;

@Getter
public enum TypeEnum {

    HELLO_WORLD_DYNAMIC("HELLO_WORLD_DYNAMIC") ;
    private final String type;
    TypeEnum(String type) {
        this.type = type;
    }


    public static String getProcessKey(String type) {
        // 根据type返回对应的流程key
        return "process_" + type;
    }
}
