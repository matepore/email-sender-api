package matepore.email.sender.service;

import java.io.File;


//Interface for email service operations
public interface IEmailService {
    void sendEmail(String[] toAddress, String subject, String message);
    void sendEmailWithFile(String[] toAddress, String subject, String message, File file);
}
