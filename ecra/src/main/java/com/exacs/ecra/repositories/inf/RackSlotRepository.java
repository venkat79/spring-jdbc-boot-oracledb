package com.exacs.ecra.repositories.inf;

import com.exacs.ecra.entities.model.RackSlot;

import java.util.List;

public interface RackSlotRepository {

    RackSlot create(RackSlot rackSlot);

    List<RackSlot> findAllClusters(long rackId, boolean lazy);

    RackSlot findClusterById(long rackSlotId, boolean lazy);

    List<RackSlot> findClusterByIds(List<Long> rackSlotIds, boolean lazy);

    RackSlot findClusterByRackIdAndClusterId(long rackId, long rackSlotId, boolean lazy);

    void deleteAllClusters(long rackId);

    void deleteCluster(long rackSlotId);


}
