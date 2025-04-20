package org.example.book.service;

import lombok.RequiredArgsConstructor;
import org.example.book.entity.BorrowApply;
import org.example.book.enums.ApplyStatusEnum;
import org.example.book.enums.ApproveLevelEnum;
import org.example.book.mapper.BorrowApplyMapper;
import org.example.book.model.convertor.BorrowApplyConvertor;
import org.example.book.model.request.BorrowApplyRequest;
import org.example.book.model.dto.BorrowApplyDTO;
import org.example.book.utils.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BorrowApplyService {

    private final BorrowApplyMapper bookBorrowApplyMapper;


    @Transactional(rollbackFor = Exception.class)
    public BorrowApplyDTO create(BorrowApplyRequest request){
        BorrowApply apply = new BorrowApply();
        apply.setId(CommonUtil.uuid());
        apply.setUserId(request.getUserId());
        apply.setBookCount(request.getBookCount());
        apply.setApplyDays(request.getApplyDays());
        apply.setStatus(ApplyStatusEnum.INIT.getCode());
        apply.setCreateTime(Instant.now());
        apply.setUpdateTime(Instant.now());
        bookBorrowApplyMapper.insert(apply) ;
        return BorrowApplyConvertor.I.toDTO(apply);
    }

    public void updateStatus(String id, String currentLevelCode) {
        ApproveLevelEnum approvalLevel = ApproveLevelEnum.of(currentLevelCode);
        if (approvalLevel == null) {
            throw new IllegalArgumentException("Invalid current level code: " + currentLevelCode);
        }
        ApplyStatusEnum status = CommonUtil.getStatusByApproveLevel(approvalLevel);
        if (status == null) {
            throw new IllegalArgumentException("Invalid status for approval level: " + currentLevelCode);
        }
        BorrowApply apply = new BorrowApply();
        apply.setId(id);
        apply.setStatus(status.getCode());
        apply.setUpdateTime(Instant.now());
        bookBorrowApplyMapper.updateById(apply) ;
    }


    public ApproveLevelEnum queryNextApproveLevel(String id, String currentLevelCode) {
        BorrowApply apply = bookBorrowApplyMapper.selectById(id) ;
        if (apply == null) {
            throw new IllegalArgumentException("申请不存在");
        }
        ApproveLevelEnum maxApprovalLevel = ApproveLevelEnum.getByCount(apply.getBookCount());
        // 如果levelCode为空，首次校验
        if (currentLevelCode == null) {
            // 返回首个需要审批的级别
            return CommonUtil.getNextApprovalLevel(ApproveLevelEnum.AUTO, maxApprovalLevel);
        }

        ApproveLevelEnum currentLevel = ApproveLevelEnum.of(currentLevelCode);
        if (currentLevel == null) {
            throw new IllegalArgumentException("Invalid approval level: " + currentLevelCode);
        }
        return CommonUtil.getNextApprovalLevel(currentLevel, maxApprovalLevel);
    }

}
