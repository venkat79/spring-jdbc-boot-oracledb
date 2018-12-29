package com.exacs.ecra.repositories.inf;

import com.exacs.ecra.entities.model.Rack;

import java.util.List;

public interface RackRepository {

    Rack create(Rack rack);

    List<Rack> findAll(boolean lazy);

    Rack findRackById(long rackId, boolean lazy);

    List<Rack> findRackByIds(List<Long> rackIds, boolean lazy);

    void delete(long rackId);

    void deleteAllRacks();

    void delete(List<Long> rackIds);

}
