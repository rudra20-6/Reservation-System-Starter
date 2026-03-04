package flight.reservation.flight;

/**
 * Observer Pattern - Subject Interface
 *
 * This is the Subject (Observable) interface in the Observer Pattern.
 * It defines the contract for any class that wishes to act as a Subject
 * that can be observed by FlightObserver instances.
 *
 * ScheduledFlight implements this interface, making it the Concrete Subject.
 * This interface ensures that the Subject's observer-management responsibilities
 * are clearly defined and separated from the core flight logic.
 *
 * Benefits:
 * - Loose coupling: Observers depend only on FlightObserver; subjects depend only on FlightSubject
 * - Extensibility: New Subject types can be built around this contract
 * - Testability: Can mock/stub the subject in tests using this interface
 */
public interface FlightSubject {

    /**
     * Registers a new observer to receive notifications from this subject.
     * After registration, the observer will be notified on every state change
     * (price change, passengers added/removed).
     *
     * @param observer the FlightObserver to add; must not be null
     */
    void registerObserver(FlightObserver observer);

    /**
     * Removes a previously registered observer. After removal, the observer
     * will no longer receive notifications from this subject.
     *
     * @param observer the FlightObserver to remove
     */
    void removeObserver(FlightObserver observer);

    /**
     * Notifies all currently registered observers. Called internally by the
     * subject whenever its state changes. Implementations iterate over the
     * observer list and invoke the appropriate callback on each observer.
     */
    void notifyObservers();
}
