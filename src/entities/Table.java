package entities;

import java.util.Objects;

public final class Table {
    private final int tableNumber;
    private Customer customer;

    public Table(int tableNumber) {
        if (tableNumber < 1) {
            throw new IllegalArgumentException("tableNumber must be positive");
        }
        this.tableNumber = tableNumber;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public boolean isOccupied() {
        return customer != null;
    }

    public void seatCustomer(Customer customer) {
        if (isOccupied()) {
            throw new IllegalStateException("table is already occupied");
        }
        Customer arrivingCustomer = Objects.requireNonNull(
                customer,
                "customer cannot be null");
        arrivingCustomer.markSeated(this);
        this.customer = arrivingCustomer;
    }

    public void freeTable() {
        if (customer != null) {
            Customer leavingCustomer = customer;
            customer = null;
            leavingCustomer.releaseTable(this);
        }
    }

    @Override
    public String toString() {
        if (isOccupied()) {
            return "Table " + tableNumber
                    + " (Occupied by Customer "
                    + customer.getCustomerId() + ")";
        }
        return "Table " + tableNumber + " (Available)";
    }
}
