package flight.reservation.plane;

public class PassengerDrone implements Aircraft {
    private final String model;
    private static final int PASSENGER_CAPACITY = 4;
    private static final int CREW_CAPACITY = 0;

    public PassengerDrone(String model) {
        if (model.equals("HypaHype")) {
            this.model = model;
        } else {
            throw new IllegalArgumentException(String.format("Model type '%s' is not recognized", model));
        }
    }

    @Override
    public int getPassengerCapacity() {
        return PASSENGER_CAPACITY;
    }

    @Override
    public int getCrewCapacity() {
        return CREW_CAPACITY;
    }

    @Override
    public String getModel() {
        return model;
    }
}
