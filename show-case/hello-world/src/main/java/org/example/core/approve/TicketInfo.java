package org.example.core.approve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketInfo {

    /**
     * 工单ID
     */
    private String id ;
    /**
     * 工单名称
     */
    private String name;
    /**
     * 工单大区
     */
    private String regionCode ;

}
