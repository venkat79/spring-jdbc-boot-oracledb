package com.exacs.ecra.controllers;

import com.exacs.ecra.converters.EcraConverter;
import com.exacs.ecra.entities.model.RackSlot;
import com.exacs.ecra.entities.model.VirtualMachine;
import com.exacs.ecra.entities.request.VirtualMachineRequest;
import com.exacs.ecra.entities.response.RackSlotResponse;
import com.exacs.ecra.entities.response.VirtualMachineResponse;
import com.exacs.ecra.services.inf.VirtualMachineService;
import com.exacs.ecra.utilities.APIURIConstants;
import com.exacs.ecra.utilities.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class VirtualMachineController {

    private static final Logger _logger = LoggerFactory.getLogger(RackSlotController.class);

    @Autowired
    private VirtualMachineService virtualMachineService;

    @Autowired
    private EcraConverter ecraConverter;

    @PostMapping(value = APIURIConstants.V2_ECRA_RACK_ID_CLUSTER_ID_NODES_NODE_ID_VMS, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RackSlotResponse> createVM(@PathVariable long clusterId,
                                                   @PathVariable long nodeId,
                                                   @RequestBody VirtualMachineRequest virtualMachineRequest,
                                                     HttpServletRequest httpServletRequest) {

        _logger.debug("VirtualMachineController::createVirtualMachine() called");

        RackSlot rackSlot = virtualMachineService.addVM(clusterId, nodeId, virtualMachineRequest);
        RackSlotResponse rackSlotResponse = ecraConverter.toRackSlotResponse(rackSlot);
        return new HttpResponse<RackSlotResponse>().ok().build(rackSlotResponse);

    }

    @GetMapping(value = APIURIConstants.V2_ECRA_RACK_ID_CLUSTER_ID_VMS, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VirtualMachineResponse>> getVMs(@PathVariable long clusterId) {

        _logger.debug("VirtualMachineController::getVirtualMachines() called");

        List<VirtualMachine> virtualMachinesList = virtualMachineService.getVMs(clusterId);
        List<VirtualMachineResponse> virtualMachineResponseList = ecraConverter.getVirtualMachineResponses(virtualMachinesList);
        return new HttpResponse<List<VirtualMachineResponse>>().ok().build(virtualMachineResponseList);

    }

    @DeleteMapping(value = APIURIConstants.V2_ECRA_RACK_ID_CLUSTER_ID_VMS_ID)
    public ResponseEntity deleteVM(@PathVariable long clusterId, @PathVariable long nodeId, @PathVariable long vmId) {
        _logger.debug("VirtualMachineController::deleteVM() called");

        RackSlot rackSlot = virtualMachineService.removeVM(clusterId, nodeId, vmId);
        RackSlotResponse rackSlotResponse = ecraConverter.toRackSlotResponse(rackSlot);
        return new HttpResponse<RackSlotResponse>().ok().build(rackSlotResponse);
    }


    @GetMapping(value = APIURIConstants.V2_ECRA_RACK_ID_CLUSTER_ID_NODES_NODE_ID_VMS)
    public ResponseEntity<VirtualMachineResponse> getVM(@PathVariable long clusterId, @PathVariable long nodeId) {
        _logger.debug("VirtualMachineController::getVM() called");

        VirtualMachine virtualMachine = virtualMachineService.getVM(clusterId, nodeId);
        VirtualMachineResponse virtualMachineResponse = ecraConverter.toVirtualMachineResponse(virtualMachine);
        return new HttpResponse<VirtualMachineResponse>().ok().build(virtualMachineResponse);
    }


}
