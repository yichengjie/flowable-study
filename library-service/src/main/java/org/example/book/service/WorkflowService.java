package org.example.book.service;

import lombok.RequiredArgsConstructor;
import org.example.book.feign.WorkflowFeignClient;
import org.example.book.feign.request.WorkflowCompleteRequest;
import org.example.book.feign.request.WorkflowCreateRequest;
import org.example.book.model.dto.BorrowApplyDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowService {
    private final WorkflowFeignClient workflowFeignClient;

    public String create(BorrowApplyDTO dto) {
        String processDefinitionKey = "book-borrow-apply" ;
        WorkflowCreateRequest request = WorkflowCreateRequest.builder()
                .createUser(dto.getUserId())
                .businessKey(dto.getId())
                .processDefinitionKey(processDefinitionKey)
                .build();
        // 调用工作流引擎
        ResponseEntity<String> response = workflowFeignClient.create(request);
        return response.getBody();
    }

    public String complete(String taskId) {
        WorkflowCompleteRequest request = WorkflowCompleteRequest.builder()
                .taskId(taskId)
                .build();
        // 调用工作流引擎
        ResponseEntity<String> response = workflowFeignClient.complete(request);
        return response.getBody();
    }

}
