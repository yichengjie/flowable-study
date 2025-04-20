package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.request.WorkflowCompleteRequest;
import org.example.model.request.WorkflowCreateRequest;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final RuntimeService runtimeService;

    private final TaskService taskService;

    public String create(WorkflowCreateRequest request) {
        // 启动流程实例
        String processId = this.startProcess(request);
        // 提交借阅申请
        this.createTicket(request, processId);
        // 返回流程实例ID
        return processId;
    }


    private void createTicket(WorkflowCreateRequest request, String processId){
        Task task = taskService.createTaskQuery()
                .processInstanceId(processId)
                .singleResult();
        String id = task.getId();
        // 将businessKey 存放到流程变量中, 方便后续流程中直接使用该变量
        taskService.complete(id);
    }


    private String startProcess(WorkflowCreateRequest request){
        // 启动流程实例
        String createUser = request.getCreateUser();
        String businessKey = request.getBusinessKey();
        String processDefinitionKey = request.getProcessDefinitionKey();
        Map<String, Object> variables = request.getVariables();
        variables.put("createUser", createUser);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
            processDefinitionKey,
            businessKey,
            variables
        );
        runtimeService.setVariable(processInstance.getId(), "businessKey", businessKey);
        return processInstance.getId();
    }

    public String complete(WorkflowCompleteRequest request) {
        // 完成任务
        String taskId = request.getTaskId();
        Map<String, Object> variables = request.getVariables();
        taskService.complete(taskId, variables);
        return taskId;
    }

}
