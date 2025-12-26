package in.kenz.travelagency.payment.domain.enums;

public enum PaymentStatus {

    CREATED,
    // Payment intent created
    // No gateway attempt yet

    RECEIVED,
    // Money successfully received by merchant
    // Booking can be CONFIRMED

    RETURNED,
    // Money was received earlier and returned to customer
    // Booking should be CANCELLED

    FAILED_CUSTOMER,
    // Payment failed
    // Money is with customer (not debited or reversed)
    // Safe to retry
    // Reconciliation MAY be needed (bank delays)

    FAILED_MERCHANT
    // Payment failed from system perspective
    // BUT money is with merchant
    // DO NOT ask customer to pay again
    // Reconciliation REQUIRED

}