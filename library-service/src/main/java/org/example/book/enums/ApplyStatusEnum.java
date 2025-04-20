package org.example.book.enums;

import lombok.Getter;

@Getter
public enum ApplyStatusEnum {
    /**
     * 初始化
     */
    INIT("INIT", "初始化"),
    /**
     * 员工审批中
     */
    EMPLOYEE_APPROVING("EMPLOYEE_APPROVING", "员工审批中"),
    /**
     * 经理审批中
     */
    MANAGER_APPROVING("MANAGER_APPROVING", "经理审批中"),
    /**
     * 馆长审批中
     */
    DIRECTOR_APPROVING("DIRECTOR_APPROVING", "馆长审批中"),
    /**
     * 审批通过
     */
    APPROVED("APPROVED", "审批通过") ;

    private final String code ;

    private final String name ;

    ApplyStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ApplyStatusEnum of(String code) {
        for (ApplyStatusEnum status : ApplyStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }


}
