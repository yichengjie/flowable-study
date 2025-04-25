package org.example.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.DynamicProcessRecord;
import org.example.repository.mapper.DynamicProcessRecordMapper;
import org.example.repository.DynamicProcessRecordRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DynamicProcessRecordRepositoryImpl
    extends ServiceImpl<DynamicProcessRecordMapper, DynamicProcessRecord> implements DynamicProcessRecordRepository {

}
