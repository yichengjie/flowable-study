package org.example.core.basic;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
