package rca.rw.secure.services;

import rca.rw.secure.models.Book;

public interface MailService {
    void sendResourceCreatedEmail(String username, Book book);
    void sendResourceUpdatedEmail(String username, Book book);
    void sendResourceDeletedEmail(String username, Book book);
    void sendBookingStatusEmail(String username, Book book, String action, String status);
    void sendBookingStatusEmailToDefault(Book book, String action, String status);
}