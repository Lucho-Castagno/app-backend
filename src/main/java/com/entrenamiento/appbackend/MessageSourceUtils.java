package com.entrenamiento.appbackend;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceUtils {

	private static MessageSource messageSource;
	
	public MessageSourceUtils(MessageSource messageSource) {
		MessageSourceUtils.messageSource = messageSource;
	}
	
	public static String getMessage(String key) {
		return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
	}
	
}
