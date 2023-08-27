package com.usyd.capstone.common.compents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class SendEmail {

    @Autowired
    private JavaMailSender mailSender;

    @Async("taskExecutor")
    public void sentRegistrationEmail(String email, long registrationTimestamp, String passwordToken, int userRole){
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(email);
            helper.setSubject("Welcome to our platform! Please verify your email");

            String emailContent = getRegistEmailContent(email, registrationTimestamp, passwordToken, userRole);
            helper.setText(emailContent, true); // Use true to enable HTML content

            mailSender.send(mimeMessage);
        } catch (MessagingException | MailException e) {
            // Handle the exception here
            e.printStackTrace();
            // You can log the exception or take other appropriate actions
        }
    }

    private static String getRegistEmailContent(String email, long registrationTimestamp, String passwordToken, int userRole) {
        String url = "http://localhost:8082/user/registrationVerification?email=" + email + "&registrationTimestamp=" +
                registrationTimestamp + "&passwordToken=" + passwordToken + "&userRole=" + userRole;
        String emailContent = "<p>Dear user,</p>" +
                "<p>Thank you for registering with us! To complete your registration, please click the link below to verify your email:</p>" +
                "<P>Verification Link: <a href=\"" + url + "\">" + url +"</a></P>" +
                "<P>If you're unable to click the link, please copy and paste the link into your browser's address bar.</P>" +
                "<P>This link will be valid for the next 24 hours. If you don't verify within this time frame, your account won't be activated.</P>" +
                "<P>If you didn't register for our platform, please disregard this email.</P>" +
                "<P>Thank you!</P>"+
                "<P>Best regards,</P>" +
                "<P>[Company Name]</P>";
        return emailContent;
    }



    @Async("taskExecutor")
    public void sentForgetEmail(String email, long resettingPasswordTimestamp){
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(email);
            helper.setSubject("Password Reset Request");

            String emailContent = getForgetEmailContent(email, resettingPasswordTimestamp);
            helper.setText(emailContent, true); // Use true to enable HTML content

            mailSender.send(mimeMessage);
        } catch (MessagingException | MailException e) {
            // Handle the exception here
            e.printStackTrace();
            // You can log the exception or take other appropriate actions
        }
    }

    private static String getForgetEmailContent(String email, long resettingPasswordTimestamp) {
        String url = "http://localhost:8082/user/forgetPasswordVerification?email=" + email + "&resettingPasswordTimestamp=" +
                resettingPasswordTimestamp;
        String emailContent = "<p>Dear user,</p>" +
                "<p>We noticed that you recently requested a password reset. To ensure the security of your account, " +
                "we are here to assist you in completing this process. Please click on the link below to create a new password:</p>" +
                "<P>Verification Link: <a href=\"" + url + "\">" + url +"</a></P>" +
                "<P>Please note that this link will be valid for the next 24 hours. " +
                "If you did not initiate this password reset request, you can disregard this email.</P>" +
                "<P>Thank you for your cooperation.</P>" +
                "<P>Best regards,</P>" +
                "<P>[Company Name]</P>";

        return emailContent;
    }
}
