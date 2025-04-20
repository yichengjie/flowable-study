package org.example.book.feign;

import org.example.book.feign.request.WorkflowCompleteRequest;
import org.example.book.feign.request.WorkflowCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "workflow", url = "${remote.workflow.url}")
public interface WorkflowFeignClient {


    /**
     * 创建工作流
     * @param request request
     * @return 工作流实例ID
     */
    @PostMapping("/api/v1/workflow/create")
    ResponseEntity<String> create(@RequestBody WorkflowCreateRequest request)  ;

    /**
     * 完成工作流
     * @param request request
     * @return 工作流实例ID
     */
    @PostMapping("/api/v1/workflow/complete")
    ResponseEntity<String> complete(@RequestBody WorkflowCompleteRequest request) ;

}