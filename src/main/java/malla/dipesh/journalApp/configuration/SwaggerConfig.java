package malla.dipesh.journalApp.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info()
                        .title("JournalApp APIs Docs")
                        .description("By dipesh")
        )
                .servers(List.of(new Server().url("http://localhost:8080").description("localhost"),
                    new Server().url("http://localhost:8081").description("live")))
                .tags(List.of(
                        new Tag().name("User APIs")
//                        new io.swagger.v3.oas.models.tags.Tag().name("Admin APIs").description("all about admin endpoints")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes("basic", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer").bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization")
                ));
    }
}
