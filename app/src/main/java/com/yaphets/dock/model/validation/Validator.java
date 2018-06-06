package com.yaphets.dock.model.validation;

import com.yaphets.dock.model.Accessible;

public interface Validator {
	Result isValid(Accessible item);

	static boolean isEmailValid(String email) {
		return email.contains("@") && email.contains(".com");
	}

	static boolean isPasswordValid(String password) {
		return password.length() > 4;
	}
}
