package org.example.book.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.book.enums.ApproveLevelEnum;
import org.example.book.model.request.BorrowApplyRequest;
import org.example.book.model.dto.BorrowApplyDTO;
import org.example.book.service.BorrowApplyService;
import org.example.book.service.WorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/borrow/apply")
public class BorrowApplyController {

    private final BorrowApplyService borrowApplyService;
    private final WorkflowService workflowService;

    @PostMapping("/create")
    public ResponseEntity<BorrowApplyDTO> create(@Validated @RequestBody BorrowApplyRequest request){
        // 1. 新建申请记录
        BorrowApplyDTO dto = borrowApplyService.create(request);
        // 2. 开启工作流
        String processId = workflowService.create(dto) ;
        log.info("processId = {}", processId);
        return ResponseEntity.ok(dto);
    }

    /**
     * 更新借阅申请进度
     * @param id 申请id
     * @param currentLevelCode 当前审批级别
     */
    @GetMapping("/update/status")
    public ResponseEntity<String> updateStatus(
            @RequestParam("id") String id,
            @RequestParam("currentLevelCode") String currentLevelCode){
        borrowApplyService.updateStatus(id, currentLevelCode);
        return ResponseEntity.ok("success");
    }

    /**
     * 校验状态
     */
    @GetMapping("/query/approve-level")
    public ResponseEntity<String> queryApproveLevel(
            @RequestParam("id") String id,
            @RequestParam(value = "currentLevelCode", required = false) String currentLevelCode){
        ApproveLevelEnum nextApproveLevel = borrowApplyService.queryNextApproveLevel(id, currentLevelCode);
        return ResponseEntity.ok(nextApproveLevel != null ? nextApproveLevel.getCode() : null);
    }


    @GetMapping("/approve/{taskId}")
    public ResponseEntity<String> approve(@PathVariable ("taskId") String taskId){
        workflowService.complete(taskId);
        return ResponseEntity.ok("success");
    }

}
