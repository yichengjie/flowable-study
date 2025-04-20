package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HolidayRequest2Test {

    @Test
    void init(){
        this.initProcessEngine() ;
    }

    @Test
    void deploy(){
        ProcessEngine processEngine = initProcessEngine();
        // Deploy the process definition
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/holiday-request.bpmn20.xml")
                .addClasspathResource("process/holiday-request2.bpmn20.xml")
                .deploy();
    }

    @Test
    void undeploy(){
        String deploymentId = "1";
        ProcessEngine processEngine = initProcessEngine();
        processEngine.getRepositoryService()
                .deleteDeployment(deploymentId, true);
    }

    @Test
    void queryProcessDefinition(){
        ProcessEngine processEngine = initProcessEngine();
        // Deploy the process definition
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        Deployment deployment = repositoryService.createDeployment()
//                .addClasspathResource("process/holiday-request.bpmn20.xml")
//                .deploy();
        // Query the process definition
        String deployId = "" ;
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                //.deploymentId(deployment.getId())
                .processDefinitionKey("holiday-request2")
                .singleResult();
        System.out.println("Found process definition : " + processDefinition.getName());
    }


    @Test
    void startProcessByKey(){
        String employee = "John Doe";
        String processDefinitionKey = "holiday-request2" ;
        ProcessEngine processEngine = initProcessEngine();
        List<ProcessDefinition> list = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
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
        String businessKey = "10011" ;
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
    }


    @Test
    void queryTodoList(){
        String assignee = "Zhang San";
        ProcessEngine processEngine = initProcessEngine();
        List<Task> list = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee(assignee)
                .list();
        if (CollectionUtils.isEmpty(list)){
            System.out.println("没有待办任务");
        }
        for (Task task : list) {
           log.info("task : {}", task);
        }
    }

    @Test
    void createApplicationForm(){
        String taskId = "22507";
        Map<String, Object> variables = new HashMap<>();
        variables.put("result", "拒绝") ;
        variables.put("personnelApproval", "Zhang San") ;
        variables.put("managerApproval", "Li Si") ;
        variables.put("var1", "var1 value") ;
        variables.put("var2", "var2 value") ;

        ProcessEngine processEngine = initProcessEngine();
        processEngine.getTaskService()
                .complete(taskId, variables);
    }

    @Test
    void personnelReject(){
        // RuntimeService runtimeService = initProcessEngine().getRuntimeService();
        // runtimeService.setVariableLocal("12501", "personnelApproval", "Zhang San") ;
        Map<String, Object> variables = new HashMap<>();
        variables.put("result", "人事审批通过") ;
        String taskId = "25016";
        ProcessEngine processEngine = initProcessEngine();
        processEngine.getTaskService()
                .complete(taskId, variables);

    }

    @Test
    void personnelApprove(){
        Map<String, Object> variables = new HashMap<>();
        variables.put("comment", "经理审批拒绝") ;
        String taskId = "22516";
        ProcessEngine processEngine = initProcessEngine();
        processEngine.getTaskService()
                .complete(taskId, variables);
    }

    @Test
    void queryActInstanceHistory(){
        String processInstanceId = "22501";
        ProcessEngine processEngine = initProcessEngine();
        List<HistoricActivityInstance> list = processEngine.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                //.orderBy(HistoricActivityInstanceQueryProperty.END).desc()
                .list()
                .stream()
                .sorted((HistoricActivityInstance o1, HistoricActivityInstance o2) -> {
                    if (o1.getTransactionOrder() == null || o2.getTransactionOrder() == null) {
                        return 0;
                    }
                    return o1.getTransactionOrder().compareTo(o2.getTransactionOrder());
                }).toList() ;
        if (CollectionUtils.isEmpty(list)){
            System.out.println("没有历史任务");
        }
        for (HistoricActivityInstance task : list) {
            log.info("task : {}", task);
        }
    }

    private ProcessEngine initProcessEngine(){
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://localhost:3306/flowable?serverTimezone=UTC")
                .setJdbcUsername("root")
                .setJdbcPassword("root")
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        return cfg.buildProcessEngine();
    }


}
