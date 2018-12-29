package com.exacs.ecra.entities.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RackSlot {

    private long id;

    private String name;

    private Rack rack;

    private List<VirtualMachine> virtualMachinesList;

    public RackSlot() {
        this.virtualMachinesList = new ArrayList();
    }

    public RackSlot(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Rack getRack() {
        return rack;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }

    public List<VirtualMachine> getVirtualMachinesList() {
        return virtualMachinesList;
    }

    public void setVirtualMachinesList(List<VirtualMachine> virtualMachinesList) {
        this.virtualMachinesList = virtualMachinesList;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RackSlot rackSlot = (RackSlot) o;
        return id == rackSlot.id;
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
