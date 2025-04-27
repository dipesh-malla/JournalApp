package malla.dipesh.journalApp.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "test APIs", description = "all about test endpoints") // for swagger

public class HealthCheck {

    @GetMapping("/health-check")
    public String healthCheck() {
        return "OK";
    }
}
