package flight.reservation.plane;

/**
 * Aircraft interface providing a common contract for all aircraft types.
 * Eliminates the need for instanceof checks and type casting.
 */
public interface Aircraft {
    /**
     * Get the passenger capacity of the aircraft
     * @return passenger capacity
     */
    int getPassengerCapacity();

    /**
     * Get the crew capacity of the aircraft
     * @return crew capacity
     */
    int getCrewCapacity();

    /**
     * Get the model name of the aircraft
     * @return model name
     */
    String getModel();
}
