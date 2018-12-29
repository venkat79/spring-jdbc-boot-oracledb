package com.exacs.ecra.services.inf;

import com.exacs.ecra.entities.model.Rack;
import com.exacs.ecra.entities.request.RackRequest;
import com.exacs.ecra.exceptions.ECRAValidationException;

import java.util.List;

public interface RackService {

    List<Rack> getRacks();

    Rack getRack(long rackIdentifier);

    Rack createRack(RackRequest rackRequest) throws ECRAValidationException;

    void deleteRack(long rackIdentifier);

}
