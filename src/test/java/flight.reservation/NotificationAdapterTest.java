package flight.reservation;

import flight.reservation.flight.Flight;
import flight.reservation.flight.Schedule;
import flight.reservation.flight.ScheduledFlight;
import flight.reservation.notification.*;
import flight.reservation.order.FlightOrder;
import flight.reservation.plane.AircraftFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Adapter Pattern - Notification Tests")
public class NotificationAdapterTest {

    @Nested
    @DisplayName("EmailNotificationAdapter")
    class EmailAdapterTests {

        private ThirdPartyEmailService emailService;
        private EmailNotificationAdapter adapter;

        @BeforeEach
        void setup() {
            emailService = new ThirdPartyEmailService();
            adapter = new EmailNotificationAdapter(emailService);
        }

        @Test
        @DisplayName("should adapt sendNotification to sendMail and return true for valid recipient")
        void shouldSendEmailSuccessfully() {
            boolean result = adapter.sendNotification("test@example.com", "Your booking is confirmed.");
            assertTrue(result);
        }

        @Test
        @DisplayName("should return false for null recipient")
        void shouldFailForNullRecipient() {
            boolean result = adapter.sendNotification(null, "Message");
            assertFalse(result);
        }

        @Test
        @DisplayName("should return false for empty recipient")
        void shouldFailForEmptyRecipient() {
            boolean result = adapter.sendNotification("", "Message");
            assertFalse(result);
        }

        @Test
        @DisplayName("should report notification type as Email")
        void shouldReportEmailType() {
            assertEquals("Email", adapter.getNotificationType());
        }

        @Test
        @DisplayName("should use custom subject when provided")
        void shouldUseCustomSubject() {
            EmailNotificationAdapter customAdapter = new EmailNotificationAdapter(emailService, "Custom Subject");
            boolean result = customAdapter.sendNotification("test@example.com", "Body text");
            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("SMSNotificationAdapter")
    class SMSAdapterTests {

        private ThirdPartySMSService smsService;
        private SMSNotificationAdapter adapter;

        @BeforeEach
        void setup() {
            smsService = new ThirdPartySMSService();
            adapter = new SMSNotificationAdapter(smsService);
        }

        @Test
        @DisplayName("should adapt sendNotification to dispatchSMS and return true for valid recipient")
        void shouldSendSMSSuccessfully() {
            boolean result = adapter.sendNotification("+1234567890", "Your flight is confirmed.");
            assertTrue(result);
        }

        @Test
        @DisplayName("should return false for null phone number")
        void shouldFailForNullPhoneNumber() {
            boolean result = adapter.sendNotification(null, "Message");
            assertFalse(result);
        }

        @Test
        @DisplayName("should return false for empty phone number")
        void shouldFailForEmptyPhoneNumber() {
            boolean result = adapter.sendNotification("", "Message");
            assertFalse(result);
        }

        @Test
        @DisplayName("should report notification type as SMS")
        void shouldReportSMSType() {
            assertEquals("SMS", adapter.getNotificationType());
        }

        @Test
        @DisplayName("should use custom priority when provided")
        void shouldUseCustomPriority() {
            SMSNotificationAdapter highPriority = new SMSNotificationAdapter(smsService, 1);
            boolean result = highPriority.sendNotification("+1234567890", "Urgent notification");
            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("OrderNotifier with Adapters")
    class OrderNotifierTests {

        private OrderNotifier notifier;
        private ThirdPartyEmailService emailService;
        private ThirdPartySMSService smsService;
        private Schedule schedule;
        private Customer customer;
        private Flight flight;

        @BeforeEach
        void setup() {
            emailService = new ThirdPartyEmailService();
            smsService = new ThirdPartySMSService();
            notifier = new OrderNotifier();

            // Setup flight for order creation
            Airport departure = new Airport("John F. Kennedy International Airport", "JFK",
                    "Queens, New York, New York");
            Airport arrival = new Airport("Berlin Airport", "BER", "Berlin, Berlin");
            flight = new Flight(1, departure, arrival, AircraftFactory.createPassengerPlane("A380"));
            schedule = new Schedule();
            Date departureDate = TestUtil.addDays(Date.from(Instant.now()), 3);
            schedule.scheduleFlight(flight, departureDate);

            customer = new Customer("Max Mustermann", "max@example.com");
        }

        @Test
        @DisplayName("should send notifications via all registered adapters")
        void shouldNotifyViaAllServices() {
            notifier.addNotificationService(new EmailNotificationAdapter(emailService));
            notifier.addNotificationService(new SMSNotificationAdapter(smsService));

            ScheduledFlight scheduledFlight = schedule.searchScheduledFlight(flight.getNumber());
            FlightOrder order = customer.createOrder(
                    Arrays.asList("Amanda"),
                    Arrays.asList(scheduledFlight),
                    250.0
            );

            boolean result = notifier.notifyBookingConfirmation(order, "max@example.com");
            assertTrue(result);
        }

        @Test
        @DisplayName("should return false when no notification services are registered")
        void shouldReturnFalseWhenNoServices() {
            ScheduledFlight scheduledFlight = schedule.searchScheduledFlight(flight.getNumber());
            FlightOrder order = customer.createOrder(
                    Arrays.asList("Amanda"),
                    Arrays.asList(scheduledFlight),
                    250.0
            );

            boolean result = notifier.notifyBookingConfirmation(order, "max@example.com");
            assertFalse(result);
        }

        @Test
        @DisplayName("should allow adding and removing notification services")
        void shouldManageServices() {
            NotificationService emailAdapter = new EmailNotificationAdapter(emailService);
            NotificationService smsAdapter = new SMSNotificationAdapter(smsService);

            notifier.addNotificationService(emailAdapter);
            notifier.addNotificationService(smsAdapter);
            assertEquals(2, notifier.getNotificationServices().size());

            notifier.removeNotificationService(smsAdapter);
            assertEquals(1, notifier.getNotificationServices().size());
        }

        @Test
        @DisplayName("should send only email when only email adapter is registered")
        void shouldSendOnlyEmail() {
            notifier.addNotificationService(new EmailNotificationAdapter(emailService));

            ScheduledFlight scheduledFlight = schedule.searchScheduledFlight(flight.getNumber());
            FlightOrder order = customer.createOrder(
                    Arrays.asList("Amanda"),
                    Arrays.asList(scheduledFlight),
                    300.0
            );

            boolean result = notifier.notifyBookingConfirmation(order, "max@example.com");
            assertTrue(result);
            assertEquals(1, notifier.getNotificationServices().size());
        }
    }
}
