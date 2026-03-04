package flight.reservation.notification;

/**
 * Target interface that the reservation system uses to send notifications.
 * This is the interface our system expects for all notification operations.
 */
public interface NotificationService {

    /**
     * Send a notification to the given recipient with a message.
     *
     * @param recipient the recipient identifier (email, phone, etc.)
     * @param message   the notification message content
     * @return true if the notification was sent successfully
     */
    boolean sendNotification(String recipient, String message);

    /**
     * Get the type of notification service (e.g., "Email", "SMS").
     *
     * @return the notification type as a string
     */
    String getNotificationType();
}
