package flight.reservation.notification;

/**
 * A third-party SMS service with its own incompatible interface.
 * This class simulates an external SMS gateway that cannot be modified.
 * Its method signatures differ from our system's NotificationService interface.
 */
public class ThirdPartySMSService {

    /**
     * Dispatches an SMS text message using the third-party gateway.
     * Note: This method signature is incompatible with NotificationService.
     *
     * @param phoneNumber the recipient phone number (with country code)
     * @param text        the SMS text content (max 160 chars)
     * @param priority    the message priority (1=high, 2=normal, 3=low)
     * @return true if the SMS was dispatched successfully
     */
    public boolean dispatchSMS(String phoneNumber, String text, int priority) {
        // Simulate sending SMS via third-party gateway
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            System.out.println("[ThirdPartySMSService] ERROR: Invalid phone number.");
            return false;
        }
        if (text != null && text.length() > 160) {
            text = text.substring(0, 157) + "...";
        }
        System.out.println("[ThirdPartySMSService] Dispatching SMS to: " + phoneNumber);
        System.out.println("  Text: " + text);
        System.out.println("  Priority: " + priority);
        return true;
    }

    /**
     * Check if the SMS gateway is reachable.
     *
     * @return true if the gateway is reachable
     */
    public boolean pingGateway() {
        return true;
    }
}
