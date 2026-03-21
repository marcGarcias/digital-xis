package dev.garcias.backend.controller;

import dev.garcias.backend.dto.request.ContactRequest;
import dev.garcias.backend.service.form.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ContactController {

    private final ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    @PostMapping("/contact")
    public ResponseEntity<?> sendContact(@Valid @RequestBody ContactRequest request) {
        service.process(request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Message sent successfully."
        ));
    }
}