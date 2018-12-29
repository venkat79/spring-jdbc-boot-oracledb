package com.exacs.ecra.validations;

import com.exacs.ecra.errors.ErrorCodes;
import com.exacs.ecra.errors.ErrorUtils;
import com.exacs.ecra.errors.Errors;
import com.exacs.ecra.exceptions.ECRAValidationException;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.ObjectUtils;

public class ValidationUtils {

    public static void validate(Errors errors) throws ECRAValidationException {
        if (errors.getErrors().size() > 0) {
            throw new ECRAValidationException(errors);
        }
    }

    public static <T> Errors rejectIfEmptyORNullFieldValue(MessageSource messageSource, Errors errors,
                                                           T value, String fieldName, boolean validateErrors)
                                                throws ECRAValidationException {
        if(ObjectUtils.isEmpty(value)){
            ErrorUtils.addLocalizedError(errors,messageSource, ErrorCodes.FIELD_VALUE_SHOULD_BE_PROVIDED,new Object[] { fieldName },null,null);
            if(validateErrors)
                validate(errors);
        }
        return errors;
    }


    @Bean
    public static MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("locale/messages");
        return messageSource;
    }


}
