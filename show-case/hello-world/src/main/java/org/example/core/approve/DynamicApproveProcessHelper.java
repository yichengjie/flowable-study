package org.example.core.approve;

import org.example.core.basic.FlowableBpmnHandler;
import org.example.enums.DynamicProcessType;
import org.example.utils.CommonUtil;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DynamicApproveProcessHelper {

    public static String create(List<TicketInfo> tickets, DynamicProcessType type){
        if (CollectionUtils.isEmpty(tickets)){
            return null;
        }
        BpmnModel bpmnModel = new BpmnModel();
        Process process = new Process();
        bpmnModel.addProcess(process);
        // 流程标识
        String processKey = type.getCode() + "_" +CommonUtil.uuid() ;
        process.setId(processKey);
        process.setName(processKey);
        //开始事件
        FlowElement startEvent = FlowableBpmnHandler.createStartFlowElement("start", "开始");
        process.addFlowElement(startEvent);

        //Prepare data service
        ServiceTask prepareDataService = new ServiceTask();
        prepareDataService.setId(CommonUtil.uuid());
        prepareDataService.setName("预处理服务");
        prepareDataService.setType("org.example.task.PrepareDataServiceTask");

        // 开始事件 ===> 预处理服务 sequenceFlow
        SequenceFlow startSequenceFlow = new SequenceFlow(startEvent.getId(), prepareDataService.getId());
        process.addFlowElement(startSequenceFlow);
        process.addFlowElement(prepareDataService);

        // 创建并行网关
        ParallelGateway startParallelGateway = FlowableBpmnHandler.createParallelGateway(1);
        String startParallelGatewayId = startParallelGateway.getId();

        // 添加预处理服务 ====> 其实网关 sequenceFlow
        SequenceFlow prepareDataSequenceFlow = new SequenceFlow(prepareDataService.getId(), startParallelGatewayId);
        process.addFlowElement(prepareDataSequenceFlow);
        process.addFlowElement(startParallelGateway);

        // end 网关
        ParallelGateway endParallelGateway = FlowableBpmnHandler.createParallelGateway(2);
        String endParallelGatewayId = endParallelGateway.getId();

        // 按照区域将数据分组
        Map<String, List<TicketInfo>> groupTicketMap = CommonUtil.list2MapList(tickets, TicketInfo::getRegionCode);
        for (Map.Entry<String, List<TicketInfo>> entry : groupTicketMap.entrySet()) {
            String regionCode = entry.getKey();
            // 创建区域对应的节点
            ServiceTask regionServiceTask = new ServiceTask();
            regionServiceTask.setId("region_service_task_id_" + regionCode);
            regionServiceTask.setName(regionCode);
            // "flowable:class", regionCode
            CustomProperty customProperty = new CustomProperty();
            customProperty.setName("flowable:class");
            customProperty.setSimpleValue("org.example.task.RegionServiceTask");
            regionServiceTask.setCustomProperties(List.of(customProperty));
            // 审核人
            UserTask firstUserTask = new UserTask();
            firstUserTask.setId("region_user_task_first_reviewer_id_" + regionCode);
            firstUserTask.setName("审核人");
            // task assignee
            firstUserTask.setAssignee("${first_reviewer_" + regionCode + "}");

            // 二级审核人
            UserTask secondaryUserTask = new UserTask();
            secondaryUserTask.setId("region_user_task_secondary_reviewer_id_" + regionCode);
            secondaryUserTask.setName("二级审核人");
            secondaryUserTask.setAssignee("${secondary_reviewer_" + regionCode + "}");

            // COE总部审核人
            UserTask coeUserTask = new UserTask();
            coeUserTask.setId("region_user_task_coe_reviewer_id_" + regionCode);
            coeUserTask.setName("COE总部");
            coeUserTask.setAssignee("${coe_reviewer_" + regionCode + "}");

            // start网关 ====> 大区服务 sequenceFlow
            SequenceFlow startGatewaySequence = new SequenceFlow(startParallelGatewayId, regionServiceTask.getId());
            process.addFlowElement(startGatewaySequence);
            process.addFlowElement(regionServiceTask);

            // 大区服务 ====> 审核人 sequenceFlow
            SequenceFlow reviewerSequenceFlow = new SequenceFlow(regionServiceTask.getId(), firstUserTask.getId());
            process.addFlowElement(reviewerSequenceFlow);
            process.addFlowElement(firstUserTask);
            // 审核人 ====> 二级审核人 sequenceFlow

            SequenceFlow secondaryReviewerSequenceFlow = new SequenceFlow(firstUserTask.getId(), secondaryUserTask.getId());
            process.addFlowElement(secondaryReviewerSequenceFlow);
            process.addFlowElement(secondaryUserTask);

            // 二级审核人 ===> COE审核 sequenceFlow
            SequenceFlow coeReviewerSequenceFlow = new SequenceFlow(secondaryUserTask.getId(), coeUserTask.getId());
            process.addFlowElement(coeReviewerSequenceFlow);
            process.addFlowElement(coeUserTask);

            // COE审核 ===> 结束网关 sequenceFlow
            SequenceFlow endGatewaySequence = new SequenceFlow(coeUserTask.getId(), endParallelGatewayId);
            process.addFlowElement(endGatewaySequence);
        }
        // 添加结束网关
        process.addFlowElement(endParallelGateway);
        // 添加数据更新任务
        ServiceTask dataUpdateTask = new ServiceTask();
        dataUpdateTask.setId("data_update_service_task_id");
        dataUpdateTask.setName("数据更新API");
        dataUpdateTask.setExtensionId("org.example.task.DataUpdateServiceTask");
        // 结束网关 ===> 数据更新服务  sequenceFlow
        SequenceFlow endGatewaySequenceFlow = new SequenceFlow(endParallelGatewayId, dataUpdateTask.getId());
        process.addFlowElement(endGatewaySequenceFlow);
        process.addFlowElement(dataUpdateTask);

        // 数据更新服务 ===> 结束 sequenceFlow
        //结束事件--任务正常完成
        FlowElement endEvent = FlowableBpmnHandler.createEndFlowElement("end", "结束");
        SequenceFlow endSequenceFlow = new SequenceFlow(dataUpdateTask.getId(), endEvent.getId());
        process.addFlowElement(endSequenceFlow);
        process.addFlowElement(endEvent);

        //该流程的流程xml字符串
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        byte[] convertToXML = bpmnXMLConverter.convertToXML(bpmnModel);
        return new String(convertToXML);
    }


}
