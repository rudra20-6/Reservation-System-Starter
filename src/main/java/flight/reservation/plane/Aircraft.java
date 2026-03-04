package flight.reservation.plane;

/**
 * Common interface for all aircraft types in the reservation system.
 * This interface provides a unified way to access aircraft properties,
 * eliminating the need for instanceof checks and type casting.
 */
public interface Aircraft {
    
    /**
     * @return the model name of the aircraft
     */
    String getModel();
    
    /**
     * @return the passenger capacity of the aircraft
     */
    int getPassengerCapacity();
    
    /**
     * @return the crew capacity of the aircraft
     */
    int getCrewCapacity();
}
