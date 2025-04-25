package org.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.Instant;

@Data
@TableName("dynamic_process_record")
public class DynamicProcessRecord {
    @TableField("id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id ;
    // 流程类型(区域审批、请假审批等)
    @TableField("process_type")
    private String processType ;
    // 业务主键数据
    @TableField("business_key")
    private String businessKey ;
    @TableField("process_definition_id")
    private String processDefinitionId ;
    @TableField("create_time")
    private Instant createTime ;
    @TableField("update_time")
    private Instant updateTime ;
}
