package flight.reservation.notification;

/**
 * A third-party email service with its own incompatible interface.
 * This class simulates an external library that cannot be modified.
 * Its method signatures differ from our system's NotificationService interface.
 */
public class ThirdPartyEmailService {

    /**
     * Sends an email using the third-party API.
     * Note: This method signature is incompatible with NotificationService.
     *
     * @param toAddress   the recipient email address
     * @param subject     the email subject line
     * @param body        the email body content
     * @param isHtml      whether the body is HTML formatted
     * @return a status code (0 = success, non-zero = failure)
     */
    public int sendMail(String toAddress, String subject, String body, boolean isHtml) {
        // Simulate sending email via third-party API
        if (toAddress == null || toAddress.isEmpty()) {
            System.out.println("[ThirdPartyEmailService] ERROR: Invalid email address.");
            return -1;
        }
        System.out.println("[ThirdPartyEmailService] Sending email to: " + toAddress);
        System.out.println("  Subject: " + subject);
        System.out.println("  Body: " + body);
        System.out.println("  HTML: " + isHtml);
        return 0; // 0 means success
    }

    /**
     * Check if the email service is available.
     *
     * @return true if the service is operational
     */
    public boolean isServiceAvailable() {
        return true;
    }
}
