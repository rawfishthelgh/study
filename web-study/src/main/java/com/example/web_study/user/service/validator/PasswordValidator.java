package com.example.web_study.user.service.validator;

public class PasswordValidator {

	public static boolean isValid(String password) {
		if (password == null || password.length() < 6 || password.length() > 10) {
			return false;
		}

		boolean hasLower = password.matches(".*[a-z].*");
		boolean hasUpper = password.matches(".*[A-Z].*");
		boolean hasDigit = password.matches(".*\\d.*");

		int count = 0;
		if (hasLower){
			count++;
		}
		if (hasUpper){
			count++;
		}
		if (hasDigit){
			count++;
		}

		return count >= 2;
	}
}
