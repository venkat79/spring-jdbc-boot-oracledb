package com.exacs.ecra.errors;

import org.springframework.context.MessageSource;

public class ErrorUtils extends BaseErrorUtils {
    
    public static void addLocalizedError(final Errors errors, final MessageSource messageSource, final ErrorCodes errorCodes, final Object[] messageArgs, final Object[] detailArgs, final Object[] moreInfoArgs){
        errors.getErrors().add(getLocalizedError(messageSource, messageArgs, detailArgs,
                moreInfoArgs, errorCodes.getMessageKey(), errorCodes.getCode(),
                errorCodes.getMoreInfoKey(), errorCodes.getDetailKey()));
    }

}
