package org.example.enums;

import lombok.Getter;

@Getter
public enum DynamicProcessType {
    REGION_APPROVE("region_approve", "区域审批");

    private final String code;

    private final String name;

    DynamicProcessType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
