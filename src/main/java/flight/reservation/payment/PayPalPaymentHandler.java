package flight.reservation.payment;

import flight.reservation.order.FlightOrder;

/**
 * Concrete handler for PayPal payments.
 * Part of the Chain of Responsibility pattern.
 */
public class PayPalPaymentHandler extends PaymentHandler {
    
    private final String email;
    private final String password;
    
    public PayPalPaymentHandler(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    @Override
    protected boolean canHandle() {
        return email != null && password != null && 
               email.equals(Paypal.DATA_BASE.get(password));
    }
    
    @Override
    protected boolean process(FlightOrder order) throws IllegalStateException {
        if (order.isClosed()) {
            return true;
        }
        
        if (!canHandle()) {
            throw new IllegalStateException("Payment information is not set or not valid.");
        }
        
        System.out.println("Paying " + order.getPrice() + " using PayPal.");
        order.setClosed();
        return true;
    }
}
