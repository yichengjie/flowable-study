package org.example.book.utils;

import org.example.book.enums.ApplyStatusEnum;
import org.example.book.enums.ApproveLevelEnum;

import java.util.UUID;

public class CommonUtil {

    public static String uuid(){

        return UUID.randomUUID().toString().replace("-", "");
    }

    public static ApproveLevelEnum getNextApprovalLevel(
            ApproveLevelEnum currentApprovalLevel, ApproveLevelEnum maxApprovalLevel) {
        ApproveLevelEnum[] values = ApproveLevelEnum.values();
        boolean beginFlag = false;
        for(ApproveLevelEnum level: values){
            // 获取currentApprovalLevel 的下一个级别
            if (level.getLevel() > currentApprovalLevel.getLevel() &&
                    level.getLevel() <= maxApprovalLevel.getLevel()) {
                return level;
            }
        }
        return null ;
    }

    public static ApplyStatusEnum getStatusByApproveLevel(ApproveLevelEnum level){
        if (level == null) {
            return null;
        }
        //    AUTO("AUTO", "自动审批", 0) => INIT
        //    EMPLOYEE("EMPLOYEE", "员工", 1) => EMPLOYEE_APPROVING
        //    MANAGER("MANAGER", "经理", 2) => MANAGER_APPROVING
        //    DIRECTOR("DIRECTOR", "馆长", 3) => DIRECTOR_APPROVING
        // switch case mapping to status
        switch (level){
            case AUTO -> {
                return ApplyStatusEnum.INIT;
            }
            case EMPLOYEE -> {
                return ApplyStatusEnum.EMPLOYEE_APPROVING;
            }
            case MANAGER -> {
                return ApplyStatusEnum.MANAGER_APPROVING;
            }
            case DIRECTOR -> {
                return ApplyStatusEnum.DIRECTOR_APPROVING;
            }
        }
        return null;
    }
}
