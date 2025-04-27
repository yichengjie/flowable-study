package org.example.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.core.approve.TicketInfo;
import org.example.enums.ProcessVariables;
import org.example.utils.JsonConverter;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.List;

@Slf4j
public class PrepareDataServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        // hua_bei、hua_nan、xi_nan、dong_bei
        // 将数据按照区域分组，并存储到流程变量中
        String listContent = (String) execution.getVariable(ProcessVariables.TICKETS);
        if (StringUtils.isNotBlank(listContent)){
            List<TicketInfo> tickets = JsonConverter.deserializeObject(listContent, List.class, TicketInfo.class);
            for (TicketInfo ticket : tickets) {
                log.info("ticket: {}", ticket);
            }
        }else {
            log.info("没有获取到流程变量: {}", ProcessVariables.TICKETS);
        }
    }
}
