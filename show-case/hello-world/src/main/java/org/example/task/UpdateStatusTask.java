package org.example.task;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.HashMap;

@Slf4j
public class UpdateStatusTask implements JavaDelegate {
    private Expression parameters ;
    private Expression url ;
    private Expression expression ;

    @Override
    public void execute(DelegateExecution execution) {
        String address = url.getExpressionText() ;
        String businessKey = (String) execution.getVariable("businessKey");
        String currentLevelCode = (String) execution.getVariable("currentLevelCode");
        // 1. 使用hutool 发送http请求
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", businessKey);
        paramMap.put("currentLevelCode", currentLevelCode);
        String result = HttpUtil.get(address, paramMap);
        log.info("result = {}", result);
    }
}
