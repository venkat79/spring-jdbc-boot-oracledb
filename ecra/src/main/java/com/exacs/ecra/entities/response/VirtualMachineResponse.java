package com.exacs.ecra.entities.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public class VirtualMachineResponse {

    private long id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String nodeName;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
