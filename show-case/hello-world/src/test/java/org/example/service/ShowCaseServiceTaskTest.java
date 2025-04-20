package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.HelloWorldApplication;
import org.flowable.engine.*;
import org.flowable.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest(classes = HelloWorldApplication.class)
public class ShowCaseServiceTaskTest {

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private RepositoryService repositoryService ;
    @Autowired
    private RuntimeService runtimeService ;
    @Autowired
    private TaskService taskService ;
    @Autowired
    private HistoryService historyService ;

    @Test
    void deploy(){
        repositoryService.createDeployment()
            .addClasspathResource("process/show-case-service-task.bpmn20.xml")
            .deploy();
    }

    @Test
    void undeploy(){
        String deploymentId = "1";
        // 危险动作，会删除history中之前的所有记录数据
        repositoryService.deleteDeployment(deploymentId, true);
    }

    @Test
    void startProcessByKey(){
        String processDefinitionKey = "show-case-service-task" ;
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .latestVersion()
                .list();
        for (ProcessDefinition processDefinition : list) {
            System.out.println("Found process definition : " + processDefinition.getName());
        }
        // -------------------------------------------------------------
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey(processDefinitionKey);
    }

    @Test
    void completeById(){
        String taskId = "2b6c959e-1c3b-11f0-a362-d4f32dce9838";
        Map<String,Object> variables = new HashMap<>();
        variables.put("manager", "张三");
        taskService.complete(taskId, variables);
    }

    @Test
    void managerApprove(){
        String taskId = "765f71b6-1c3b-11f0-b7e4-d4f32dce9838";
        taskService.complete(taskId);
    }

}
