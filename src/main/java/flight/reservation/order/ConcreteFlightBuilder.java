package flight.reservation.order;

import java.util.List;

import flight.reservation.Customer;
import flight.reservation.Passenger;
import flight.reservation.flight.ScheduledFlight;

public class ConcreteFlightBuilder implements FlightOrderBuilder {
    private FlightOrder order;

    public ConcreteFlightBuilder() {
        this.reset();
    }

    public void reset() {
        this.order = null;
    }

    @Override
    public void buildFlights(List<ScheduledFlight> flights) {
        if (order == null) {
            order = new FlightOrder(flights);
        }
    }

    @Override
    public void buildPrice(double price) {
        if (order != null) {
            order.setPrice(price);
        }
    }

    @Override
    public void buildCustomer(Customer customer) {
        if (order != null) {
            order.setCustomer(customer);
        }
    }

    @Override
    public void buildPassengers(List<Passenger> passengers) {
        if (order != null) {
            order.setPassengers(passengers);
        }
    }

    @Override
    public void buildIsClosed(boolean isClosed) {
        if (order != null && isClosed) {
            order.setClosed();
        }
    }

    @Override
    public FlightOrder getResult() {
        FlightOrder result = this.order;
        this.reset();
        return result;
    }
}