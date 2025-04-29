package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.example.HelloWorldApplication;
import org.example.model.bo.WfModelBo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@SpringBootTest(classes = HelloWorldApplication.class)
public class WfModelServiceTest {

    @Autowired
    private WfModelService modelService;

    @Test
    void insertModel(){
        WfModelBo modelBo = new WfModelBo();
        modelBo.setModelName("测试模型");
        modelBo.setModelKey("test-model-key");
        modelBo.setCategory("测试分类");
        modelService.insertModel(modelBo);
    }

    @Test
    void saveModel() throws IOException {
        ClassPathResource resource = new ClassPathResource("process/hello-dynamic-approve.bpmn20.xml");
        String bpmnXml = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
        String modelId = "3f1f4247-2497-11f0-8dea-d4f32dce9838";
        WfModelBo modelBo = new WfModelBo();
        modelBo.setModelId(modelId);
        modelBo.setBpmnXml(bpmnXml);
        modelService.saveModel(modelBo);
    }

    @Test
    void queryBpmnXmlById(){
        String modelId = "3f1f4247-2497-11f0-8dea-d4f32dce9838";
        String bpmnXml = modelService.queryBpmnXmlById(modelId);
        log.info("bpmnXml: {}", bpmnXml);
    }

}
