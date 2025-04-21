package org.example.core;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ProcessEntity {
    @NotBlank(message = "流程名称不能为空")
    private String type ;
    private List<ProcessNode> processNodeList;
}
