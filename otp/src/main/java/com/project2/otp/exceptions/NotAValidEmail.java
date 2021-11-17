package com.project2.otp.exceptions;

public class NotAValidEmail extends Exception
{

	@Override
	public String toString() {
		return "Entered email is not valid";
	}

}
