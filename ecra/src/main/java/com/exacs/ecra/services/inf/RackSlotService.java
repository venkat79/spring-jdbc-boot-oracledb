package com.exacs.ecra.services.inf;

import com.exacs.ecra.entities.model.RackSlot;
import com.exacs.ecra.entities.request.RackSlotRequest;

import java.util.List;

public interface RackSlotService {

    RackSlot createCluster(long rackId, RackSlotRequest rackSlotRequest);

    List<RackSlot> getClusters(long rackId);

    RackSlot getCluster(long rackId, long clusterId);

    RackSlot getCluster(long clusterId);

    void deleteCluster(long clusterId);

    void deleteClusters(long rackId);


}
