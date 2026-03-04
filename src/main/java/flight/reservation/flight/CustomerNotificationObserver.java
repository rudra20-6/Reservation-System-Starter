package flight.reservation.flight;

import flight.reservation.Customer;

/**
 * Observer Pattern - Concrete Observer
 *
 * CustomerNotificationObserver is a Concrete Observer that sends email-style
 * notifications to a specific Customer whenever a flight they are watching
 * changes its price or passenger manifest.
 *
 * This class has NO knowledge of ScheduledFlight internals — it only
 * depends on the FlightObserver interface callbacks, keeping the coupling
 * between the Customer notification logic and the flight domain minimal.
 *
 * Registering/deregistering an instance with a ScheduledFlight is all that
 * is needed to start/stop notifications for that customer — no changes to
 * ScheduledFlight are ever required.
 */
// Observer Pattern: Concrete Observer — notifies a specific customer of flight changes
public class CustomerNotificationObserver implements FlightObserver {

    // The customer that this observer represents and will notify
    private final Customer customer;

    /**
     * @param customer the Customer whose notifications this observer handles
     */
    public CustomerNotificationObserver(Customer customer) {
        this.customer = customer;
    }

    /**
     * Called by ScheduledFlight when the price changes.
     * Prints an email-style notification showing the direction (increase/decrease)
     * of the price change to the associated customer.
     */
    @Override
    public void onPriceChanged(ScheduledFlight flight, double oldPrice, double newPrice) {
        // Observer Pattern: react to the price-changed event broadcast by the Subject
        double diff = newPrice - oldPrice;
        String direction = diff > 0 ? "increased" : "decreased";
        System.out.println("----------------------------------");
        System.out.println("[EMAIL TO: " + customer.getEmail() + "]");
        System.out.println("Dear " + customer.getName() + ",");
        System.out.println();
        System.out.println("Price Update for Flight " + flight.getNumber());
        System.out.println("  From: " + flight.getDeparture().getCode()
                + " -> To: " + flight.getArrival().getCode());
        System.out.printf("  Old Price: $%.2f%n", oldPrice);
        System.out.printf("  New Price: $%.2f%n", newPrice);
        if (diff > 0) {
            System.out.printf("  [!] Price %s by $%.2f%n", direction, Math.abs(diff));
        } else {
            System.out.printf("  [v] Price %s by $%.2f - Book now to save money!%n", direction, Math.abs(diff));
        }
        System.out.println("----------------------------------");
        System.out.println();
    }

    /**
     * Called by ScheduledFlight when passengers are added.
     * Informs the customer of the updated seat availability.
     */
    @Override
    public void onPassengersAdded(ScheduledFlight flight, int passengersAdded, int availableSeats) {
        // Observer Pattern: react to the passengers-added event broadcast by the Subject
        System.out.println("[" + customer.getName() + "] " + passengersAdded
                + " passenger(s) just booked Flight " + flight.getNumber()
                + ". Available seats remaining: " + availableSeats);
    }

    /**
     * Called by ScheduledFlight when passengers are removed (e.g., cancellation).
     * Informs the customer that seats have reopened.
     */
    @Override
    public void onPassengersRemoved(ScheduledFlight flight, int passengersRemoved, int availableSeats) {
        // Observer Pattern: react to the passengers-removed event broadcast by the Subject
        System.out.println("[" + customer.getName() + "] " + passengersRemoved
                + " passenger(s) cancelled on Flight " + flight.getNumber()
                + ". Available seats now: " + availableSeats);
    }

    /** @return the customer associated with this observer */
    public Customer getCustomer() {
        return customer;
    }
}
