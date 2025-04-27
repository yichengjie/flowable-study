package org.example.core.approve;

import lombok.extern.slf4j.Slf4j;
import org.example.HelloWorldApplication;
import org.example.enums.DynamicProcessType;
import org.example.enums.ProcessVariables;
import org.example.utils.CommonUtil;
import org.example.utils.JsonConverter;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest(classes = HelloWorldApplication.class)
public class DynamicApproveProcessHelperTest {

    @Autowired
    private RuntimeService runtimeService ;

    @Autowired
    private RepositoryService repositoryService ;

    @Autowired
    private TaskService taskService ;

    @Test
    void create(){
        List<TicketInfo> tickets = initTicketInfo() ;
        DynamicProcessType type = DynamicProcessType.REGION_APPROVE ;
        String xmlContent = DynamicApproveProcessHelper.create(tickets, type);
        log.info("xmlContent: {}", xmlContent);
    }

    @Test
    void deploy(){
        log.info("hello world");
        Deployment deploy = repositoryService.createDeployment()
            .addClasspathResource("process/hello-dynamic-approve.bpmn20.xml")
            .deploy();
        log.info("流程部署成功，部署id:{}", deploy.getId());
    }

    @Test
    void undeploy(){
        String definitionKey = "dynamic_region_approve";
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey(definitionKey)
            .list();
        if (CollectionUtils.isEmpty(list)){
            log.info("没有找到流程定义");
            return;
        }
        for (ProcessDefinition processDefinition : list) {
            log.info("流程定义id:{}", processDefinition.getId());
            repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
        }
    }

    @Test
    void start(){
        String traceId = CommonUtil.uuid() ;
        // prepare data
        List<TicketInfo> tickets =  initTicketInfo() ;
        Map<String, Object> variables = new HashMap<>();
        variables.put(ProcessVariables.TRACE_ID, traceId);
        variables.put(ProcessVariables.TICKETS, JsonConverter.serializeObject(tickets));
        //  start process
        DynamicProcessType type = DynamicProcessType.REGION_APPROVE ;
        String processInstanceKey = type.getCode() ;
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processInstanceKey, variables);
        // 流程实例id:3998694f-2182-11f0-99cf-c4c6e6281752
        log.info("流程实例id:{}", processInstance.getId());
    }

    @Test
    void complete(){
        // 33f3c769-2352-11f0-9e3e-d4f32dce9838
        // 3415f56c-2352-11f0-9e3e-d4f32dce9838
        // 1b73ce7c-2352-11f0-86c5-d4f32dce9838
        // 1b84e57f-2352-11f0-86c5-d4f32dce9838
        //  添加监听器
        taskService.complete("33f3c769-2352-11f0-9e3e-d4f32dce9838");
        taskService.complete("3415f56c-2352-11f0-9e3e-d4f32dce9838");
        //taskService.complete("f57439f3-233c-11f0-95c1-d4f32dce9838");
    }


    private List<TicketInfo> initTicketInfo() {
        return Arrays.asList(
            TicketInfo.builder().id("1").name("工单1").regionCode("hua_bei").build(),
            TicketInfo.builder().id("2").name("工单2").regionCode("hua_nan").build()
            //TicketInfo.builder().id("3").name("工单3").regionCode("xi_nan").build(),
            //TicketInfo.builder().id("4").name("工单4").regionCode("dong_bei").build()
        );
    }

}
