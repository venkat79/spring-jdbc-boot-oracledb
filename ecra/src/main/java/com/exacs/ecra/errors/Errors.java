package com.exacs.ecra.errors;

import java.util.ArrayList;
import java.util.List;


public class Errors {

    private List<Error> errors;

    public List<Error> getErrors() {
        if(errors == null){
            errors = new ArrayList<>();
        }
        return errors;
    }
}
