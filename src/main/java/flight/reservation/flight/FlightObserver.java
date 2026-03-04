package flight.reservation.flight;

/**
 * Observer Pattern - Observer Interface
 *
 * This is the Observer interface in the Observer Pattern.
 * Any class that wants to be notified of changes to a ScheduledFlight
 * must implement this interface.
 *
 * The three event types correspond to meaningful state changes in ScheduledFlight:
 *   1. Price changes (setCurrentPrice was called)
 *   2. Passengers added (addPassengers was called)
 *   3. Passengers removed (removePassengers was called)
 *
 * Implementing this interface decouples the notification logic from
 * ScheduledFlight. New observer types (SMS, analytics, push notifications)
 * can be added without modifying ScheduledFlight at all (Open/Closed Principle).
 */
public interface FlightObserver {

    /**
     * Called when the price of a scheduled flight changes.
     *
     * @param flight    the ScheduledFlight whose price changed (the Subject)
     * @param oldPrice  the price before the change
     * @param newPrice  the price after the change
     */
    void onPriceChanged(ScheduledFlight flight, double oldPrice, double newPrice);

    /**
     * Called when passengers are added to a scheduled flight.
     *
     * @param flight            the ScheduledFlight passengers were added to
     * @param passengersAdded   the number of passengers added
     * @param availableSeats    the number of seats still available after the addition
     */
    void onPassengersAdded(ScheduledFlight flight, int passengersAdded, int availableSeats);

    /**
     * Called when passengers are removed from a scheduled flight.
     *
     * @param flight             the ScheduledFlight passengers were removed from
     * @param passengersRemoved  the number of passengers removed
     * @param availableSeats     the number of seats available after the removal
     */
    void onPassengersRemoved(ScheduledFlight flight, int passengersRemoved, int availableSeats);
}
