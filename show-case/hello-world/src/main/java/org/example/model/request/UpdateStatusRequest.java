package org.example.model.request;

import lombok.Data;
import org.example.core.approve.TicketInfo;

import java.util.List;

@Data
public class UpdateStatusRequest {

    private String traceId ;
    private String status;
    private List<TicketInfo> tickets ;

}
