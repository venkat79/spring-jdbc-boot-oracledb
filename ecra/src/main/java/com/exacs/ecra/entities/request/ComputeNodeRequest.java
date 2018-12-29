package com.exacs.ecra.entities.request;

public class ComputeNodeRequest {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ComputeNodeRequest{" +
                "name='" + name + '\'' +
                '}';
    }
}
