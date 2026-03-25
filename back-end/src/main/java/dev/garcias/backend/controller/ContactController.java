package dev.garcias.backend.controller;

import dev.garcias.backend.dto.request.ContactRequest;
import dev.garcias.backend.service.form.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Contact Form", description = "Provides endpoints for processing contact form submissions, including input validation, captcha verification, and email delivery.")
public class ContactController {

    private final ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    @PostMapping("/contact")
    @Operation(summary = "Submit contact form", description = "Validates the contact form data and sends the message via email.")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Message sent successfully."),
    @ApiResponse(responseCode = "400", description = "Data Validation Error"),
    @ApiResponse(responseCode = "400", description = "Invalid or expired security verification"),
    @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<?> sendContact(@Valid @RequestBody ContactRequest request) {
        service.process(request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Message sent successfully."
        ));
    }
}