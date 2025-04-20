package org.example.task;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import java.util.HashMap;

@Slf4j
public class QueryApproveLevelTask implements JavaDelegate {

    private Expression parameters ;
    private Expression url ;
    private Expression expression ;


    @Override
    public void execute(DelegateExecution execution) {
        log.info("parameters.getExpressionText() = {}", parameters.getExpressionText());
        //
        log.info("url.getExpressionText() = {}", url.getExpressionText());
        //
        log.info("expression.getExpressionText() = {}", expression.getExpressionText());
        // 1. 使用hutool 发送http请求
        String address = url.getExpressionText() ;
        // 获取businessKey、 currentLevelCode
        String businessKey = (String) execution.getVariable("businessKey");
        String currentLevelCode = (String) execution.getVariable("currentLevelCode");
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", businessKey);
        paramMap.put("currentLevelCode", currentLevelCode);
        // 发送请求
        String result = HttpUtil.get(address, paramMap);
        log.info("result = {}", result);
        if (StringUtils.isBlank(result) || "AUTO".equalsIgnoreCase(result)){
            execution.setVariable("continue_approve_flag", false);
            // 设置当前审批状态
            execution.setVariable("status", "APPROVED");
        }else {
            execution.setVariable("currentLevelCode", result);
            execution.setVariable("continue_approve_flag", true);
            execution.setVariable("currentApproveUser", result +"_USER");
        }
    }
}
