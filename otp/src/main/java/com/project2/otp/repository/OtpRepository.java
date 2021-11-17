package com.project2.otp.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project2.otp.entity.EmailOtp;

public interface OtpRepository extends JpaRepository<EmailOtp, String>{

}

