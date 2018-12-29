package com.exacs.ecra.repositories.inf;

import com.exacs.ecra.entities.model.VirtualMachine;

import java.util.List;

public interface VirtualMachineRepository {

    VirtualMachine createVM(VirtualMachine virtualMachine);

    void deleteVM(long id);

    VirtualMachine findById(long id, boolean lazy);

    List<VirtualMachine> findVMsForCluster(long rackSlotId, boolean lazy);

    VirtualMachine findVMForClusterAndNode(long rackSlotId, long NodeId, boolean lazy);
}
