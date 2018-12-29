package com.exacs.ecra.entities.response;

import com.exacs.ecra.entities.enums.RackType;

import java.util.HashSet;
import java.util.Set;

public class RackResponse {

    private long id;

    private String name;

    private RackType rackType;

    private Set<ComputeNodeResponse> nodes = new HashSet();

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

    public RackType getRackType() {
        return rackType;
    }

    public void setRackType(RackType rackType) {
        this.rackType = rackType;
    }

    public Set<ComputeNodeResponse> getNodes() {
        return nodes;
    }

    public void setNodes(Set<ComputeNodeResponse> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return "RackResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rackType=" + rackType +
                ", nodes=" + nodes +
                '}';
    }
}
