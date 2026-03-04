package flight.reservation.payment;

import flight.reservation.order.FlightOrder;

/**
 * Concrete handler for Credit Card payments.
 * Part of the Chain of Responsibility pattern.
 */
public class CreditCardPaymentHandler extends PaymentHandler {
    
    private final CreditCard creditCard;
    
    public CreditCardPaymentHandler(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
    
    @Override
    protected boolean canHandle() {
        return creditCard != null && creditCard.isValid();
    }
    
    @Override
    protected boolean process(FlightOrder order) throws IllegalStateException {
        if (order.isClosed()) {
            return true;
        }
        
        if (!canHandle()) {
            throw new IllegalStateException("Payment information is not set or not valid.");
        }
        
        System.out.println("Paying " + order.getPrice() + " using Credit Card.");
        double remainingAmount = creditCard.getAmount() - order.getPrice();
        
        if (remainingAmount < 0) {
            System.out.printf("Card limit reached - Balance: %f%n", remainingAmount);
            throw new IllegalStateException("Card limit reached");
        }
        
        creditCard.setAmount(remainingAmount);
        order.setClosed();
        return true;
    }
}
