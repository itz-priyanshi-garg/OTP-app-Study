package com.project2.otp.services;
import java.util.Random;

import java.util.regex.Pattern;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import com.project2.otp.entity.EmailOtp;
import com.project2.otp.exceptions.NotAValidEmail;
import com.project2.otp.repository.OtpRepository;

@Service
public class OtpService {
	
//	private static final Logger LOGGER = LoggerFactory.getLogger(OtpService.class);
	
	@Autowired
	OtpRepository databaseObj;
	
	public String mailOtp(String email)
	{	
		String generatedOtp = generateOtp();
		String ans=generatedOtp;
		String to = email;
		
        // Sender's email ID needs to be mentioned
        String from = "gargpriyanshi100@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("gargpriyanshi100@gmail.com", "Priyanshi@5001");
            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("OTP for Recent login request");

            // Now set the actual message
            message.setText("Greetings of the day, \n For security reasons, you're required to use the following otp to login into application. This OTP get expired in 30 seconds \n \n OTP : "+generatedOtp);
            
            Transport.send(message);
        } 
        catch (MessagingException mex) {
            ans = "otp";
        }
        return ans;
	}
	
	public boolean isValidEmail(String email)
	{
//		LOGGER.info("Inside Is valid EMail");
		String emailRegex = "\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
                  
		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}
	
	public String generateOtp()
	{
//		LOGGER.info("Inside generate OTP");
		    Random rnd = new Random();
		    int number = rnd.nextInt(999999);

		    return String.format("%06d", number);
	}
	
	public String sendEmail(String email)
	{	
		
//		LOGGER.info("Inside SendEmail");
		// Recipient's email ID needs to be mentioned.
		String ans;
		
		if(!(isValidEmail(email)))
		{
			ans = new NotAValidEmail().toString();
		}
		else
		{
			EmailOtp searchedObj = databaseObj.findById(email).orElse(null);
			
			ans = mailOtp(email);
			if(searchedObj==null)
			{	
				if(!(ans.equals("otp")))
				{
					EmailOtp newObj = new EmailOtp();
					newObj.setEmailorMobile(email);
					newObj.setOtp(ans);
					newObj.setNoOfAttempts(4);
					newObj.setOtpExpiryTime(System.currentTimeMillis()+60000);
					newObj.setSessions_3Mins(System.currentTimeMillis()+180000);
					newObj.setVerified(false);
					databaseObj.save(newObj);
					ans = "Sent Otp successfully :)";
				}
	            else
	            {
	            	ans = "Exception occured";
	            }	
				
			}
			
			else if(searchedObj.getNoOfAttempts()<=0 && searchedObj.getSessions_3Mins()>System.currentTimeMillis())
			{
				ans = "You have exceeded the number of attempts. Try after "+((searchedObj.getSessions_3Mins()-System.currentTimeMillis())/1000)+" seconds";
			}	
			
			else
			{           
		            if(!(ans.equals("otp")))
		            {
			            int x = ((searchedObj.getNoOfAttempts()-1)<0)?4:(searchedObj.getNoOfAttempts()-1);
			            
						searchedObj.setNoOfAttempts(x);
						searchedObj.setOtp(ans);
						searchedObj.setOtpExpiryTime(System.currentTimeMillis()+60000);
						searchedObj.setVerified(false);
						
						if(searchedObj.getSessions_3Mins()<System.currentTimeMillis())
							searchedObj.setSessions_3Mins(System.currentTimeMillis()+180000);
						
						databaseObj.save(searchedObj);
						ans = "Sent Otp successfully :)";
		            }
		            else
		            {
		            	ans = "Exception occured";
		            }
			}
		}
		return ans;
	}
	
	public String validateOtp(EmailOtp obj)
	{		
		
//		LOGGER.info("Inside Validate OTP");
		String emailorMobile = obj.getEmailorMobile();
		String otp = obj.getOtp();
		
		EmailOtp searchedObj = databaseObj.findById(emailorMobile).orElse(null);
		
		String ans;
				
		if(searchedObj==null)
		{
			ans = "Email or mobile number is not correct";
		}
		
		else
		{	
			long time = System.currentTimeMillis();
			long diff = (searchedObj.getOtpExpiryTime() - time)/1000;
			System.out.println("Difference is : "+(diff));
			
			if(time>searchedObj.getOtpExpiryTime())
			{
				ans = "Otp is expired. Please generate a new Otp to login" + "\n";
				if(obj.getEmailorMobile().contains("@"))
					ans = ans + sendEmail(emailorMobile);
				else
					ans = ans + sendSMS(emailorMobile);
				
				searchedObj.setVerified(false);
			}
			
			else if((searchedObj.getOtp()).equals(otp))
			{
				ans = "Valid Otp";
				searchedObj.setVerified(true);
			}
			else
			{
				ans = "Invalid Otp, we are sending otp again to help you login into application." + "\n" ;
				if(obj.getEmailorMobile().contains("@"))
					ans = ans + sendEmail(emailorMobile);
				else
					ans = ans + sendSMS(emailorMobile);
				
				searchedObj.setVerified(false);
			}

			databaseObj.save(searchedObj);
		}
		return ans;
	}

	public String sendSMS(String mobile) {
		// TODO Auto-generated method stub

//		LOGGER.info("Inside Send SMS");
		String generatedOtp = generateOtp();
		
		EmailOtp searchedObj = databaseObj.findById(mobile).orElse(null); 
		
		String ans;
		
		if(searchedObj==null)
		{
			EmailOtp newObj = new EmailOtp();
			newObj.setEmailorMobile(mobile);
			newObj.setOtp(generatedOtp);
			newObj.setNoOfAttempts(4);
			newObj.setOtpExpiryTime(System.currentTimeMillis()+60000);
			newObj.setSessions_3Mins(System.currentTimeMillis()+180000);
			databaseObj.save(newObj);
			ans = "Entry saved successfully :)";
		}
		
		else if(searchedObj.getNoOfAttempts()<=0 && searchedObj.getSessions_3Mins()>System.currentTimeMillis())
		{
			ans = "You have exceeded the number of attempts. Try after "+((searchedObj.getSessions_3Mins()-System.currentTimeMillis())/1000)+" seconds";
		}	
		else
		{
			ans = "Entry saved successfully :)";
            
            int x = ((searchedObj.getNoOfAttempts()-1)<0)?4:(searchedObj.getNoOfAttempts()-1);
            
			searchedObj.setNoOfAttempts(x);
			searchedObj.setOtp(generatedOtp);
			searchedObj.setOtpExpiryTime(System.currentTimeMillis()+60000);
			searchedObj.setVerified(false);
			
			if(searchedObj.getSessions_3Mins()<System.currentTimeMillis())
				searchedObj.setSessions_3Mins(System.currentTimeMillis()+180000);
			
			databaseObj.save(searchedObj);
		}
		
		return ans;
	}
}
