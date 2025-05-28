package rca.rw.secure.services;

import rca.rw.secure.models.Resource;

public interface MailService {
    void sendResourceCreatedEmail(String username, Resource resource);
    void sendResourceUpdatedEmail(String username, Resource resource);
    void sendResourceDeletedEmail(String username, Resource resource);
    void sendBookingStatusEmail(String username, Resource resource, String action, String status);
    void sendBookingStatusEmailToDefault(Resource resource, String action, String status);
}