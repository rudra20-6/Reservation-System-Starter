package flight.reservation.plane;

/**
 * Factory class for creating Aircraft instances.
 * Centralizes aircraft creation logic and eliminates the need for
 * instanceof checks throughout the codebase.
 */
public class AircraftFactory {

    /**
     * Create a PassengerPlane with the specified model
     * @param model the aircraft model (e.g., "A380", "A350")
     * @return an Aircraft instance (PassengerPlane)
     */
    public static Aircraft createPassengerPlane(String model) {
        return new PassengerPlane(model);
    }

    /**
     * Create a Helicopter with the specified model
     * @param model the helicopter model (e.g., "H1", "H2")
     * @return an Aircraft instance (Helicopter)
     */
    public static Aircraft createHelicopter(String model) {
        return new Helicopter(model);
    }

    /**
     * Create a PassengerDrone with the specified model
     * @param model the drone model (e.g., "HypaHype")
     * @return an Aircraft instance (PassengerDrone)
     */
    public static Aircraft createPassengerDrone(String model) {
        return new PassengerDrone(model);
    }

    /**
     * Create an aircraft based on type and model
     * @param type the aircraft type ("PLANE", "HELICOPTER", "DRONE")
     * @param model the aircraft model
     * @return an Aircraft instance
     */
    public static Aircraft createAircraft(String type, String model) {
        switch (type.toUpperCase()) {
            case "PLANE":
                return createPassengerPlane(model);
            case "HELICOPTER":
                return createHelicopter(model);
            case "DRONE":
                return createPassengerDrone(model);
            default:
                throw new IllegalArgumentException("Unknown aircraft type: " + type);
        }
    }
}
