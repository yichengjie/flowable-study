package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HolidayRequest1Test {

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
                .deploy();
    }

    @Test
    void query(){
        ProcessEngine processEngine = initProcessEngine();
        // Deploy the process definition
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/holiday-request.bpmn20.xml")
                .deploy();
        // Query the process definition
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        System.out.println("Found process definition : " + processDefinition.getName());
    }


    @Test
    void startByKey(){
        String employee = "John Doe";
        String nrOfHolidays = "5";
        String description = "Holiday request for John Doe";
        String assignee = "Zhang San";
        ProcessEngine processEngine = initProcessEngine();
        List<ProcessDefinition> list = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey("holidayRequest")
                .latestVersion()
                .list();

        for (ProcessDefinition processDefinition : list) {
            System.out.println("Found process definition : " + processDefinition.getName());
        }
        // -------------------------------------------------------------
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employee", employee);
        variables.put("nrOfHolidays", nrOfHolidays);
        variables.put("description", description);
        variables.put("assignee", assignee);
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey("holidayRequest", variables);
    }

    @Test
    void startById(){
        String employee = "John Doe";
        String nrOfHolidays = "5";
        String description = "Holiday request for John Doe";
        String assignee = "Zhang San";
        ProcessEngine processEngine = initProcessEngine();
        List<ProcessDefinition> list = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey("holidayRequest")
                .latestVersion()
                .list();

        for (ProcessDefinition processDefinition : list) {
            System.out.println("Found process definition : " + processDefinition.getName());
        }
        // personnelApproval
        // managerApproval
        // -------------------------------------------------------------
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<>();
        variables.put("employee", employee);
        variables.put("nrOfHolidays", nrOfHolidays);
        variables.put("description", description);
        variables.put("assignee", assignee);
        String instanceById = list.get(0).getId();
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceById("holidayRequest:1:5004", variables);
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
    void personnelReject(){
        Map<String, Object> variables = new HashMap<>();
        variables.put("result", "拒绝") ;
        String taskId = "25006";
        ProcessEngine processEngine = initProcessEngine();
        processEngine.getTaskService()
                .complete(taskId, variables);

    }

    @Test
    void personnelApprove(){
        Map<String, Object> variables = new HashMap<>();
        variables.put("result", "通过") ;
        String taskId = "12511";
        ProcessEngine processEngine = initProcessEngine();
        processEngine.getTaskService()
                .complete(taskId, variables);
    }

    @Test
    void queryActInstanceHistory(){
        String processInstanceId = "12501";
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
