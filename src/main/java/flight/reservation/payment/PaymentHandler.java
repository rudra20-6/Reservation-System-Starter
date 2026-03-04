package flight.reservation.payment;

import flight.reservation.order.FlightOrder;

/**
 * Abstract handler for the Chain of Responsibility pattern.
 * Each concrete handler will process a specific payment type.
 */
public abstract class PaymentHandler {
    
    protected PaymentHandler nextHandler;
    
    /**
     * Set the next handler in the chain
     * @param nextHandler the next handler to process if this one cannot
     * @return the next handler (for chaining)
     */
    public PaymentHandler setNext(PaymentHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }
    
    /**
     * Handle the payment request. If this handler can process it, it will.
     * Otherwise, it passes to the next handler in the chain.
     * @param order the order to process payment for
     * @return true if payment was successful
     * @throws IllegalStateException if payment fails
     */
    public boolean handle(FlightOrder order) throws IllegalStateException {
        if (canHandle()) {
            return process(order);
        } else if (nextHandler != null) {
            return nextHandler.handle(order);
        }
        throw new IllegalStateException("No payment handler could process this request.");
    }
    
    /**
     * Check if this handler can process the payment
     * @return true if this handler can process the payment
     */
    protected abstract boolean canHandle();
    
    /**
     * Process the payment
     * @param order the order to process
     * @return true if payment successful
     * @throws IllegalStateException if payment fails
     */
    protected abstract boolean process(FlightOrder order) throws IllegalStateException;
}
