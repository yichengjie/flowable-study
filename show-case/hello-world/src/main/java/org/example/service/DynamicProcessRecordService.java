package org.example.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.example.entity.DynamicProcessRecord;
import org.example.enums.DynamicProcessType;
import org.example.repository.DynamicProcessRecordRepository;
import org.example.utils.CommonUtil;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DynamicProcessRecordService {

    private final DynamicProcessRecordRepository repository ;

    public String create(
        DynamicProcessType processType,
        String businessKey,
        String processDefinitionId){
        DynamicProcessRecord record = new DynamicProcessRecord();
        record.setId(CommonUtil.uuid());
        record.setProcessType(processType.getCode());
        record.setBusinessKey(businessKey);
        record.setProcessDefinitionId(processDefinitionId);
        record.setCreateTime(Instant.now());
        record.setUpdateTime(Instant.now());
        repository.save(record) ;
        return record.getId();
    }

    public List<DynamicProcessRecord> listByProcessType(DynamicProcessType processType){
        LambdaQueryWrapper<DynamicProcessRecord> wrapper = Wrappers.<DynamicProcessRecord>lambdaQuery()
            .eq(DynamicProcessRecord::getProcessType, processType.getCode());
        return repository.list(wrapper) ;
    }

}
