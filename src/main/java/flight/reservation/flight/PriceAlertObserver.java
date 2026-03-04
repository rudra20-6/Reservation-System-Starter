package flight.reservation.flight;

import flight.reservation.Customer;

/**
 * Observer Pattern - Concrete Observer
 *
 * PriceAlertObserver fires a targeted alert when the flight price drops to or
 * below a customer-specified threshold price.  It ignores all other events.
 *
 * This is a classic "conditional observer" — it reacts only to a subset of
 * events and only when a business condition (price ≤ threshold) is met.
 * Adding this kind of specialised behaviour is trivial with the Observer
 * Pattern; without it, the same logic would have to live inside ScheduledFlight
 * and would couple the flight class to customer alert preferences.
 */
// Observer Pattern: Concrete Observer — fires a targeted alert when the price drops below a threshold
public class PriceAlertObserver implements FlightObserver {

    // The customer to alert when the target price is reached
    private final Customer customer;

    // The price threshold: alert only when newPrice ≤ targetPrice
    private final double targetPrice;

    /**
     * @param customer    the customer who set this price alert
     * @param targetPrice the maximum price at which the customer wants to be alerted
     */
    public PriceAlertObserver(Customer customer, double targetPrice) {
        this.customer = customer;
        this.targetPrice = targetPrice;
    }

    /**
     * Called by ScheduledFlight when the price changes.
     * Fires the alert only if the new price is at or below the target threshold.
     */
    @Override
    public void onPriceChanged(ScheduledFlight flight, double oldPrice, double newPrice) {
        // Observer Pattern: react to the price-changed event; only act when threshold is met
        if (newPrice <= targetPrice) {
            System.out.println("*** PRICE ALERT ***");
            System.out.println("Customer: " + customer.getName());
            System.out.printf("Flight %d is now $%.2f%n", flight.getNumber(), newPrice);
            System.out.printf("Your target price was: $%.2f%n", targetPrice);
            System.out.println("[TARGET REACHED! Book now!]");
            System.out.println("--------------------------------");
            System.out.println();
        }
    }

    /**
     * No action needed when passengers are added — this observer only cares about price.
     */
    @Override
    public void onPassengersAdded(ScheduledFlight flight, int passengersAdded, int availableSeats) {
        // Observer Pattern: no-op for this observer — we only watch price changes
    }

    /**
     * No action needed when passengers are removed — this observer only cares about price.
     */
    @Override
    public void onPassengersRemoved(ScheduledFlight flight, int passengersRemoved, int availableSeats) {
        // Observer Pattern: no-op for this observer — we only watch price changes
    }

    /** @return the price threshold that triggers this alert */
    public double getTargetPrice() {
        return targetPrice;
    }

    /** @return the customer associated with this price alert */
    public Customer getCustomer() {
        return customer;
    }
}
