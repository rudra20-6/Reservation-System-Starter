package flight.reservation.notification;

/**
 * Adapter that makes ThirdPartyEmailService compatible with NotificationService.
 * 
 * This is the ADAPTER in the Adapter pattern. It wraps the ThirdPartyEmailService
 * (the Adaptee) and translates calls from the NotificationService interface (the Target)
 * into the format expected by the third-party email library.
 * 
 * The ThirdPartyEmailService.sendMail(toAddress, subject, body, isHtml) method
 * is adapted to NotificationService.sendNotification(recipient, message).
 */
public class EmailNotificationAdapter implements NotificationService {

    private final ThirdPartyEmailService emailService;
    private final String defaultSubject;

    /**
     * Create an adapter with the given third-party email service.
     *
     * @param emailService   the third-party email service to adapt
     * @param defaultSubject the default email subject to use
     */
    public EmailNotificationAdapter(ThirdPartyEmailService emailService, String defaultSubject) {
        this.emailService = emailService;
        this.defaultSubject = defaultSubject;
    }

    /**
     * Create an adapter with a default subject line.
     *
     * @param emailService the third-party email service to adapt
     */
    public EmailNotificationAdapter(ThirdPartyEmailService emailService) {
        this(emailService, "Flight Reservation Notification");
    }

    /**
     * Adapts the NotificationService interface to the ThirdPartyEmailService.
     * Translates sendNotification(recipient, message) into
     * sendMail(toAddress, subject, body, isHtml).
     *
     * @param recipient the recipient email address
     * @param message   the notification message (used as email body)
     * @return true if the email was sent successfully
     */
    @Override
    public boolean sendNotification(String recipient, String message) {
        // Adapt: translate our simple interface to the third-party's complex interface
        int statusCode = emailService.sendMail(recipient, defaultSubject, message, false);
        return statusCode == 0;
    }

    @Override
    public String getNotificationType() {
        return "Email";
    }
}
