package com.exacs.ecra.exceptions;

import com.exacs.ecra.errors.*;

public class ECRAValidationException extends Exception {

    private Errors errors;

    public ECRAValidationException(Errors errors) {
        super();
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
