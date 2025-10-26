package com.messaging_system.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.messaging_system.dto.MessageRequest;
import com.messaging_system.dto.MessageResponse;
import com.messaging_system.service.MailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
@Validated
public class MessageController {

    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendMessage(@Valid @RequestBody MessageRequest messageRequest) {
        try {
            // Set timestamp
            messageRequest.setCreatedAt(LocalDateTime.now());
            
            // Send email
            MessageRequest sentMessage = mailService.sendMessage(messageRequest);
            
            // Create success response
            MessageResponse response = new MessageResponse(
                true,
                "Message sent successfully! We'll get back to you within 24 hours.",
                sentMessage
            );
            
            return ResponseEntity.status(HttpStatus.OK).body(response);
            
        } catch (Exception e) {
            // Create error response
            MessageResponse response = new MessageResponse(
                false,
                "Failed to send message. Please try again later or contact us directly.",
                null
            );
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
