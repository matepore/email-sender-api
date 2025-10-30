package matepore.email.sender.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Email Sender API")
                        .description("Client that uses Spring Boot to send emails with and without attachments. It uses gmail's SMTP server to send the emails.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Mateo Calcagno")
                                .url("https://github.com/matepore")
                                .email("calcagno.mateo@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}