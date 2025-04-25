package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.HelloWorldApplication;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest(classes = HelloWorldApplication.class)
public class HelloDynamicApproveTest {

    @Autowired
    private RepositoryService repositoryService ;


    @Test
    void deploy(){
        String deployId = repositoryService.createDeployment()
            .addClasspathResource("process/hello-dynamic-approve.bpmn20.xml")
            .deploy()
            .getId();
        System.out.println("deployId = " + deployId);
    }


    @Test
    void undeploy(){
        String processDefinitionKey = "region_approve_cd1be4416fa441dfab0987a6cb26e602" ;
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey(processDefinitionKey)
            .list();
        for (ProcessDefinition item : list) {
            String deploymentId = item.getDeploymentId();
            repositoryService.deleteDeployment(deploymentId, true);
        }
    }

}
