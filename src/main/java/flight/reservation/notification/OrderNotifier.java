package flight.reservation.notification;

import flight.reservation.order.FlightOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Sends order notifications using one or more NotificationService adapters.
 * This class is a client of the Adapter pattern — it programs to the
 * NotificationService interface and is completely decoupled from the
 * concrete third-party services (ThirdPartyEmailService, ThirdPartySMSService).
 */
public class OrderNotifier {

    private final List<NotificationService> notificationServices;

    public OrderNotifier() {
        this.notificationServices = new ArrayList<>();
    }

    /**
     * Register a notification service (adapter) for sending notifications.
     *
     * @param service the notification service to add
     */
    public void addNotificationService(NotificationService service) {
        notificationServices.add(service);
    }

    /**
     * Remove a notification service.
     *
     * @param service the notification service to remove
     */
    public void removeNotificationService(NotificationService service) {
        notificationServices.remove(service);
    }

    /**
     * Get all registered notification services.
     *
     * @return list of notification services
     */
    public List<NotificationService> getNotificationServices() {
        return notificationServices;
    }

    /**
     * Send a booking confirmation notification for the given order to the given recipient
     * through all registered notification services.
     *
     * @param order     the flight order to notify about
     * @param recipient the recipient identifier (email, phone, etc.)
     * @return true if at least one notification was sent successfully
     */
    public boolean notifyBookingConfirmation(FlightOrder order, String recipient) {
        String message = buildConfirmationMessage(order);
        boolean anySent = false;

        for (NotificationService service : notificationServices) {
            boolean sent = service.sendNotification(recipient, message);
            if (sent) {
                System.out.println("[OrderNotifier] " + service.getNotificationType()
                        + " notification sent successfully to " + recipient);
                anySent = true;
            } else {
                System.out.println("[OrderNotifier] Failed to send "
                        + service.getNotificationType() + " notification to " + recipient);
            }
        }

        return anySent;
    }

    private String buildConfirmationMessage(FlightOrder order) {
        StringBuilder sb = new StringBuilder();
        sb.append("Booking Confirmation - Order ID: ").append(order.getId()).append("\n");
        sb.append("Customer: ").append(order.getCustomer().getName()).append("\n");
        sb.append("Price: $").append(String.format("%.2f", order.getPrice())).append("\n");
        sb.append("Flights: ");
        order.getScheduledFlights().forEach(f ->
                sb.append(f.getDeparture().getCode())
                        .append(" -> ")
                        .append(f.getArrival().getCode())
                        .append("  "));
        return sb.toString();
    }
}
