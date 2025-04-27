package org.example.hello;

import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.utils.CommonUtil;
import org.flowable.bpmn.utils.FlowableBpmnHandler;
import org.junit.jupiter.api.Test;

@Slf4j
public class HelloWorldTest {

    @Test
    void helloWorld(){
        BpmnModel bpmnModel = new BpmnModel();
        Process process = new Process();
        bpmnModel.addProcess(process);
        // 流程标识
        String processKey = CommonUtil.uuid() ;
        process.setId(processKey);
        process.setName(processKey);
        //开始事件
        FlowElement startEvent = FlowableBpmnHandler.createStartFlowElement("start", "开始");
        process.addFlowElement(startEvent);

        //Prepare data service
        ServiceTask prepareDataService = new ServiceTask();
        prepareDataService.setId(CommonUtil.uuid());
        prepareDataService.setName("预处理服务");
        prepareDataService.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
        prepareDataService.setImplementation("org.example.task.PrepareDataServiceTask");

        // 开始事件 ===> 预处理服务 sequenceFlow
        SequenceFlow startSequenceFlow = new SequenceFlow(startEvent.getId(), prepareDataService.getId());
        process.addFlowElement(startSequenceFlow);
        process.addFlowElement(prepareDataService);

        // 预处理服务 ===> 结束 sequenceFlow
        //结束事件--任务正常完成
        FlowElement endEvent = FlowableBpmnHandler.createEndFlowElement("end", "结束");
        SequenceFlow endSequenceFlow = new SequenceFlow(prepareDataService.getId(), endEvent.getId());
        process.addFlowElement(endSequenceFlow);
        process.addFlowElement(endEvent);

        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        byte[] convertToXML = bpmnXMLConverter.convertToXML(bpmnModel);
        String content = new String(convertToXML);
        log.info("流程xml :{}", content);
    }
}
