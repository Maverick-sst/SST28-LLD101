package com.example.payments;

public class SafeCashAdapter implements PaymentGateway {
    private SafeCashClient scc;
    public SafeCashAdapter(SafeCashClient scc){
        this.scc = scc;
    }
    @Override
    public String charge(String customerId, int amountCents) {
        return scc.createPayment(amountCents, customerId).confirm();
    }
}
