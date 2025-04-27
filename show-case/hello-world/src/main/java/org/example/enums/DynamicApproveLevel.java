package org.example.enums;

import lombok.Getter;

@Getter
public enum DynamicApproveLevel {

    FIRST_REVIEWER("first_reviewer", "一级审核人"),
    SECONDARY_REVIEWER("secondary_reviewer", "二级审核人"),
    COE_REVIEWER("coe_reviewer", "COE审核人");

    private final String code;

    private final String name;

    DynamicApproveLevel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static DynamicApproveLevel of(String code) {
        for (DynamicApproveLevel level : DynamicApproveLevel.values()) {
            if (level.getCode().equals(code)) {
                return level;
            }
        }
        return null;
    }

}
