package com.exacs.ecra.converters;

import com.exacs.ecra.entities.model.ComputeNode;
import com.exacs.ecra.entities.model.Rack;
import com.exacs.ecra.entities.model.RackSlot;
import com.exacs.ecra.entities.model.VirtualMachine;
import com.exacs.ecra.entities.request.ComputeNodeRequest;
import com.exacs.ecra.entities.request.RackRequest;
import com.exacs.ecra.entities.request.RackSlotRequest;
import com.exacs.ecra.entities.response.ComputeNodeResponse;
import com.exacs.ecra.entities.response.RackResponse;
import com.exacs.ecra.entities.response.RackSlotResponse;
import com.exacs.ecra.entities.response.VirtualMachineResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class EcraConverter {

    private static final Logger _logger = LoggerFactory.getLogger(EcraConverter.class);

    public List<RackResponse> toRacksResponse(List<Rack> rackList) {

        List<RackResponse> rackResponseList = new ArrayList();
        for (Rack rack: rackList) {
            RackResponse rackResponse = new RackResponse();
            BeanUtils.copyProperties(rack, rackResponse);
            setComputeNodeResponse(rack, rackResponse);
            rackResponseList.add(rackResponse);
        }

        return rackResponseList;
    }


    public List<RackSlotResponse> toRackSlotsResponse(List<RackSlot> rackSlotList) {
        List<RackSlotResponse> rackSlotResponseList = new ArrayList();
        for (RackSlot rackSlot : rackSlotList) {
            RackSlotResponse rackSlotResponse = new RackSlotResponse();
            BeanUtils.copyProperties(rackSlot, rackSlotResponse);
            setVirtualMachineResponse(rackSlot, rackSlotResponse);
            rackSlotResponseList.add(rackSlotResponse);
        }

        return rackSlotResponseList;
    }


    public void setComputeNodeResponse(Rack rack, RackResponse rackResponse) {
        if (!CollectionUtils.isEmpty(rack.getComputeNodeList())) {
            for (ComputeNode computeNode: rack.getComputeNodeList()) {
                ComputeNodeResponse computeNodeResponse = new ComputeNodeResponse();
                BeanUtils.copyProperties(computeNode, computeNodeResponse);
                rackResponse.getNodes().add(computeNodeResponse);
            }
        }
    }

    public RackResponse toRackResponse(Rack rack) {
        if (rack != null) {
            RackResponse rackResponse = new RackResponse();
            BeanUtils.copyProperties(rack, rackResponse);
            setComputeNodeResponse(rack, rackResponse);
            return rackResponse;
        } else {
            return null;
        }
    }

    public RackSlotResponse toRackSlotResponse(RackSlot rackSlot) {
        if (rackSlot != null) {
            RackSlotResponse rackSlotResponse = new RackSlotResponse();
            BeanUtils.copyProperties(rackSlot, rackSlotResponse);
            setVirtualMachineResponse(rackSlot, rackSlotResponse);
            return rackSlotResponse;
        } else {
            return null;
        }
    }

    public List<VirtualMachineResponse> getVirtualMachineResponses(List<VirtualMachine> virtualMachineList) {
        List<VirtualMachineResponse> virtualMachineResponseList = new ArrayList();
        if (!CollectionUtils.isEmpty(virtualMachineList)) {
            for (VirtualMachine virtualMachine : virtualMachineList) {
                VirtualMachineResponse virtualMachineResponse = new VirtualMachineResponse();
                BeanUtils.copyProperties(virtualMachine, virtualMachineResponse);
                virtualMachineResponse.setNodeName(virtualMachine.getComputeNode().getName());
                virtualMachineResponseList.add(virtualMachineResponse);

            }

        }

        return virtualMachineResponseList;
    }

    public VirtualMachineResponse toVirtualMachineResponse(VirtualMachine virtualMachine) {
        VirtualMachineResponse virtualMachineResponse = new VirtualMachineResponse();
        if (virtualMachine != null) {
            BeanUtils.copyProperties(virtualMachine, virtualMachineResponse);
            virtualMachineResponse.setNodeName(virtualMachine.getComputeNode().getName());
        }
        return virtualMachineResponse;
    }


    public void setVirtualMachineResponse(RackSlot rackSlot, RackSlotResponse rackSlotResponse) {
       if (!CollectionUtils.isEmpty(rackSlot.getVirtualMachinesList())) {
            for (VirtualMachine virtualMachine : rackSlot.getVirtualMachinesList()) {
                VirtualMachineResponse virtualMachineResponse = new VirtualMachineResponse();
                BeanUtils.copyProperties(virtualMachine, virtualMachineResponse);

                // It could be lazy instantiation, so guard against that
                if (virtualMachine.getComputeNode() != null) {
                    virtualMachineResponse.setNodeName(virtualMachine.getComputeNode().getName());
                }

                rackSlotResponse.getVms().add(virtualMachineResponse);
            }
        }
    }

    public RackSlot fromRackSlotRequest(RackSlotRequest rackSlotRequest) {
        if (rackSlotRequest != null) {
            RackSlot rackSlot = new RackSlot();
            BeanUtils.copyProperties(rackSlotRequest, rackSlot);
            return rackSlot;
        } else {
            return null;
        }
    }

    public Rack fromRackRequest(RackRequest rackRequest) {
        if (rackRequest != null) {
            Rack rack = new Rack();
            rack.setName(rackRequest.getName());
            rack.setRackType(rackRequest.getRackType());

            if (!CollectionUtils.isEmpty(rackRequest.getNodes())) {
                for (ComputeNodeRequest computeNodeRequest : rackRequest.getNodes()) {
                    ComputeNode computeNode = new ComputeNode();
                    computeNode.setRack(rack);
                    computeNode.setName(computeNodeRequest.getName());
                    rack.getComputeNodeList().add(computeNode);
                }
            }
            return rack;
        } else {
            return null;
        }
    }

}
