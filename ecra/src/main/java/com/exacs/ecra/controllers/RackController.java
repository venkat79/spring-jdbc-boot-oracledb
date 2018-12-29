package com.exacs.ecra.controllers;

import com.exacs.ecra.converters.EcraConverter;
import com.exacs.ecra.entities.model.Rack;
import com.exacs.ecra.entities.request.RackRequest;
import com.exacs.ecra.entities.response.RackResponse;
import com.exacs.ecra.exceptions.ECRAValidationException;
import com.exacs.ecra.services.inf.RackService;
import com.exacs.ecra.utilities.APIURIConstants;
import com.exacs.ecra.utilities.HttpResponse;
import com.exacs.ecra.validations.RackValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class RackController {

    private static final Logger _logger = LoggerFactory.getLogger(RackController.class);

    @Autowired
    private RackService rackService;

    @Autowired
    private EcraConverter ecraConverter;

    @Autowired
    private RackValidator rackValidator;

    /**
     * Get Racks
     * @return
     */
    @GetMapping(value= APIURIConstants.V2_ECRA_RACK, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RackResponse>> getRacks() {

        _logger.debug("RackController::getRacks() called");
        List<Rack> rackList = rackService.getRacks();
        List<RackResponse> rackResponseList = ecraConverter.toRacksResponse(rackList);
        return new HttpResponse<List<RackResponse>>().ok().build(rackResponseList);

    }

    /**
     * Get Rack
     * @param rackId
     * @return
     */
    @GetMapping(value= APIURIConstants.V2_ECRA_RACK_ID, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RackResponse> getRack(@PathVariable long rackId) throws ECRAValidationException {
        _logger.debug("RackController::getRack() called");
        Rack rack = rackService.getRack(rackId);

        if (rack == null) {

        }

        RackResponse rackResponse = ecraConverter.toRackResponse(rack);
        return new HttpResponse<RackResponse>().ok().build(rackResponse);
    }

    /**
     * Create Racks
     * @param rackRequest
     * @return
     */
    @PostMapping(value = APIURIConstants.V2_ECRA_RACK, produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RackResponse> createRack(@RequestBody RackRequest rackRequest) throws ECRAValidationException, Exception {
        _logger.debug("RackController::createRack() called");


        Rack rack = rackService.createRack(rackRequest);
        RackResponse rackResponse = ecraConverter.toRackResponse(rack);
        return new HttpResponse<RackResponse>().ok().build(rackResponse);
    }

    /**
     * Delete Rack
     * @param rackId
     * @return
     */
    @DeleteMapping(value = APIURIConstants.V2_ECRA_RACK_ID)
    public ResponseEntity deleteRack(@PathVariable long rackId) {
        _logger.debug("RackController::deleteRack() called");
        rackService.deleteRack(rackId);
        return new HttpResponse().noContent().build();
    }

}
