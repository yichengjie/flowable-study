package org.example.core.basic;

import lombok.Data;

@Data
public class ProcessNode {

    private String nodeId;

    private String nodeName;

    private int nodeLevel;

    public ProcessNode(String nodeName){
        this.nodeName = nodeName;
    }
}
