package com.project2.otp.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OtpServiceTest {
	
	@Autowired
	private OtpService obj;
	
	@Test
	void test1() {
		String expected = "Sent Otp successfully :)";
		String actual = obj.sendEmail("priyanshi.18bcs1068@abes.ac.in");
		assertEquals(expected,actual);
	}
	
	@Test
	void test() {
		String expected = "Entered email is not valid";
		String actual = obj.sendEmail("p@g@gmail.com");
		assertEquals(expected,actual);
	}
	
	@Test
	void test2() {
		String expected = "Entered email is not valid";
		String actual = obj.sendEmail(null);
		assertEquals(expected,actual);
	}
	
	
	
	

}
