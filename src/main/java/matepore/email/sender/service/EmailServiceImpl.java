package matepore.email.sender.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import matepore.email.sender.exception.EmailSendException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

//Implementation of email service for sending simple and file-attached emails
@Service
public class EmailServiceImpl implements IEmailService {

    private final String emailAddressFrom;
    private final JavaMailSender mailSender;

    public EmailServiceImpl(
            @Value("${email.address.from}") String emailAddressFrom,
            JavaMailSender mailSender) {
        this.emailAddressFrom = emailAddressFrom;
        this.mailSender = mailSender;
    }

    //Sends a simple email without attachments
    @Override
    public void sendEmail(String[] toAddress, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailAddressFrom);
        mailMessage.setTo(toAddress);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }


    //Sends an email with a file attachment
    //throws an EmailSendException if there's an error sending the email
    @Override
    public void sendEmailWithFile(String[] toAddress, String subject, String message, File file) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            mimeMessageHelper.setFrom(emailAddressFrom);
            mimeMessageHelper.setTo(toAddress);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(message);
            mimeMessageHelper.addAttachment(file.getName(), file);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EmailSendException("Error sending email with attachment", e);
        }
    }
}
