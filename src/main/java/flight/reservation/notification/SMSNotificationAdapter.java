package flight.reservation.notification;

/**
 * Adapter that makes ThirdPartySMSService compatible with NotificationService.
 * 
 * This is the ADAPTER in the Adapter pattern. It wraps the ThirdPartySMSService
 * (the Adaptee) and translates calls from the NotificationService interface (the Target)
 * into the format expected by the third-party SMS gateway.
 * 
 * The ThirdPartySMSService.dispatchSMS(phoneNumber, text, priority) method
 * is adapted to NotificationService.sendNotification(recipient, message).
 */
public class SMSNotificationAdapter implements NotificationService {

    private final ThirdPartySMSService smsService;
    private final int defaultPriority;

    /**
     * Create an adapter with the given third-party SMS service and priority.
     *
     * @param smsService      the third-party SMS service to adapt
     * @param defaultPriority the default SMS priority (1=high, 2=normal, 3=low)
     */
    public SMSNotificationAdapter(ThirdPartySMSService smsService, int defaultPriority) {
        this.smsService = smsService;
        this.defaultPriority = defaultPriority;
    }

    /**
     * Create an adapter with normal priority.
     *
     * @param smsService the third-party SMS service to adapt
     */
    public SMSNotificationAdapter(ThirdPartySMSService smsService) {
        this(smsService, 2); // default to normal priority
    }

    /**
     * Adapts the NotificationService interface to the ThirdPartySMSService.
     * Translates sendNotification(recipient, message) into
     * dispatchSMS(phoneNumber, text, priority).
     *
     * @param recipient the recipient phone number
     * @param message   the notification message (used as SMS text)
     * @return true if the SMS was dispatched successfully
     */
    @Override
    public boolean sendNotification(String recipient, String message) {
        // Adapt: translate our simple interface to the third-party's different interface
        return smsService.dispatchSMS(recipient, message, defaultPriority);
    }

    @Override
    public String getNotificationType() {
        return "SMS";
    }
}
