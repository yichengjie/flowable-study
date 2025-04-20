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
public class WorkflowCreateRequest {

    private String createUser;

    private String businessKey;

    private String processDefinitionKey;

    @Builder.Default
    private Map<String, Object> variables = new HashMap<>();
}
