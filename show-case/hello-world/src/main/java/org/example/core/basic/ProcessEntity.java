package org.example.core.basic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessEntity {
    @NotBlank(message = "流程名称不能为空")
    private String type ;
    private List<ProcessNode> processNodeList;
}
