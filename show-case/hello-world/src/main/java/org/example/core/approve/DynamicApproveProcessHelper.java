package org.example.core.approve;

import org.example.core.basic.FlowableBpmnHandler;
import org.example.enums.DynamicApproveLevel;
import org.example.enums.DynamicProcessType;
import org.example.enums.ProcessVariables;
import org.example.utils.CommonUtil;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.springframework.util.CollectionUtils;
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
        String processKey = type.getCode() ;
        process.setId(processKey);
        process.setName(processKey);
        //开始事件
        FlowElement startEvent = FlowableBpmnHandler.createStartFlowElement("start", "开始");
        process.addFlowElement(startEvent);

        //Prepare data service
        ServiceTask prepareDataService = new ServiceTask();
        prepareDataService.setId("prepare_" + CommonUtil.uuid());
        prepareDataService.setName("预处理服务");
        prepareDataService.setImplementationType("class");
        prepareDataService.setImplementation("org.example.task.PrepareDataServiceTask");

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
            regionServiceTask.setImplementationType("class");
            regionServiceTask.setImplementation("org.example.task.RegionServiceTask");
            FieldExtension regionCodeField = new FieldExtension();
            regionCodeField.setFieldName("regionCodeField");
            regionCodeField.setStringValue(regionCode);
            regionServiceTask.setFieldExtensions(
                List.of(regionCodeField)
            );
            // 审核人
            UserTask firstUserTask = new UserTask();
            firstUserTask.setId("region_user_task_first_reviewer_id_" + regionCode);
            firstUserTask.setName("审核人");
            // task assignee
            bindRegionAssignListener(firstUserTask, regionCode, DynamicApproveLevel.FIRST_REVIEWER);

            // 二级审核人
            UserTask secondaryUserTask = new UserTask();
            secondaryUserTask.setId("region_user_task_secondary_reviewer_id_" + regionCode);
            secondaryUserTask.setName("二级审核人");
            bindRegionAssignListener(secondaryUserTask, regionCode, DynamicApproveLevel.SECONDARY_REVIEWER);

            // COE总部审核人
            UserTask coeUserTask = new UserTask();
            coeUserTask.setId("region_user_task_coe_reviewer_id_" + regionCode);
            coeUserTask.setName("COE总部");
            bindRegionAssignListener(coeUserTask, regionCode, DynamicApproveLevel.COE_REVIEWER);

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
        dataUpdateTask.setImplementationType("class");
        dataUpdateTask.setImplementation("org.example.task.DataUpdateServiceTask");
        // 设置数据更新服务的参数
        addDataUpdateTaskField(dataUpdateTask);

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


    private static void bindRegionAssignListener(
        UserTask userTask,
        String regionCode,
        DynamicApproveLevel approveLevel){
        FlowableListener regionAssignListener = new FlowableListener();
        regionAssignListener.setEvent("create");
        regionAssignListener.setImplementationType("class");
        regionAssignListener.setImplementation("org.example.listener.RegionAssignTaskListener");
        //
        FieldExtension urlField = new FieldExtension();
        urlField.setFieldName("urlField");
        urlField.setStringValue("http://localhost:9090/api/v1/hello-world/query-region-assign");
        //
        FieldExtension parametersField = new FieldExtension();
        parametersField.setFieldName("parametersField");
        parametersField.setStringValue("regionCode=" + regionCode + "&approveLevel=" +approveLevel.getCode());
        //
        regionAssignListener.setFieldExtensions(
            List.of(urlField, parametersField)
        );
        userTask.setTaskListeners(
            List.of(regionAssignListener)
        );
    }

    private static void addDataUpdateTaskField(
        ServiceTask serviceTask){
        // url 字段
        FieldExtension urlField = new FieldExtension();
        urlField.setFieldName("urlField");
        urlField.setStringValue("http://localhost:9090/api/v1/hello-world/update-data");
        // parametersField 字段
        FieldExtension parametersField = new FieldExtension();
        parametersField.setFieldName("parametersField");
        parametersField.setStringValue(ProcessVariables.STATUS + "=COMPLETE");
        // expressionField 字段
        FieldExtension expressionField = new FieldExtension();
        expressionField.setFieldName("expressionField");
        expressionField.setStringValue("auditResult");

        serviceTask.setFieldExtensions(
            List.of(urlField, parametersField, expressionField)
        );

    }


}
