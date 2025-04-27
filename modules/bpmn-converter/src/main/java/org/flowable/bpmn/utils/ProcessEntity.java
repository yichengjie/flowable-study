package org.flowable.bpmn.utils;

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
    private String type ;
    private List<ProcessNode> processNodeList;
}
