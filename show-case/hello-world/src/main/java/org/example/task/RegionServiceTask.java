package org.example.task;

import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

@Slf4j
public class RegionServiceTask implements JavaDelegate {
    private Expression regionCodeField; ;

    @Override
    public void execute(DelegateExecution execution) {
        String regionCodeValue = regionCodeField.getExpressionText();
        log.info("regionCodeValue = {}", regionCodeValue);
        //execution.setVariable("first_reviewer_" + regionCodeValue, "张三");
        //execution.setVariable("secondary_reviewer_" + regionCodeValue, "李四");
        //execution.setVariable("coe_reviewer_" + regionCodeValue, "王五");
    }
}
