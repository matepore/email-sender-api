package matepore.email.sender.controller;

import matepore.email.sender.dto.EmailDto;
import matepore.email.sender.dto.EmailFileDto;
import matepore.email.sender.exception.EmailSendException;
import matepore.email.sender.service.IEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/email/v1")
public class EmailController {
    // Logger for email operations tracking
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
    private final IEmailService emailService;

    @Autowired
    public EmailController(IEmailService emailService) {
        this.emailService = emailService;
    }

    // Endpoint for sending simple emails without attachments
    @PostMapping("/send-message")
    public ResponseEntity<Map<String, String>> receiveRequestEmail(@RequestBody @Validated EmailDto emailDto) {
        logger.info("Received email sending request to: {}", (Object) emailDto.getToAddress());

        emailService.sendEmail(emailDto.getToAddress(), emailDto.getSubject(), emailDto.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("Status", "Email sent successfully");

        return ResponseEntity.ok(response);
    }

    // Endpoint for sending emails with file attachments
    @PostMapping("/send-message-with-file")
    public ResponseEntity<Map<String, String>> receiveRequestEmailWithFile(@ModelAttribute @Validated EmailFileDto emailFileDto) {
        logger.info("Received email with file sending request to: {}", (Object) emailFileDto.getToAddress());

        Path tempFilePath = null;
        try {
            String fileName = emailFileDto.getFile().getName();

            // Store file temporarily before sending
            tempFilePath = Paths.get("src/main/resources/files/" + fileName);
            Files.createDirectories(tempFilePath.getParent());
            Files.copy(emailFileDto.getFile().getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);
            File file = tempFilePath.toFile();

            emailService.sendEmailWithFile(emailFileDto.getToAddress(), emailFileDto.getSubject(), emailFileDto.getMessage(), file);

            Map<String, String> response = new HashMap<>();
            response.put("status", "Email sent successfully");
            response.put("file", fileName);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            String errorMessage = "Error processing file: " + e.getMessage();
            throw new EmailSendException(errorMessage, e);

        } finally {
            // Clean up temporary file after sending
            if (tempFilePath != null) {
                try {
                    Files.deleteIfExists(tempFilePath);
                } catch (IOException e) {
                    logger.warn("Could not delete temporary file: {}", tempFilePath, e);
                }
            }
        }
    }
}
