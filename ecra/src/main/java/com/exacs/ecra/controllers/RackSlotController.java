package com.exacs.ecra.controllers;

import com.exacs.ecra.converters.EcraConverter;
import com.exacs.ecra.entities.model.RackSlot;
import com.exacs.ecra.entities.request.RackSlotRequest;
import com.exacs.ecra.entities.response.RackSlotResponse;
import com.exacs.ecra.services.inf.RackSlotService;
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
public class RackSlotController {

    private static final Logger _logger = LoggerFactory.getLogger(RackSlotController.class);

    @Autowired
    private RackSlotService rackSlotService;

    @Autowired
    private EcraConverter ecraConverter;

    @PostMapping(value = APIURIConstants.V2_ECRA_RACK_ID_CLUSTERS, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RackSlotResponse> createCluster(@PathVariable long rackId,
                                                          @RequestBody RackSlotRequest rackSlotRequest,
                                                          HttpServletRequest httpServletRequest) {

        _logger.debug("RackSlotController::createCluster() called");

        RackSlot rackSlot = rackSlotService.createCluster(rackId, rackSlotRequest);
        RackSlotResponse rackSlotResponse = ecraConverter.toRackSlotResponse(rackSlot);
        return new HttpResponse<RackSlotResponse>().ok().build(rackSlotResponse);
    }


    @GetMapping(value= APIURIConstants.V2_ECRA_RACK_ID_CLUSTERS, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RackSlotResponse>> getClusters(@PathVariable long rackId) {

        _logger.debug("RackSlotController::getClusters() called");

        List<RackSlot> rackSlotList = rackSlotService.getClusters(rackId);
        List<RackSlotResponse> rackSlotResponseList = ecraConverter.toRackSlotsResponse(rackSlotList);
        return new HttpResponse<List<RackSlotResponse>>().ok().build(rackSlotResponseList);

    }

    @GetMapping(value= APIURIConstants.V2_ECRA_RACK_ID_CLUSTER_ID, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RackSlotResponse> getCluster(@PathVariable long rackId, @PathVariable long clusterId) {

        _logger.debug("RackSlotController::getCluster() called");
        RackSlot rackSlot = rackSlotService.getCluster(rackId, clusterId);

        RackSlotResponse rackSlotResponse = ecraConverter.toRackSlotResponse(rackSlot);
        return new HttpResponse<RackSlotResponse>().ok().build(rackSlotResponse);

    }

    @DeleteMapping(value = APIURIConstants.V2_ECRA_RACK_ID_CLUSTER_ID)
    public ResponseEntity deleteCluster(@PathVariable long rackId, @PathVariable long clusterId) {
        _logger.debug("RackSlotController::deleteCluster() called");
        _logger.debug("Cluster Id is {}", clusterId);
        rackSlotService.deleteCluster(clusterId);
        return new HttpResponse().noContent().build();
    }

    @DeleteMapping(value = APIURIConstants.V2_ECRA_RACK_ID_CLUSTERS)
    public ResponseEntity deleteClusters(@PathVariable long rackId) {
        _logger.debug("RackSlotController::deleteClusters() called");
        rackSlotService.deleteClusters(rackId);
        return new HttpResponse().noContent().build();
    }

}

