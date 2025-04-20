package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.HelloWorldApplication;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@SpringBootTest(classes = HelloWorldApplication.class)
public class BookBorrowApplyTest {

    @Autowired
    private RepositoryService repositoryService ;

    @Test
    void deploy(){
        repositoryService.createDeployment()
                .addClasspathResource("process/book-borrow-apply.bpmn20.xml")
                .deploy();
    }

    @Test
    void undeploy(){
        // 查询部署信息
        // 根据processDefinitionKey 查询 流程定义
        String processDefinitionKey = "book-borrow-apply";
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();
        if (CollectionUtils.isEmpty(list)) {
            log.info("流程定义不存在，无法删除");
            return;
        }
        for (ProcessDefinition processDefinition : list) {
            // 根据部署id 删除部署信息
            String deploymentId = processDefinition.getDeploymentId();
            // 危险动作，会删除history中之前的所有记录数据
            repositoryService.deleteDeployment(deploymentId, true);
        }
    }

}
