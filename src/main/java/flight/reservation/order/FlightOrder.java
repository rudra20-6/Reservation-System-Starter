package flight.reservation.order;

import flight.reservation.Customer;
import flight.reservation.flight.ScheduledFlight;
import flight.reservation.payment.CreditCard;
import flight.reservation.payment.CreditCardPaymentHandler;
import flight.reservation.payment.PaymentHandler;
import flight.reservation.payment.PayPalPaymentHandler;
import flight.reservation.payment.Paypal;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FlightOrder extends Order {
    private final List<ScheduledFlight> flights;
    static List<String> noFlyList = Arrays.asList("Peter", "Johannes");

    public FlightOrder(List<ScheduledFlight> flights) {
        this.flights = flights;
    }

    public static List<String> getNoFlyList() {
        return noFlyList;
    }

    public List<ScheduledFlight> getScheduledFlights() {
        return flights;
    }

    private boolean isOrderValid(Customer customer, List<String> passengerNames, List<ScheduledFlight> flights) {
        boolean valid = true;
        valid = valid && !noFlyList.contains(customer.getName());
        valid = valid && passengerNames.stream().noneMatch(passenger -> noFlyList.contains(passenger));
        valid = valid && flights.stream().allMatch(scheduledFlight -> {
            try {
                return scheduledFlight.getAvailableCapacity() >= passengerNames.size();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                return false;
            }
        });
        return valid;
    }

    /**
     * Process payment using Chain of Responsibility pattern.
     * @param handler the payment handler (or chain of handlers)
     * @return true if payment successful
     * @throws IllegalStateException if payment fails
     */
    public boolean processPayment(PaymentHandler handler) throws IllegalStateException {
        return handler.handle(this);
    }

    public boolean processOrderWithCreditCardDetail(String number, Date expirationDate, String cvv) throws IllegalStateException {
        CreditCard creditCard = new CreditCard(number, expirationDate, cvv);
        return processOrderWithCreditCard(creditCard);
    }

    public boolean processOrderWithCreditCard(CreditCard creditCard) throws IllegalStateException {
        // Using Chain of Responsibility pattern
        PaymentHandler handler = new CreditCardPaymentHandler(creditCard);
        return processPayment(handler);
    }

    private boolean cardIsPresentAndValid(CreditCard card) {
        return card != null && card.isValid();
    }

    public boolean processOrderWithPayPal(String email, String password) throws IllegalStateException {
        // Using Chain of Responsibility pattern
        PaymentHandler handler = new PayPalPaymentHandler(email, password);
        return processPayment(handler);
    }
}
