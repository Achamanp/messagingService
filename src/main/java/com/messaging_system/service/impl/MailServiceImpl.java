package com.messaging_system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.messaging_system.dto.MessageRequest;
import com.messaging_system.service.MailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${receiver.mail.address}")
    private String receiverMailAddress;

    @Value("${spring.mail.username}")
    private String senderMailAddress;

    @Override
    public MessageRequest sendMessage(MessageRequest messageRequest) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(senderMailAddress);
            helper.setTo(receiverMailAddress);
            helper.setReplyTo(messageRequest.getEmail());
            helper.setSubject("New Contact Form Submission: " + messageRequest.getSubject());
            
            String htmlContent = buildEmailTemplate(messageRequest);
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
            
            return messageRequest;
            
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    private String buildEmailTemplate(MessageRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
        String formattedDate = request.getCreatedAt().format(formatter);
        
        return "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Contact Form Submission</title>" +
                "</head>" +
                "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>" +
                "    <table role='presentation' style='width: 100%; border-collapse: collapse;'>" +
                "        <tr>" +
                "            <td style='padding: 40px 0;'>" +
                "                <table role='presentation' style='width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>" +
                "                    <!-- Header -->" +
                "                    <tr>" +
                "                        <td style='background: linear-gradient(135deg, #0d9488 0%, #2563eb 100%); padding: 40px 30px; text-align: center; border-radius: 8px 8px 0 0;'>" +
                "                            <h1 style='color: #ffffff; margin: 0; font-size: 28px; font-weight: bold;'>New Contact Form Submission</h1>" +
                "                        </td>" +
                "                    </tr>" +
                "                    " +
                "                    <!-- Content -->" +
                "                    <tr>" +
                "                        <td style='padding: 40px 30px;'>" +
                "                            <p style='color: #6b7280; font-size: 14px; margin: 0 0 30px 0;'>Received on " + formattedDate + "</p>" +
                "                            " +
                "                            <!-- Contact Details -->" +
                "                            <table role='presentation' style='width: 100%; border-collapse: collapse;'>" +
                "                                <tr>" +
                "                                    <td style='padding: 15px; background-color: #f9fafb; border-left: 4px solid #0d9488;'>" +
                "                                        <p style='margin: 0 0 8px 0; font-size: 12px; color: #6b7280; text-transform: uppercase; font-weight: bold;'>Full Name</p>" +
                "                                        <p style='margin: 0; font-size: 16px; color: #1f2937; font-weight: 600;'>" + escapeHtml(request.getFullName()) + "</p>" +
                "                                    </td>" +
                "                                </tr>" +
                "                                <tr><td style='height: 15px;'></td></tr>" +
                "                                <tr>" +
                "                                    <td style='padding: 15px; background-color: #f9fafb; border-left: 4px solid #0d9488;'>" +
                "                                        <p style='margin: 0 0 8px 0; font-size: 12px; color: #6b7280; text-transform: uppercase; font-weight: bold;'>Email Address</p>" +
                "                                        <p style='margin: 0; font-size: 16px; color: #1f2937;'><a href='mailto:" + escapeHtml(request.getEmail()) + "' style='color: #0d9488; text-decoration: none;'>" + escapeHtml(request.getEmail()) + "</a></p>" +
                "                                    </td>" +
                "                                </tr>" +
                "                                <tr><td style='height: 15px;'></td></tr>" +
                "                                <tr>" +
                "                                    <td style='padding: 15px; background-color: #f9fafb; border-left: 4px solid #0d9488;'>" +
                "                                        <p style='margin: 0 0 8px 0; font-size: 12px; color: #6b7280; text-transform: uppercase; font-weight: bold;'>Subject</p>" +
                "                                        <p style='margin: 0; font-size: 16px; color: #1f2937; font-weight: 600;'>" + escapeHtml(request.getSubject()) + "</p>" +
                "                                    </td>" +
                "                                </tr>" +
                "                                <tr><td style='height: 15px;'></td></tr>" +
                "                                <tr>" +
                "                                    <td style='padding: 15px; background-color: #f9fafb; border-left: 4px solid #0d9488;'>" +
                "                                        <p style='margin: 0 0 8px 0; font-size: 12px; color: #6b7280; text-transform: uppercase; font-weight: bold;'>Message</p>" +
                "                                        <p style='margin: 0; font-size: 15px; color: #1f2937; line-height: 1.6; white-space: pre-wrap;'>" + escapeHtml(request.getMessage()) + "</p>" +
                "                                    </td>" +
                "                                </tr>" +
                "                            </table>" +
                "                            " +
                "                            <!-- Action Button -->" +
                "                            <table role='presentation' style='width: 100%; margin-top: 30px;'>" +
                "                                <tr>" +
                "                                    <td style='text-align: center;'>" +
                "                                        <a href='mailto:" + escapeHtml(request.getEmail()) + "?subject=Re: " + escapeHtml(request.getSubject()) + "' style='display: inline-block; padding: 14px 32px; background-color: #0d9488; color: #ffffff; text-decoration: none; border-radius: 6px; font-weight: bold; font-size: 16px;'>Reply to Message</a>" +
                "                                    </td>" +
                "                                </tr>" +
                "                            </table>" +
                "                        </td>" +
                "                    </tr>" +
                "                    " +
                "                    <!-- Footer -->" +
                "                    <tr>" +
                "                        <td style='padding: 30px; background-color: #f9fafb; text-align: center; border-radius: 0 0 8px 8px; border-top: 1px solid #e5e7eb;'>" +
                "                            <p style='margin: 0; font-size: 14px; color: #6b7280;'>This is an automated message from your contact form.</p>" +
                "                            <p style='margin: 8px 0 0 0; font-size: 12px; color: #9ca3af;'>D. Dolphine Programs | 123 Education Street, Tech City, IN 560001</p>" +
                "                        </td>" +
                "                    </tr>" +
                "                </table>" +
                "            </td>" +
                "        </tr>" +
                "    </table>" +
                "</body>" +
                "</html>";
    }

    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}
