package com.exacs.ecra.services.impl;

import com.exacs.ecra.converters.EcraConverter;
import com.exacs.ecra.entities.model.Rack;
import com.exacs.ecra.entities.request.RackRequest;
import com.exacs.ecra.exceptions.ECRAValidationException;
import com.exacs.ecra.repositories.impl.RackRepositoryDao;
import com.exacs.ecra.services.inf.RackService;
import com.exacs.ecra.services.inf.RackSlotService;
import com.exacs.ecra.validations.RackValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RackServiceImpl implements RackService {

    private static final Logger _logger = LoggerFactory.getLogger(RackServiceImpl.class);

    @Autowired
    private RackRepositoryDao rackRepositoryDao;

    @Autowired
    private RackSlotService rackSlotService;


    @Autowired
    private EcraConverter ecraConverter;

    @Autowired
    private RackValidator rackValidator;

    @Override
    public List<Rack> getRacks() {
        return (rackRepositoryDao.findAll(false));
    }


    @Override
    public Rack getRack(long rackIdentifier) {
        Rack rack = rackRepositoryDao.findRackById(rackIdentifier, false);
        return rack;
    }


    @Override
    @Transactional
    public Rack createRack(RackRequest rackRequest) throws ECRAValidationException {
       if (rackRequest != null) {
            rackValidator.validateForCreateRacks(rackRequest);

            Rack rack = ecraConverter.fromRackRequest(rackRequest);
            if (rack != null) {
                _logger.debug("Rack object <before save> : {}", rack.toString());
                rack = rackRepositoryDao.create(rack);
                _logger.debug("Rack object <after save> : {}", rack.toString());
                return rack;
            } else {
                return null;
            }

        } else {
            return null;
        }


    }

    @Override
    @Transactional
    public void deleteRack(long rackIdentifier) {
        rackSlotService.deleteClusters(rackIdentifier);
        rackRepositoryDao.delete(rackIdentifier);
    }
}
