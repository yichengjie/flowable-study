package org.example.core.approve;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.DynamicProcessType;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class DynamicApproveProcessHelperTest {

    @Test
    void create(){
        List<TicketInfo> tickets = Arrays.asList(
            TicketInfo.builder().id("1").name("工单1").regionCode("hua_bei").build(),
            TicketInfo.builder().id("2").name("工单2").regionCode("hua_nan").build(),
            TicketInfo.builder().id("3").name("工单3").regionCode("xi_nan").build(),
            TicketInfo.builder().id("4").name("工单4").regionCode("dong_bei").build()
        );
        DynamicProcessType type = DynamicProcessType.REGION_APPROVE ;
        String xmlContent = DynamicApproveProcessHelper.create(tickets, type);
        log.info("xmlContent: {}", xmlContent);
    }

}
