package org.example.controller;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.core.basic.DynamicProcessDefinition;
import org.example.core.basic.ProcessEntity;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/flowable")
public class FlowableController {

    private final DynamicProcessDefinition dynamicProcessDefinition;

    private final RuntimeService runtimeService;

    @RequestMapping("/create")
    public ResponseEntity<String> save(@Valid @RequestBody ProcessEntity processEntity) {
        String definitionId = dynamicProcessDefinition.createProcessDefinition(processEntity);
        return ResponseEntity.ok(definitionId);
    }


    @RequestMapping("/start")
    public String start(@Valid @RequestBody AuditData auditData) {
        //业务自己定义key
        String businessKey = auditData.getBusinessKey();
        //流程定义key
        String processKey = auditData.getType();
        // 判断是否启动流程
        boolean isStartProcess = null != runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processKey)
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        if (!isStartProcess) {
            //记录开启流程的用户
            HashMap<String, Object> variable = Maps.newHashMap();
            //启动流程
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, businessKey, variable);
            log.info("启动流程实例成功:processInstanceId={}", processInstance.getId());

        }
        return !isStartProcess ? "流程启动成功" : "流程启动失败";
    }


    @Data
    public static class AuditData {
        @NotBlank(message = "业务key不能为空")
        private String businessKey;
        @NotBlank(message = "流程类型不能为空")
        private String type;
    }

}
