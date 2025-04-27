package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.core.approve.TicketInfo;
import org.example.enums.DynamicApproveLevel;
import org.example.model.request.UpdateStatusRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/hello-world")
public class HelloWorldController {

    @PostMapping("/update-data")
    public String updateStatus(@RequestBody UpdateStatusRequest request){
        // 处理请求
        String traceId = request.getTraceId();
        String status = request.getStatus();
        List<TicketInfo> tickets = request.getTickets();
        // 进行业务逻辑处理
        for (TicketInfo ticket : tickets) {
            // 更新状态逻辑
            log.info("业务处理 : TraceId: {}, Status: {}, TicketInfo: {}, ", traceId, status, ticket);
        }
        // ...

        return "Status updated successfully";
    }

    @GetMapping("/query-region-assign")
    public String queryRegionAssign(
        @RequestParam("regionCode") String regionCode,
        @RequestParam("approveLevel") String approveLevel){
        if (StringUtils.isBlank(regionCode) || StringUtils.isBlank(approveLevel)){
            return "regionCode or approveLevel is empty";
        }
        DynamicApproveLevel dynamicApproveLevel = DynamicApproveLevel.of(approveLevel);
        if (DynamicApproveLevel.FIRST_REVIEWER == dynamicApproveLevel){
            return "张三";
        }
        if (DynamicApproveLevel.SECONDARY_REVIEWER == dynamicApproveLevel){
            return "李四";
        }
        if (DynamicApproveLevel.COE_REVIEWER == dynamicApproveLevel){
            return "王五";
        }
        return "没有找到对应的审批人";
    }
}
