package org.example.book.model.dto;


import lombok.Data;

import java.time.Instant;

@Data
public class BorrowApplyDTO {
    private String id;
    private String userId;
    private Integer bookCount ;
    private Integer applyDays;
    private String status;
    private Instant createTime;
    private Instant updateTime;
}
