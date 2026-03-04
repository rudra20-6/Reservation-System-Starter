package flight.reservation.order;

import java.util.List;

import flight.reservation.Customer;
import flight.reservation.Passenger;
import flight.reservation.flight.ScheduledFlight;

public interface FlightOrderBuilder {
    void buildFlights(List<ScheduledFlight>flights);

void buildPrice(double price);
    void buildCustomer(Customer customer);
    void buildPassengers(List<Passenger> passengers);
    void buildIsClosed(boolean isClosed);
    FlightOrder getResult(); 
}
