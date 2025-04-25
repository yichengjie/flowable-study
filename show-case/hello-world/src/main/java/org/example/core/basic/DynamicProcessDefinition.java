package org.example.core.basic;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.springframework.stereotype.Component;
import org.flowable.bpmn.model.Process;
import java.util.List;

/**
 * 动态创建流程模板
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicProcessDefinition {

    //private final RepositoryService repositoryService;


    public String createProcessDefinition(ProcessEntity processEntity) {
        //数据校验：传入的数据节点字段：流程配置id、节点id、节点顺序、节点描述
        List<ProcessNode> nodeList = processEntity.getProcessNodeList();
        if (CollUtil.isEmpty(nodeList)) {
            throw new RuntimeException("流程节点不能少于一个，请配置发起人节点和至少一个审批节点");
        }
        BpmnModel bpmnModel = new BpmnModel();
        Process process = new Process();
        bpmnModel.addProcess(process);
        // 流程标识
        process.setId(TypeEnum.getProcessKey(processEntity.getType()));
        process.setName(TypeEnum.getProcessKey(processEntity.getType()));
        //开始事件
        FlowElement startEvent = FlowableBpmnHandler.createStartFlowElement("start", "开始");
        process.addFlowElement(startEvent);
        //结束事件--任务正常完成
        FlowElement endEvent = FlowableBpmnHandler.createEndFlowElement("end", "结束");
        process.addFlowElement(endEvent);
        //创建用户节点任务
        List<UserTask> userTaskList = FlowableBpmnHandler.createCommonUserTask(processEntity);
        //构建流程模板
        buildProcessTemplate(process, startEvent, endEvent, userTaskList);
        //该流程的流程xml字符串
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        byte[] convertToXML = bpmnXMLConverter.convertToXML(bpmnModel);
        return new String(convertToXML);
        //log.info("该流程的流程xml为：{}", bytes);
        // 创建一个新的部署
//        Deployment deploy = repositoryService.createDeployment().key(TypeEnum.getProcessKey(processEntity.getType()))
//                .name(TypeEnum.getProcessKey(processEntity.getType()))
//                .addBpmnModel(TypeEnum.getProcessKey(processEntity.getType()) + ".bpmn", bpmnModel)
//                .deploy();
//        log.info("部署id:{}", deploy.getId());
//        return deploy.getId();
    }

    /**
     * 构建流程模板
     *
     * @param process
     * @param startEvent
     * @param endEvent
     * @param userTaskList
     * @return void
     * @author ll
     * @date 2023/04/24 08:53
     */
    private void buildProcessTemplate(Process process, FlowElement startEvent, FlowElement endEvent, List<UserTask> userTaskList) {
        for (int i = 0; i < userTaskList.size(); i++) {
            //用户任务
            UserTask userTask = userTaskList.get(i);
            process.addFlowElement(userTask);
            //创建排它网关节点
            ExclusiveGateway exclusiveGateway = FlowableBpmnHandler.createExclusiveGateway(i);
            process.addFlowElement(exclusiveGateway);
            //开始事件到第一个节点
            if (i == 0) {
                // 开始节点到第一级节点
                SequenceFlow startSequenceFlow = new SequenceFlow(startEvent.getId(), userTask.getId());
                process.addFlowElement(startSequenceFlow);
            }
            //用户任务到网关节点
            SequenceFlow sequenceFlow = new SequenceFlow(userTask.getId(), exclusiveGateway.getId());
            sequenceFlow.setName(userTask.getName() + "_开始审批");
            process.addFlowElement(sequenceFlow);
            //同意：取下一步用户任务的节点id，可能为结束节点或者用户任务节点
            String nextUserTaskId = endEvent.getId();
            if (userTaskList.size() - i > 1) {
                nextUserTaskId = userTaskList.get(i + 1).getId();
            }
            SequenceFlow sequenceFlowAgree = new SequenceFlow(exclusiveGateway.getId(), nextUserTaskId);
            sequenceFlowAgree.setConditionExpression("${auditResult}");
            sequenceFlowAgree.setName("同意");
            process.addFlowElement(sequenceFlowAgree);
            //不同意：回退到上一步,取上一步的节点id
            String preUserTaskId = userTaskList.get(0).getId();
            if (i != 0) {
                preUserTaskId = userTaskList.get(i - 1).getId();
            }
            SequenceFlow sequenceFlowRefuse = new SequenceFlow(exclusiveGateway.getId(), preUserTaskId);
            sequenceFlowRefuse.setConditionExpression("${!auditResult}");
            sequenceFlowRefuse.setName("拒绝");
            process.addFlowElement(sequenceFlowRefuse);
        }
    }
}
