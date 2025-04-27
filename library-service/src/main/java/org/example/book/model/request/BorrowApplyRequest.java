package org.example.book.model.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BorrowApplyRequest {

    @NotBlank(message = "申请人不能为空")
    private String userId;
    @NotNull(message = "申请数量不能为空")
    private Integer bookCount ;
    @NotNull(message = "申请天数不能为空")
    private Integer applyDays;

}
