package org.example.listener;

import cn.hutool.http.HttpUtil;
import org.example.utils.CommonUtil;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import java.util.HashMap;

public class RegionAssignTaskListener implements TaskListener {

    private Expression urlField ;
    private Expression parametersField ;

    private Expression expressionField ;

    @Override
    public void notify(DelegateTask delegateTask) {
        String address = urlField.getExpressionText() ;
        String parameters = parametersField.getExpressionText() ;
        String regionCode = CommonUtil.getParameter(parameters, "regionCode");
        String approveLevel = CommonUtil.getParameter(parameters, "approveLevel");
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("regionCode", regionCode);
        paramMap.put("approveLevel", approveLevel);
        // 发送请求
        String result = HttpUtil.get(address, paramMap);
        delegateTask.setAssignee(result);
    }

}
