package org.example.core.basic;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.utils.UUIDHelper;
import org.flowable.bpmn.model.*;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 动态创建流程节点
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FlowableBpmnHandler {
    /**
     * 创建开始节点信息
     */
    public static FlowElement createStartFlowElement(String id, String name) {
        StartEvent startEvent = new StartEvent();
        startEvent.setId(id);
        startEvent.setName(name);
        return startEvent;
    }

    /**
     * 创建结束节点信息
     */
    public static FlowElement createEndFlowElement(String id, String name) {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(id);
        endEvent.setName(name);
        return endEvent;
    }

    /**
     * 循环创建普通任务节点信息
     * @param processEntity 流程实体
     */
    public static List<UserTask> createCommonUserTask(ProcessEntity processEntity) {
        List<ProcessNode> processNodes = processEntity.getProcessNodeList();
        List<UserTask> userTaskList = Lists.newLinkedList();
        for (int i = 0; i < processNodes.size(); i++) {
            ProcessNode node = processNodes.get(i);
            node.setNodeId(UUIDHelper.randomUUID());
            node.setNodeLevel(i + 1);
            UserTask userTask = new UserTask();
            userTask.setId(TypeEnum.getProcessKey(processEntity.getType()) + "_task_" + node.getNodeId());
            userTask.setCategory(String.valueOf(i + 1));
            userTask.setDocumentation(i == processNodes.size() - 1 ? "true" : "false");
            userTask.setName(node.getNodeName());
            userTaskList.add(userTask);
        }
        return userTaskList;
    }


    /**
     * 创建排它网关节点
     */
    public static ExclusiveGateway createExclusiveGateway(Integer i) {
        ExclusiveGateway gateway = new ExclusiveGateway();
        gateway.setName("getaway_name_" + i);
        gateway.setId("getaway_id_" + i);
        return gateway;
    }


    public static ParallelGateway createParallelGateway(Integer i) {
        ParallelGateway gateway = new ParallelGateway();
        gateway.setName("getaway_name_" + i);
        gateway.setId("getaway_id_" + i);
        return gateway;
    }
}
