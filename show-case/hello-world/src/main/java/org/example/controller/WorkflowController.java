package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.request.WorkflowCompleteRequest;
import org.example.model.request.WorkflowCreateRequest;
import org.example.service.WorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/workflow")
public class WorkflowController {

    private final WorkflowService workflowService ;

    /**
     * 创建工作流
     * @param request request
     * @return 工作流实例ID
     */
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody WorkflowCreateRequest request){
        String processId = workflowService.create(request);
        return ResponseEntity.ok(processId);
    }

    @PostMapping("/complete")
    public ResponseEntity<String> complete(@RequestBody WorkflowCompleteRequest request){
        String ret = workflowService.complete(request);
        return ResponseEntity.ok(ret);
    }

}
