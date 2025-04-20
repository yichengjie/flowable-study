package org.example.book.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.book.enums.ApproveLevelEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Slf4j
public class CommonUtilTest {

    @ParameterizedTest
    @CsvSource({
        "AUTO, DIRECTOR, EMPLOYEE",
        "EMPLOYEE, DIRECTOR, MANAGER",
        "MANAGER, DIRECTOR, DIRECTOR",
        "DIRECTOR, DIRECTOR, null"
    })
    void getNextApprovalLevel(String currentLevelCode, String maxLevelCode, String expectedNextLevelCode) {
        ApproveLevelEnum currentApprovalLevel = ApproveLevelEnum.of(currentLevelCode);
        ApproveLevelEnum maxApprovalLevel = ApproveLevelEnum.of(maxLevelCode);
        ApproveLevelEnum expectNextApprovalLevel = ApproveLevelEnum.of(expectedNextLevelCode);
        ApproveLevelEnum nextApprovalLevel = CommonUtil.getNextApprovalLevel(currentApprovalLevel, maxApprovalLevel);
        log.info("下一个审批级别: {}", nextApprovalLevel);
        Assertions.assertEquals(expectNextApprovalLevel, nextApprovalLevel) ;
    }
}
