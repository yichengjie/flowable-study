package org.example.core;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.example.HelloWorldApplication;
import org.example.controller.FlowableController;
import org.example.core.basic.DynamicProcessDefinition;
import org.example.core.basic.ProcessEntity;
import org.example.core.basic.ProcessNode;
import org.example.core.basic.TypeEnum;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;

@Slf4j
@SpringBootTest(classes = HelloWorldApplication.class)
public class DynamicProcessDefinitionTest {
    @Autowired
    private DynamicProcessDefinition dynamicProcessDefinition;

    @Autowired
    private RuntimeService runtimeService ;

    @Autowired
    private RepositoryService repositoryService ;


    @Test
    void deploy(){
        log.info("hello world");
        Deployment deploy = repositoryService.createDeployment()
            .addClasspathResource("process/hello-world-dynamic.bpmn20.xml")
            .deploy();
        log.info("流程部署成功，部署id:{}", deploy.getId());
    }

    @Test
    void start(){
        String processInstanceKey = "process_HELLO_WORLD_DYNAMIC";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processInstanceKey);
        // 流程实例id:3998694f-2182-11f0-99cf-c4c6e6281752
        log.info("流程实例id:{}", processInstance.getId());
    }



    @Test
    void createProcessDefinition(){
        ProcessEntity processEntity = new ProcessEntity();
        processEntity.setType(TypeEnum.HELLO_WORLD_DYNAMIC.getType());
        processEntity.setProcessNodeList(
            Arrays.asList(
                new ProcessNode("普通任务1"),
                new ProcessNode("普通任务2"),
                new ProcessNode("普通任务3"),
                new ProcessNode("普通任务4"),
                new ProcessNode("普通任务5"),
                new ProcessNode("普通任务6")
            )
        );
        String xmlContent = dynamicProcessDefinition.createProcessDefinition(processEntity);
        log.info("流程xml :{}", xmlContent);
    }

    @Test
    void startProcessInstanceByKey(){
        FlowableController.AuditData auditData = new FlowableController.AuditData();
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
        String ret = !isStartProcess ? "流程启动成功" : "流程启动失败";
        log.info("ret={}", ret);
    }
}
