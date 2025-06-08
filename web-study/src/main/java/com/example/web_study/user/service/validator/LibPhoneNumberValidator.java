package com.example.web_study.user.service.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

@Component
public class LibPhoneNumberValidator implements PhoneNumberValidator {

	private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
	private final String countryCode;

	public LibPhoneNumberValidator(@Value("${phone.country}") String countryCode) {
		this.countryCode = countryCode;
	}
	@Override
	public boolean isValid(String phoneNumber) {
		try {
			var parsed = phoneUtil.parse(phoneNumber, countryCode);
			return phoneUtil.isValidNumber(parsed);
		} catch (Exception e) {
			return false;
		}
	}
}

