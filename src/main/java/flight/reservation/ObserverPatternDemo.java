package flight.reservation;

import flight.reservation.flight.AdminMonitorObserver;
import flight.reservation.flight.CustomerNotificationObserver;
import flight.reservation.flight.PriceAlertObserver;
import flight.reservation.flight.ScheduledFlight;
import flight.reservation.plane.AircraftFactory;

import java.time.Instant;
import java.util.Date;

/**
 * Observer Pattern - Usage Demonstration
 *
 * This class demonstrates the Observer Pattern as applied to ScheduledFlight.
 * It is NOT part of the production system and is provided solely as a
 * human-readable illustration of the refactoring.
 *
 * Scenarios covered:
 *   1. Register three different observer types on a single flight
 *   2. Trigger a price increase  → CustomerNotificationObserver + AdminMonitorObserver react
 *   3. Trigger a price drop that hits a threshold → PriceAlertObserver fires
 *   4. Book passengers → AdminMonitorObserver tracks the event
 *   5. Remove (unsubscribe) one observer → prove it no longer receives events
 *   6. Trigger another price change → only remaining observers are notified
 */
public class ObserverPatternDemo {

    /**
     * Entry point — runs all demonstration scenarios in sequence.
     */
    public static void main(String[] args) throws NoSuchFieldException {

        // --- Setup: Flight and Customers ---
        Airport jfk = new Airport("John F. Kennedy International Airport", "JFK", "Queens, New York, New York");
        Airport lax = new Airport("Los Angeles International Airport", "LAX", "Los Angeles, California");

        // ScheduledFlight is the Subject (Observable) in the Observer Pattern
        ScheduledFlight flight = new ScheduledFlight(
                123, jfk, lax,
                AircraftFactory.createPassengerPlane("A380"),
                Date.from(Instant.now()),
                500.00
        );

        Customer alice = new Customer("Alice", "alice@email.com");
        Customer bob   = new Customer("Bob",   "bob@email.com");

        // --- Observer Pattern: create Concrete Observer instances ---
        // Observer 1: sends email-style notifications to Alice on every event
        CustomerNotificationObserver aliceNotifier = new CustomerNotificationObserver(alice);

        // Observer 2: fires a targeted alert to Bob when the price drops to/below $450
        PriceAlertObserver bobPriceAlert = new PriceAlertObserver(bob, 450.00);

        // Observer 3: admin analytics dashboard
        AdminMonitorObserver adminMonitor = new AdminMonitorObserver("John Admin");

        // --- Observer Pattern: register all observers with the Subject ---
        System.out.println("========== REGISTERING OBSERVERS ==========");
        System.out.println();
        flight.registerObserver(aliceNotifier);  // Observer 1
        flight.registerObserver(bobPriceAlert);  // Observer 2
        flight.registerObserver(adminMonitor);   // Observer 3

        // --- Scenario 1: Price Increase ---
        System.out.println("========== SCENARIO 1: PRICE INCREASE ==========");
        System.out.println();
        // Observer Pattern: calling setCurrentPrice() automatically notifies all observers
        flight.setCurrentPrice(550.00);  // goes up by $50 — Alice and Admin react

        // --- Scenario 2: Price Drops to Bob's Target ---
        System.out.println("========== SCENARIO 2: PRICE DROPS TO TARGET ==========");
        System.out.println();
        flight.setCurrentPrice(440.00);  // drops below Bob's $450 threshold — all three react

        // --- Scenario 3: Passengers Book ---
        System.out.println("========== SCENARIO 3: PASSENGERS BOOKING ==========");
        System.out.println();
        // Observer Pattern: addPassengers() also triggers observer notifications
        flight.addPassengers(java.util.Arrays.asList(new Passenger("P1"), new Passenger("P2")));

        // --- Scenario 4: Bob Unsubscribes ---
        System.out.println("========== SCENARIO 4: BOB UNSUBSCRIBES ==========");
        System.out.println();
        // Observer Pattern: removeObserver() de-registers Bob's price alert
        flight.removeObserver(bobPriceAlert);

        // --- Scenario 5: Price Change After Bob Unsubscribed ---
        System.out.println("========== SCENARIO 5: PRICE CHANGE (BOB WON'T SEE THIS) ==========");
        System.out.println();
        flight.setCurrentPrice(420.00);  // Only Alice and Admin are still registered

        System.out.println("========== DEMO COMPLETE ==========");
    }
}
