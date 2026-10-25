package entities;

import java.util.Objects;


public final class Waiter extends Staff {
    public Waiter() {
        this("Waiter");
    }

    public Waiter(String name) {
        super(name);
    }

    public void seatCustomer(Customer customer, Table table) {
        Objects.requireNonNull(table, "table cannot be null").seatCustomer(customer);
    }

    public Order takeOrder(Customer customer, MenuItem item, int orderId, int turn) {
        Objects.requireNonNull(customer, "customer cannot be null");
        Order order = new Order(orderId, customer, item, turn);
        customer.attachOrder(order);
        return order;
    }

    public Order takeOrder(Customer customer, MenuItem item, int turn) {
        return takeOrder(customer, item, Order.nextGeneratedId(), turn);
    }

    public void serve(Customer customer, int turn) {
        Objects.requireNonNull(customer, "customer cannot be null").markServed(turn);
    }
}
