package flight.reservation.order;

import java.util.List;

import flight.reservation.Customer;
import flight.reservation.Passenger;
import flight.reservation.flight.ScheduledFlight;

public class FlightOrderDirector {
    private FlightOrderBuilder builder;

    public FlightOrderDirector(FlightOrderBuilder builder) {
        this.builder = builder;
    }

    public void setBuilder(FlightOrderBuilder builder) {
        this.builder = builder;
    }


    public FlightOrder constructOrder(
            List<ScheduledFlight> flights,
            Customer customer,
            List<Passenger> passengers,
            double price) {
        
        builder.buildFlights(flights);
        builder.buildCustomer(customer);
        builder.buildPassengers(passengers);
        builder.buildPrice(price);
        builder.buildIsClosed(false);
        
        return builder.getResult();
    }


    public FlightOrder constructClosedOrder(
            List<ScheduledFlight> flights,
            Customer customer,
            List<Passenger> passengers,
            double price) {
        
        builder.buildFlights(flights);
        builder.buildCustomer(customer);
        builder.buildPassengers(passengers);
        builder.buildPrice(price);
        builder.buildIsClosed(true);
        
        return builder.getResult();
    }
}
