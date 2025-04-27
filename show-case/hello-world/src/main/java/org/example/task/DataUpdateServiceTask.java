package org.example.task;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.core.approve.TicketInfo;
import org.example.enums.ProcessVariables;
import org.example.utils.CommonUtil;
import org.example.utils.JsonConverter;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class DataUpdateServiceTask implements JavaDelegate {
    private Expression urlField ;
    private Expression parametersField ;

    private Expression expressionField ;

    @Override
    public void execute(DelegateExecution execution) {
        String url = urlField.getExpressionText() ;
        String parameters = parametersField.getExpressionText() ;
        String expression = expressionField.getExpressionText() ;
        log.info("======> url : {}", url);
        log.info("======> parameters : {}", parameters);
        log.info("======> expression : {}", expression);
        // 1. 使用hutool 发送http请求
        HashMap<String, Object> paramMap = new HashMap<>();
        String traceId = (String) execution.getVariable(ProcessVariables.TRACE_ID);
        String listContent = (String) execution.getVariable(ProcessVariables.TICKETS);
        List<TicketInfo> tickets = JsonConverter.deserializeObject(listContent, List.class, TicketInfo.class);
        //
        String status = CommonUtil.getParameter(parameters, "status");
        paramMap.put("traceId", traceId);
        paramMap.put("tickets", tickets);
        paramMap.put("status", status);
        String body = JsonConverter.serializeObject(paramMap);
        String result = HttpUtil.post(url, body);
        log.info("result = {}", result);

    }
}
