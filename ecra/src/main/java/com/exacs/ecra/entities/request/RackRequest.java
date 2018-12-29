package com.exacs.ecra.entities.request;

import com.exacs.ecra.entities.enums.RackType;

import java.util.HashSet;
import java.util.Set;

public class RackRequest {

    private String name;
    private RackType rackType;
    private Set<ComputeNodeRequest> nodes = new HashSet();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RackType getRackType() {
        return rackType;
    }

    public void setRackType(RackType rackType) {
        this.rackType = rackType;
    }

    public Set<ComputeNodeRequest> getNodes() {
        return nodes;
    }

    public void setNodes(Set<ComputeNodeRequest> nodes) {
        this.nodes = nodes;
    }


    @Override
    public String toString() {
        return "RackRequest{" +
                "name='" + name + '\'' +
                ", rackType=" + rackType +
                ", nodes=" + nodes +
                '}';
    }
}
