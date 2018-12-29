package com.exacs.ecra.services.impl;

import com.exacs.ecra.converters.EcraConverter;
import com.exacs.ecra.entities.model.Rack;
import com.exacs.ecra.entities.model.RackSlot;
import com.exacs.ecra.entities.model.VirtualMachine;
import com.exacs.ecra.entities.request.RackSlotRequest;
import com.exacs.ecra.repositories.impl.RackSlotRepositoryDao;
import com.exacs.ecra.repositories.impl.VirtualMachineRepositoryDao;
import com.exacs.ecra.services.inf.RackService;
import com.exacs.ecra.services.inf.RackSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class RackSlotServiceImpl implements RackSlotService {

    @Autowired
    private RackSlotRepositoryDao rackSlotRepositoryDao;

    @Autowired
    private VirtualMachineRepositoryDao virtualMachineRepositoryDao;

    @Autowired
    private RackService rackService;

    @Autowired
    private EcraConverter ecraConverter;

    @Override
    @Transactional
    public RackSlot createCluster(long rackId, RackSlotRequest rackSlotRequest) {
      Rack rack = rackService.getRack(rackId);
        if (rack != null) {
            RackSlot rackSlot = ecraConverter.fromRackSlotRequest(rackSlotRequest);
            rackSlot.setRack(rack);
            rackSlot = rackSlotRepositoryDao.create(rackSlot);
            return rackSlot;
        } else {
            return null;
        }

    }

    @Override
    public List<RackSlot> getClusters(long rackId) {
        return rackSlotRepositoryDao.findAllClusters(rackId, false);
    }

    @Override
    public RackSlot getCluster(long clusterId) {
       return (rackSlotRepositoryDao.findClusterById(clusterId, false));
    }

    @Override
    public RackSlot getCluster(long rackId, long clusterId) {
       return (rackSlotRepositoryDao.findClusterByRackIdAndClusterId(rackId, clusterId, false));
    }

    @Transactional
    @Override
    public void deleteClusters(long rackId) {
        List<RackSlot> rackSlotList = rackSlotRepositoryDao.findAllClusters(rackId, true);
        if (!CollectionUtils.isEmpty(rackSlotList)) {
            for (RackSlot rackSlot : rackSlotList) {
                List<VirtualMachine> virtualMachineList =
                        virtualMachineRepositoryDao.findVMsForCluster(rackSlot.getId(), true);
                if (!CollectionUtils.isEmpty(virtualMachineList)) {
                    for (VirtualMachine virtualMachine : virtualMachineList) {
                        virtualMachineRepositoryDao.deleteVM(virtualMachine.getId());
                    }
                }

            }
        }

        rackSlotRepositoryDao.deleteAllClusters(rackId);
        
    }

    @Transactional
    @Override
    public void deleteCluster(long clusterId) {
        RackSlot rackSlot = rackSlotRepositoryDao.findClusterById(clusterId, false);
        List<VirtualMachine> virtualMachineList =
                virtualMachineRepositoryDao.findVMsForCluster(rackSlot.getId(), true);
        if (!CollectionUtils.isEmpty(virtualMachineList)) {
            for (VirtualMachine virtualMachine : virtualMachineList) {
                virtualMachineRepositoryDao.deleteVM(virtualMachine.getId());
            }
        }
        rackSlotRepositoryDao.deleteCluster(clusterId);
    }


}
