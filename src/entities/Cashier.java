
package entities;

import java.util.Objects;

// Collects payment for a customer who has completed eating. 
public final class Cashier extends Staff {
    public Cashier() {
        this("Cashier");
    }

    public Cashier(String name) {
        super(name);
    }

    // Marks the customer and order paid, frees the table, and returns cents.
    public int collectPayment(Customer customer, int turn) {
        Customer payingCustomer = Objects.requireNonNull(
                customer,
                "customer cannot be null");
        int amount = payingCustomer.markPaid(turn);
        Table table = payingCustomer.getTable();
        if (table != null) {
            table.freeTable();
        }
        return amount;
    }
}
