package com.exacs.ecra.errors;

import com.exacs.ecra.exceptions.ECRAValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestErrorsController {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ECRAValidationException.class)
	@ResponseBody
	public Errors handleBadRequest(final ECRAValidationException e) {
		logger.error("Validation error", e);
		return e.getErrors();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public String handleUnExpectedRequest(final Throwable t) {
		logger.error("Validation error", t);
		return t.getMessage();
	}

}
