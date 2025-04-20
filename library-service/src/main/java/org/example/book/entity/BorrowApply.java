package org.example.book.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.Instant;

@Data
@TableName("borrow_apply")
public class BorrowApply {
    @TableId(type = IdType.ASSIGN_ID)
    @TableField(value = "id")
    private String id;
    @TableField("user_id")
    private String userId;
    @TableField("book_count")
    private Integer bookCount ;
    @TableField("apply_days")
    private Integer applyDays;
    @TableField("status")
    private String status;
    @TableField("create_time")
    private Instant createTime;
    @TableField("update_time")
    private Instant updateTime;
}
