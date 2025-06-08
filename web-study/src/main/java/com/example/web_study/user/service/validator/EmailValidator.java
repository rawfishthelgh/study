package com.example.web_study.user.service.validator;

import java.util.regex.Pattern;

public class EmailValidator {

	private static final String EMAIL_REGEX = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

	private EmailValidator() {
	}

	public static boolean isValid(String email) {
		return email != null && EMAIL_PATTERN.matcher(email).matches();
	}
}
