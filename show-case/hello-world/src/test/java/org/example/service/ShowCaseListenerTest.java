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
public class ShowCaseListenerTest {

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
            .addClasspathResource("process/show-case-listener.bpmn20.xml")
            .deploy();
    }

    @Test
    void undeploy(){
        String deploymentId = "1";
        repositoryService.deleteDeployment(deploymentId, true);
    }

    @Test
    void startProcessByKey(){
        String employee = "John Doe";
        String processDefinitionKey = "show-case-listener" ;
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .latestVersion()
                .list();
        for (ProcessDefinition processDefinition : list) {
            System.out.println("Found process definition : " + processDefinition.getName());
        }
        // -------------------------------------------------------------
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<>();
        variables.put("employee", employee);
        runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
    }

    @Test
    void completeById(){
        String taskId = "2d9e76e6-1c26-11f0-b7e7-d4f32dce9838";
        Map<String,Object> variables = new HashMap<>();
        variables.put("manager", "张三");
        taskService.complete(taskId, variables);
    }

    @Test
    void managerApprove(){
        String taskId = "55ea78fd-1c26-11f0-a4cd-d4f32dce9838";
        taskService.complete(taskId);
    }

}
