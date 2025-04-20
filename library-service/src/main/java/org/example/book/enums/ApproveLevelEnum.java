package org.example.book.enums;

import lombok.Getter;

@Getter
public enum ApproveLevelEnum {

    AUTO("AUTO", "自动审批", 0),
    EMPLOYEE("EMPLOYEE", "员工", 1),
    MANAGER("MANAGER", "经理", 2),
    DIRECTOR("DIRECTOR", "馆长", 3);

    private final String code;

    private final String name;

    private final Integer level ;

    ApproveLevelEnum(String code, String name, Integer level) {
        this.code = code;
        this.name = name;
        this.level = level;
    }

    public static ApproveLevelEnum of(String code) {
        for (ApproveLevelEnum approvalLevel : ApproveLevelEnum.values()) {
            if (approvalLevel.getCode().equals(code)) {
                return approvalLevel;
            }
        }
        return null;
    }


    public static ApproveLevelEnum getByCount(int bookCount) {
        if (bookCount <= 2) {
            return ApproveLevelEnum.AUTO ;
        }else if (bookCount <= 5) {
            return ApproveLevelEnum.EMPLOYEE;
        } else if (bookCount <= 10) {
            return ApproveLevelEnum.MANAGER;
        } else {
            return ApproveLevelEnum.DIRECTOR;
        }
    }
}
