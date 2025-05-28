package rca.rw.secure.impls;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import rca.rw.secure.models.Book;
import rca.rw.secure.models.User;
import rca.rw.secure.repos.IUserRepo;
import rca.rw.secure.services.MailService;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final IUserRepo userRepo;
    private final SpringTemplateEngine templateEngine;
    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.username}")
    private String defaultAdminEmail; //so I receive it in case the admin doesn't actually exist

    @Async
    @Override
    public void sendResourceCreatedEmail(String username, Book book) {
        sendEmail(username, book, "resource-created", "Resource Created");
    }

    @Async
    @Override
    public void sendResourceUpdatedEmail(String username, Book book) {
        sendEmail(username, book, "resource-updated", "Resource Updated");
    }

    @Async
    @Override
    public void sendResourceDeletedEmail(String username, Book book) {
        sendEmail(username, book, "resource-deleted", "Resource Deleted");
    }

    @Async
    @Override
    public void sendBookingStatusEmail(String username, Book book, String action, String status) {
        User user = userRepo.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setTo(user.getEmail());
            helper.setSubject("Your booking has been " + action + ": " + book.getName());
            helper.setFrom(from);

            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("resourceName", book.getName());
            context.setVariable("action", action);
            context.setVariable("status", status);
            String htmlContent = templateEngine.process("booking-status", context);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            logger.info("Email sent to {}", user.getEmail());
        } catch (MessagingException e) {
            logger.error("Failed to send booking status email to {}: {}", user.getEmail(), e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Async
    @Override
    public void sendBookingStatusEmailToDefault(Book book, String action, String status) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setTo(defaultAdminEmail);
            helper.setSubject("Booking " + action);
            helper.setFrom(from);

            Context context = new Context();
            context.setVariable("username", "Admin");
            context.setVariable("resourceName", book.getName());
            context.setVariable("action", action);
            context.setVariable("status", status);
            String htmlContent = templateEngine.process("booking-status", context);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            logger.info("Email sent to default admin: {}", defaultAdminEmail);
        } catch (MessagingException e) {
            logger.error("Failed to send email to default admin {}: {}", defaultAdminEmail, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private void sendEmail(String username, Book book, String templateName, String subject) {
        User user = userRepo.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setFrom(from);

            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("resourceName", book.getName());
            String htmlContent = templateEngine.process(templateName, context);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            logger.info("Email sent to {} for {}", user.getEmail(), templateName);
        } catch (MessagingException e) {
            logger.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}