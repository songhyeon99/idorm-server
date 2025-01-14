package idorm.idormServer.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static idorm.idormServer.config.SecurityConfig.AUTHENTICATION_HEADER_NAME;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        final Info info = new Info()
                .title("아이돔 develop api")
                .description("테스트 서버 api description")
                .version("2.0.0");

        final String tokenSchemaName = AUTHENTICATION_HEADER_NAME;
        final SecurityRequirement tokenRequirement = new SecurityRequirement().addList(tokenSchemaName);
        final Components components = new Components()
                .addSecuritySchemes(tokenSchemaName, new SecurityScheme()
                        .name(tokenSchemaName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                );

        return new OpenAPI()
                .info(info)
                .addSecurityItem(tokenRequirement)
                .components(components);
    }
}