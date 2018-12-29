package com.exacs.ecra.entities.response;

import java.util.HashSet;
import java.util.Set;

public class RackSlotResponse {

    private long id;
    private String name;
    private Set<VirtualMachineResponse> vms = new HashSet();

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

    public Set<VirtualMachineResponse> getVms() {
        return vms;
    }

    public void setVms(Set<VirtualMachineResponse> vms) {
        this.vms = vms;
    }
}
