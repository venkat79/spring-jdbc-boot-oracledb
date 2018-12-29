package com.exacs.ecra.entities.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

public class VirtualMachine {

    private long id;

    private String name;

    private RackSlot rackSlot;

    private ComputeNode computeNode;

    public VirtualMachine() {

    }

    public VirtualMachine(long id, String name) {
        this.id = id;
        this.name = name;
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

    public RackSlot getRackSlot() {
        return rackSlot;
    }

    public void setRackSlot(RackSlot rackSlot) {
        this.rackSlot = rackSlot;
    }

    public ComputeNode getComputeNode() {
        return computeNode;
    }

    public void setComputeNode(ComputeNode computeNode) {
        this.computeNode = computeNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VirtualMachine that = (VirtualMachine) o;
        return id == that.id;
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
