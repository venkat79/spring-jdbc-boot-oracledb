package com.exacs.ecra.entities.model;

import com.exacs.ecra.entities.enums.RackType;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Rack {

    private long id;
    private String name;
    private RackType rackType;
    private Set<ComputeNode> computeNodeList;

    public Rack() {
        this.computeNodeList = new HashSet();
    }

    public Rack(long id, String name, RackType rackType) {
        super();
        this.id = id;
        this.name = name;
        this.rackType = rackType;
        this.computeNodeList = new HashSet();
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

    public RackType getRackType() {
        return rackType;
    }

    public void setRackType(RackType rackType) {
        this.rackType = rackType;
    }

    public Set<ComputeNode> getComputeNodeList() {
        return computeNodeList;
    }

    public void setComputeNodeList(Set<ComputeNode> computeNodeList) {
        this.computeNodeList = computeNodeList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rack rack = (Rack) o;
        return Objects.equals(name, rack.name) &&
                rackType == rack.rackType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
