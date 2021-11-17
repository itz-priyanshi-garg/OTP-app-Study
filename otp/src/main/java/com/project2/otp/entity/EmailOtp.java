package com.project2.otp.entity;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Value;

@Entity
public class EmailOtp {
	
	@Id
	private String emailOrMobile;
	private String otp;
	private long otpExpiryTime;
	private long sessions_3Mins;
	@Value("${attempts}")
	private int noOfAttempts;
	private boolean verified;

	public EmailOtp() {
		super();
	}

	public EmailOtp(String emailOrMobile, String otp, long otpExpiryTime, long sessions_3Mins, int noOfAttempts,
			boolean verified) {
		super();
		this.emailOrMobile = emailOrMobile;
		this.otp = otp;
		this.otpExpiryTime = otpExpiryTime;
		this.sessions_3Mins = sessions_3Mins;
		this.noOfAttempts = noOfAttempts;
		this.verified = verified;
	}

	public long getOtpExpiryTime() {
		return otpExpiryTime;
	}

	public void setOtpExpiryTime(long otpExpiryTime) {
		this.otpExpiryTime = otpExpiryTime;
	}

	public long getSessions_3Mins() {
		return sessions_3Mins;
	}

	public void setSessions_3Mins(long sessions_3Mins) {
		this.sessions_3Mins = sessions_3Mins;
	}

	public int getNoOfAttempts() {
		return noOfAttempts;
	}
	
	public void setNoOfAttempts(int x)
	{
		this.noOfAttempts=x;
	}

	public String getEmailorMobile() {
		return emailOrMobile;
	}
	
	public void setEmailorMobile(String email) {
		this.emailOrMobile = email;
	}
	
	public String getOtp() {
		return otp;
	}
	
	public void setOtp(String otp) {
		this.otp = otp;
	}

	public boolean getVerified() {
		return verified;
	}

	public void setVerified(boolean x) {
		this.verified = x;
	}

	@Override
	public String toString() {
		return "EmailOtp [email=" + emailOrMobile + ", otp=" + otp + ", otpExpiryTime=" + otpExpiryTime + ", sessions_3Mins="
				+ sessions_3Mins + ", noOfAttempts=" + noOfAttempts + ", verified=" + verified + "]";
	}
}