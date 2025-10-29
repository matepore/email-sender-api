package matepore.email.sender.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import org.springframework.web.multipart.MultipartFile;

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

    //Logger for tracking email operations
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
    private final IEmailService emailService;

    @Autowired
    public EmailController(IEmailService emailService) {
        this.emailService = emailService;
    }

    @Operation(
            summary = "Send a simple email message",
            description = "Sends an email to one or multiple recipients without attachments.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmailDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email sent successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    //Endpoint for sending simple emails without attachments
    @PostMapping("/send-message")
    public ResponseEntity<Map<String, String>> receiveRequestEmail(@RequestBody @Validated EmailDto emailDto) {
        logger.info("Received email sending request to: {}", (Object) emailDto.getToAddress());

        emailService.sendEmail(emailDto.getToAddress(), emailDto.getSubject(), emailDto.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("Status", "Email sent successfully");
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Send an email with file attachment",
            description = "Sends an email with an optional file attachment using multipart/form-data.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = EmailFileDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email sent successfully with file"),
                    @ApiResponse(responseCode = "400", description = "Error processing the file or invalid data")
            }
    )
    //Endpoint for sending email with file attachment
    @PostMapping(value = "/send-message-with-file", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> receiveRequestEmailWithFile(@ModelAttribute @Validated EmailFileDto emailFileDto) {

        logger.info("Received email with file sending request to: {}", (Object) emailFileDto.getToAddress());

        MultipartFile filePart = emailFileDto.getFile();

        //Validates that a file was uploaded
        if (filePart == null || filePart.isEmpty()) {
            throw new EmailSendException("No file was uploaded or file is empty");
        }

        //Obtains the original filename
        String originalFilename = filePart.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            originalFilename = "attachment_" + System.currentTimeMillis();
        }

        Path tempFilePath = null;

        try {
            //Store file temporarily before sending
            tempFilePath = Paths.get("src/main/resources/files/" + originalFilename);
            Files.createDirectories(tempFilePath.getParent());
            Files.copy(filePart.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

            File file = tempFilePath.toFile();

            emailService.sendEmailWithFile(emailFileDto.getToAddress(), emailFileDto.getSubject(), emailFileDto.getMessage(), file);

            Map<String, String> response = new HashMap<>();
            response.put("status", "Email sent successfully");
            response.put("file", originalFilename);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            String errorMessage = "Error processing file: " + e.getMessage();
            throw new EmailSendException(errorMessage, e);

        } finally {
            //Clean up temporary file after sending
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