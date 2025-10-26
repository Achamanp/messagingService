package com.messaging_system.service;

import com.messaging_system.dto.MessageRequest;

public interface MailService {
	public MessageRequest sendMessage(MessageRequest messageRequest);

}
