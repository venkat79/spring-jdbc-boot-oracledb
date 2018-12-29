package com.exacs.ecra.errors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;

public class BaseErrorUtils {

	public static void addError(final Errors errors, final String code, final String message, final String detail, final String moreInfo) {
		errors.getErrors().add(getError(code, message, detail, moreInfo));
	}

	public static Error getError(final String code, final String message, final String detail, final String moreInfo) {
		Error error = new Error();
		error.setCode(code);
		error.setMessage(message);
		error.setDetail(detail);
		error.setMoreInfo(moreInfo);
		return error;
	}

	public static Error getLocalizedError(final MessageSource messageSource, final Object[] messageArgs,
			final Object[] detailArgs, final Object[] moreInfoArgs, String messageKey, String code, String moreInfoKey, String detailKey) {
		String message = getMessage(messageSource, messageKey, messageArgs);

		String detail = null;
		if (detailKey != null) {
			detail = getMessage(messageSource, detailKey, detailArgs);
		}

		String moreInfo = null;
		if (moreInfoKey != null) {
			moreInfo = getMessage(messageSource, moreInfoKey, moreInfoArgs);
		}else if(moreInfoArgs != null && moreInfoArgs.length > 0){

			moreInfo = Arrays.asList(moreInfoArgs).toString()
					.replace("[","")
					.replace("]","")
					.trim();
		}

		return getError(code, message, detail, moreInfo);
	}

	public static String getMessage(final MessageSource messageSource, final String messageKey, final Object[] args) {
		return messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
	}

}
