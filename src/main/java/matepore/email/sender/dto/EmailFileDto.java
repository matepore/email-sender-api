package matepore.email.sender.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for email with file attachment")
public class EmailFileDto {
    // Recipients of the email
    @Schema(description = "Array of recipient email addresses")
    private String[] toAddress;

    // Subject of the email
    @Schema(description = "Subject of the email")
    private String subject;

    // Content of the email
    @Schema(description = "Body content of the email")
    private String message;

    // File attachment
    @Schema(description = "File to be attached to the email")
    private MultipartFile file;
}
