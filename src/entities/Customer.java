package entities;

import java.util.Objects;

public abstract class Customer {

    private static int nextId = 1;

    private final int customerId;
    private final int arrivalTurn;

    private int satisfaction;
    private CustomerStatus status;

    private Order order;
    private Table table;

    private int servedTurn;
    private int readyToPayTurn;
    private int satisfactionAtPayment;

    protected Customer(int arrivalTurn) {
        this.customerId = nextId++;
        this.arrivalTurn = arrivalTurn;

        this.satisfaction = 100;
        this.status = CustomerStatus.WAITING;

        this.servedTurn = -1;
        this.readyToPayTurn = -1;
        this.satisfactionAtPayment = -1;
    }

    public abstract int getSatisfactionLoss();

    /**
     * Applies satisfaction loss at end of turn.
     * Returns true if customer leaves.
     */
    public boolean applyEndOfTurn() {

        if (status != CustomerStatus.WAITING
                && status != CustomerStatus.SEATED) {
            return false;
        }

        satisfaction = Math.max(
                0,
                satisfaction - getSatisfactionLoss());

        if (satisfaction == 0) {
            abandon();
            return true;
        }

        return false;
    }

    public void decreaseSatisfaction() {
        applyEndOfTurn();
    }

    private void abandon() {

        if (order != null
                && order.getStatus() != OrderStatus.PAID
                && order.getStatus() != OrderStatus.CANCELLED) {

            order.cancel();
        }

        status = CustomerStatus.LEFT;

        if (table != null) {
            table.freeTable();
        }
    }

    //Assign table
    public void markSeated(Table assignedTable) {

        if (status != CustomerStatus.WAITING) {
            throw new IllegalStateException(
                    "Customer must be waiting before seating.");
        }

        this.table = Objects.requireNonNull(assignedTable);
        this.status = CustomerStatus.SEATED;
    }

    //Take order when they are seated
    public void attachOrder(Order newOrder) {

        if (status != CustomerStatus.SEATED) {
            throw new IllegalStateException(
                    "Customer must be seated before ordering.");
        }

        if (order != null) {
            throw new IllegalStateException(
                    "Customer already has an order.");
        }

        if (newOrder.getCustomer() != this) {
            throw new IllegalArgumentException(
                    "Order belongs to another customer.");
        }

        this.order = newOrder;
    }

    public void markServed(int turn) {

        if (order == null
                || order.getStatus() != OrderStatus.READY) {

            throw new IllegalStateException(
                    "Order must be READY before serving.");
        }

        order.markServed(turn);

        status = CustomerStatus.SERVED;
        servedTurn = turn;
    }

    /**
     * Customer finishes eating one turn after being served.
     */
    public boolean finishEating(int currentTurn) {

        if (status != CustomerStatus.SERVED) {
            return false;
        }

        if (currentTurn <= servedTurn) {
            return false;
        }

        status = CustomerStatus.READY_TO_PAY;
        readyToPayTurn = currentTurn;

        return true;
    }

    public int markPaid(int currentTurn) {

        if (status != CustomerStatus.READY_TO_PAY) {
            throw new IllegalStateException(
                    "Customer is not ready to pay.");
        }

        if (currentTurn <= readyToPayTurn) {
            throw new IllegalStateException(
                    "Must wait one turn before payment.");
        }

        order.markPaid(currentTurn);

        status = CustomerStatus.PAID;
        satisfactionAtPayment = satisfaction;

        return order.getPriceCents();
    }

    public void releaseTable(Table table) {

        if (this.table == table) {
            this.table = null;
        }
    }

    //getters and setters
    public int getCustomerId() {
        return customerId;
    }

    public int getArrivalTurn() {
        return arrivalTurn;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public CustomerStatus getStatus() {
        return status;
    }
    //TODO: ask if want to change so that getters for orders go through restaurant only, if not need update 2 classes per action method
    public Order getOrder() {
        return order;
    }

    public Table getTable() {
        return table;
    }

    public int getServedTurn() {
        return servedTurn;
    }

    public int getReadyToPayTurn() {
        return readyToPayTurn;
    }

    public int getSatisfactionAtPayment() {
        return satisfactionAtPayment;
    }
}
