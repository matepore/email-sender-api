package matepore.email.sender.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Send email with file attachment",
        description = "Data transfer object for email with file attachment"
)
public class EmailFileDto {
    // Recipients of the email
    @ArraySchema(
            schema = @Schema(
                    description = "Array of recipient email addresses",
                    example = "email.address@service.com"),
            minItems = 1
    )
    private String[] toAddress;

    // Subject of the email
    @Schema(
            description = "Subject of the email",
            example = "Important Notification"
    )
    private String subject;

    // Content of the email
    @Schema(
            description = "Body content of the email",
            example = "This text contains important information, please respond with an equal amount of information. Regards, human n°91382494."
    )
    private String message;

    // File attachment
    @Schema(
            type = "string",
            format = "binary",
            description = "File to be attached to the email, can't be more than 10MB"
    )
    private MultipartFile file;
}
