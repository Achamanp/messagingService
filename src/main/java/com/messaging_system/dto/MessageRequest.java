package com.messaging_system.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
	
	private String fullName;
	private String email;
	private String subject;
	
	private String message;
	private LocalDateTime createdAt;
	
	

}
