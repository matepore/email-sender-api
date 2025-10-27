package matepore.email.sender.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;


//Configuration class for email service setup
//Handles the configuration of JavaMailSender bean for sending emails
@Configuration
public class EmailConfig {


    //Email address that will be used as the sender
    //Value is loaded from application properties
    @Value("${email.address.from}")
    private String emailAddressFrom;


    //Creates and configures a JavaMailSender instance for Gmail SMTP
    //@return configured JavaMailSender bean
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Configure Gmail SMTP server settings
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(emailAddressFrom);
        mailSender.setPassword("<NotARealPassword>");//This is just temporary and not the real password, eventually I will change it to something secure

        // Set additional mail properties
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        return mailSender;
    }
}
