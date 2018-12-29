package com.exacs.ecra.services.inf;

import com.exacs.ecra.entities.model.RackSlot;
import com.exacs.ecra.entities.model.VirtualMachine;
import com.exacs.ecra.entities.request.VirtualMachineRequest;

import java.util.List;

public interface VirtualMachineService {

    RackSlot addVM(long clusterId, long nodeId, VirtualMachineRequest virtualMachineRequest);

    RackSlot removeVM(long clusterId, long nodeId, long vmId);

    List<VirtualMachine> getVMs(long clusterId);

    VirtualMachine getVM(long clusterId, long nodeId);

    VirtualMachine getVM(long vmId);


}
