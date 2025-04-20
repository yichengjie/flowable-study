package org.example.book.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class BorrowApplyRequest {

    @NotBlank(message = "申请人不能为空")
    private String userId;
    @NotNull(message = "申请数量不能为空")
    private Integer bookCount ;
    @NotNull(message = "申请天数不能为空")
    private Integer applyDays;

}
