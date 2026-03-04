package flight.reservation.flight;

/**
 * Observer Pattern - Concrete Observer
 *
 * AdminMonitorObserver is a Concrete Observer that collects analytics data
 * for an administrative dashboard.  It tracks:
 *   - Total price changes and the percentage change for each
 *   - Total booking events and the count of passengers added
 *   - Total cancellation events
 *
 * This class demonstrates how the Observer Pattern makes it trivial to bolt
 * on cross-cutting concerns (logging, analytics) without touching the core
 * domain classes.  ScheduledFlight has zero knowledge of this class.
 */
// Observer Pattern: Concrete Observer — collects analytics/admin data on all flight events
public class AdminMonitorObserver implements FlightObserver {

    // Name displayed in dashboard output
    private final String adminName;

    // Running counters — purely additive analytics state
    private int totalPriceChanges = 0;
    private int totalPassengerBookings = 0;
    private int totalPassengerCancellations = 0;

    /**
     * @param adminName a label identifying the admin/dashboard instance
     */
    public AdminMonitorObserver(String adminName) {
        this.adminName = adminName;
    }

    /**
     * Called by ScheduledFlight when the price changes.
     * Records the event and prints a formatted dashboard entry including the
     * percentage change and current occupancy.
     */
    @Override
    public void onPriceChanged(ScheduledFlight flight, double oldPrice, double newPrice) {
        // Observer Pattern: react to price-changed event and update analytics counters
        totalPriceChanges++;
        double percentChange = oldPrice == 0 ? 0 : ((newPrice - oldPrice) / oldPrice) * 100;

        // Attempt to read occupancy — may fail for aircraft types without capacity info
        double occupancyPercent = 0;
        try {
            int total = flight.getCapacity();
            int booked = total - flight.getAvailableCapacity();
            occupancyPercent = total == 0 ? 0 : ((double) booked / total) * 100;
        } catch (NoSuchFieldException ignored) {
            // Not all aircraft types expose capacity; silently default to 0 %
        }

        System.out.println("[ADMIN DASHBOARD - " + adminName + "]");
        System.out.println("===================================");
        System.out.println("Event: Price Change");
        System.out.println("Flight: " + flight.getNumber());
        System.out.printf("Old: $%.2f -> New: $%.2f%n", oldPrice, newPrice);
        System.out.printf("Change: %.1f%%%n", percentChange);
        System.out.println("Total Price Changes Monitored: " + totalPriceChanges);
        System.out.printf("Current Occupancy: %.1f%%%n", occupancyPercent);
        System.out.println("===================================");
        System.out.println();
    }

    /**
     * Called by ScheduledFlight when passengers are added.
     * Increments the bookings counter and logs the event.
     */
    @Override
    public void onPassengersAdded(ScheduledFlight flight, int passengersAdded, int availableSeats) {
        // Observer Pattern: react to passengers-added event and update analytics counters
        totalPassengerBookings += passengersAdded;
        System.out.println("[ADMIN - " + adminName + "] Passengers booked: "
                + passengersAdded + " on Flight " + flight.getNumber());
        System.out.println("Total bookings tracked: " + totalPassengerBookings);
        System.out.println();
    }

    /**
     * Called by ScheduledFlight when passengers are removed.
     * Increments the cancellations counter and logs the event.
     */
    @Override
    public void onPassengersRemoved(ScheduledFlight flight, int passengersRemoved, int availableSeats) {
        // Observer Pattern: react to passengers-removed event and update analytics counters
        totalPassengerCancellations += passengersRemoved;
        System.out.println("[ADMIN - " + adminName + "] Passengers cancelled: "
                + passengersRemoved + " on Flight " + flight.getNumber());
        System.out.println("Total cancellations tracked: " + totalPassengerCancellations);
        System.out.println();
    }

    // Getters for analytics data (useful in unit tests and reports)

    public int getTotalPriceChanges() {
        return totalPriceChanges;
    }

    public int getTotalPassengerBookings() {
        return totalPassengerBookings;
    }

    public int getTotalPassengerCancellations() {
        return totalPassengerCancellations;
    }

    public String getAdminName() {
        return adminName;
    }
}
