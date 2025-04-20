package org.example.book.feign.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowCompleteRequest {

    private String taskId ;

    @Builder.Default
    private Map<String, Object> variables = new HashMap<>();
}
