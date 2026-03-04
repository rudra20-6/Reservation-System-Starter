package flight.reservation.flight;

import flight.reservation.Airport;
import flight.reservation.Passenger;
import flight.reservation.plane.Aircraft;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Observer Pattern - Concrete Subject
 *
 * ScheduledFlight now implements FlightSubject, making it the Observable in the
 * Observer Pattern.  It maintains a list of registered FlightObserver instances
 * and automatically notifies them whenever its state changes:
 *   - price changes  → notifyPriceChange()
 *   - passengers added   → notifyPassengersAdded()
 *   - passengers removed → notifyPassengersRemoved()
 *
 * REFACTORING NOTE (setCurrentPrice): Previously this was a simple setter with
 * no side-effects. Now it captures the old price, updates the field, and
 * broadcasts the change to all registered observers — without the calling code
 * needing to know anything about the notification mechanism.
 */
// Observer Pattern: ScheduledFlight is the Concrete Subject; it now implements FlightSubject
public class ScheduledFlight extends Flight implements FlightSubject {

    private final List<Passenger> passengers;
    private final Date departureTime;
    private double currentPrice = 100;

    // Observer Pattern: the list of registered observers (one-to-many relationship)
    private final List<FlightObserver> observers;

    public ScheduledFlight(int number, Airport departure, Airport arrival, Aircraft aircraft, Date departureTime) {
        super(number, departure, arrival, aircraft);
        this.departureTime = departureTime;
        this.passengers = new ArrayList<>();
        // Observer Pattern: initialise the observer list so it is never null
        this.observers = new ArrayList<>();
    }

    public ScheduledFlight(int number, Airport departure, Airport arrival, Aircraft aircraft, Date departureTime, double currentPrice) {
        super(number, departure, arrival, aircraft);
        this.departureTime = departureTime;
        this.passengers = new ArrayList<>();
        this.currentPrice = currentPrice;
        // Observer Pattern: initialise the observer list so it is never null
        this.observers = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // Observer Pattern — FlightSubject implementation
    // -------------------------------------------------------------------------

    /**
     * Registers an observer to be notified of future state changes on this flight.
     * Duplicate registrations of the same instance are silently ignored.
     *
     * @param observer the observer to register; must not be null
     */
    @Override
    public void registerObserver(FlightObserver observer) {
        // Observer Pattern: add to the observers list; avoid duplicates
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            System.out.println("[Observer] Observer registered for Flight " + this.getNumber());
        }
    }

    /**
     * Removes a previously registered observer.  After this call the removed
     * observer will no longer receive any notifications from this flight.
     *
     * @param observer the observer to remove
     */
    @Override
    public void removeObserver(FlightObserver observer) {
        // Observer Pattern: unsubscribe — remove from the list
        if (observers.remove(observer)) {
            System.out.println("[Observer] Observer removed from Flight " + this.getNumber());
        }
    }

    /**
     * Required by FlightSubject but intentionally left as a no-op at the
     * top level: concrete change-specific notify methods (notifyPriceChange,
     * notifyPassengersAdded, notifyPassengersRemoved) are used instead so
     * each callback receives precise event information.
     */
    @Override
    public void notifyObservers() {
        // Observer Pattern: generic hook required by the interface.
        // Specialised private helpers are used for concrete notifications.
    }

    // -------------------------------------------------------------------------
    // Observer Pattern — private notification helpers
    // -------------------------------------------------------------------------

    /**
     * Broadcasts a price-change event to all registered observers.
     *
     * @param oldPrice price before the change
     * @param newPrice price after the change
     */
    private void notifyPriceChange(double oldPrice, double newPrice) {
        // Observer Pattern: iterate over a snapshot of the list to allow
        // observers to safely unregister themselves during notification
        for (FlightObserver observer : new ArrayList<>(observers)) {
            observer.onPriceChanged(this, oldPrice, newPrice);
        }
    }

    /**
     * Broadcasts a passengers-added event to all registered observers.
     *
     * @param count          the number of passengers added
     * @param availableSeats remaining available seats after the addition
     */
    private void notifyPassengersAdded(int count, int availableSeats) {
        // Observer Pattern: notify each observer about the new booking
        for (FlightObserver observer : new ArrayList<>(observers)) {
            observer.onPassengersAdded(this, count, availableSeats);
        }
    }

    /**
     * Broadcasts a passengers-removed event to all registered observers.
     *
     * @param count          the number of passengers removed
     * @param availableSeats remaining available seats after the removal
     */
    private void notifyPassengersRemoved(int count, int availableSeats) {
        // Observer Pattern: notify each observer about the cancellation
        for (FlightObserver observer : new ArrayList<>(observers)) {
            observer.onPassengersRemoved(this, count, availableSeats);
        }
    }

    // -------------------------------------------------------------------------
    // Core flight methods (existing behaviour preserved; notifications added)
    // -------------------------------------------------------------------------

    public int getCrewMemberCapacity() throws NoSuchFieldException {
        return this.aircraft.getCrewCapacity();
    }

    /**
     * Adds passengers to this flight and notifies all registered observers.
     *
     * REFACTORING NOTE: the core booking logic is unchanged; the only addition
     * is the call to notifyPassengersAdded() at the end.
     */
    public void addPassengers(List<Passenger> passengers) {
        this.passengers.addAll(passengers);
        // Observer Pattern: notify observers that new passengers were added
        int availableSeats;
        try {
            availableSeats = getAvailableCapacity();
        } catch (NoSuchFieldException e) {
            availableSeats = -1; // unknown; observers receive -1 as sentinel
        }
        notifyPassengersAdded(passengers.size(), availableSeats);
    }

    /**
     * Removes passengers from this flight and notifies all registered observers.
     *
     * REFACTORING NOTE: the core cancellation logic is unchanged; the only
     * addition is the call to notifyPassengersRemoved() at the end.
     */
    public void removePassengers(List<Passenger> passengers) {
        this.passengers.removeAll(passengers);
        // Observer Pattern: notify observers that passengers were removed/cancelled
        int availableSeats;
        try {
            availableSeats = getAvailableCapacity();
        } catch (NoSuchFieldException e) {
            availableSeats = -1; // unknown; observers receive -1 as sentinel
        }
        notifyPassengersRemoved(passengers.size(), availableSeats);
    }

    public int getCapacity() throws NoSuchFieldException {
        return this.aircraft.getPassengerCapacity();
    }

    public int getAvailableCapacity() throws NoSuchFieldException {
        return this.getCapacity() - this.passengers.size();
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    /**
     * Updates the price of this flight and notifies all registered observers.
     *
     * REFACTORING NOTE (KEY CHANGE): This was previously a plain setter.
     * It now captures the old price, updates the field, then calls
     * notifyPriceChange() so every registered observer is informed of the
     * exact old-to-new transition — enabling use-cases like price alerts,
     * customer emails, and admin analytics without modifying this class.
     *
     * @param currentPrice the new price to set
     */
    public void setCurrentPrice(double currentPrice) {
        // Observer Pattern: capture old price before updating the field
        double oldPrice = this.currentPrice;
        this.currentPrice = currentPrice;
        // Observer Pattern: broadcast the price change to all registered observers
        notifyPriceChange(oldPrice, currentPrice);
    }
}
