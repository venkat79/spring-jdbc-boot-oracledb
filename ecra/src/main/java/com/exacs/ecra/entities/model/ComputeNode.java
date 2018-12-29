package com.exacs.ecra.entities.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class ComputeNode {

    private static final Logger _logger = LoggerFactory.getLogger(ComputeNode.class);

    private long id;
    private String name;

    private Rack rack;

    public ComputeNode() {}

    public ComputeNode(long id, String name) {
        super();
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

    public Rack getRack() {
        return rack;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComputeNode that = (ComputeNode) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "ComputeNode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
