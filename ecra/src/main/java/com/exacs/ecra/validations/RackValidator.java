package com.exacs.ecra.validations;

import com.exacs.ecra.entities.request.RackRequest;
import com.exacs.ecra.errors.ErrorCodes;
import com.exacs.ecra.errors.ErrorUtils;
import com.exacs.ecra.errors.Errors;
import com.exacs.ecra.exceptions.ECRAValidationException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class RackValidator {

    public void validateForCreateRacks(RackRequest rackRequest) throws ECRAValidationException {
        Errors errors = new Errors();
        MessageSource messageSource = ValidationUtils.messageSource();
        if (rackRequest != null) {
            // Check for empty rackName
            ValidationUtils.rejectIfEmptyORNullFieldValue(messageSource, errors, rackRequest.getName() != null ?
                        rackRequest.getName().trim() : rackRequest.getName(), "name", false);
        }

        // Validate if nodes are given (just a test case)
        if (CollectionUtils.isEmpty(rackRequest.getNodes())) {
            ErrorUtils.addLocalizedError(errors, messageSource,
                    ErrorCodes.COMPUTE_NODES_REQUEST_NOT_PRESENT, null, null, null);
        }

        ValidationUtils.validate(errors);

    }


}
