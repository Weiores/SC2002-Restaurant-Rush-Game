package entities;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Customer {
    private static final AtomicInteger GENERATED_IDS = new AtomicInteger(1);

    private final int customerId;
    private final int arrivalTurn;
    private int satisfaction;
    private CustomerStatus status;
    private Order order;
    private Table table;
    private int servedTurn = -1;
    private int readyToPayTurn = -1;
    private int satisfactionAtPayment = -1;

    protected Customer(int customerId, int arrivalTurn) {
        if (customerId < 1) {
            throw new IllegalArgumentException("customerId must be positive");
        }
        if (arrivalTurn < 0) {
            throw new IllegalArgumentException("arrivalTurn cannot be negative");
        }
        this.customerId = customerId;
        GENERATED_IDS.updateAndGet(nextId -> Math.max(nextId, customerId + 1));
        this.arrivalTurn = arrivalTurn;
        this.satisfaction = 100;
        this.status = CustomerStatus.WAITING;
    }

    protected Customer(int customerId) {
        this(customerId, 0);
    }

    // Allocates a unique ID for the starter GameConfig no-argument path. 
    protected static int nextGeneratedId() {
        return GENERATED_IDS.getAndIncrement();
    }

    // Satisfaction lost at the end of each turn while waiting or seated. 
    public abstract int getSatisfactionLoss();

    // Applies this customer's Stage 1 waiting loss and abandons at zero. 
    public boolean applyEndOfTurn() {
        if (status != CustomerStatus.WAITING && status != CustomerStatus.SEATED) {
            return false;
        }

        int loss = getSatisfactionLoss();
        if (loss < 0) {
            throw new IllegalStateException("satisfaction loss cannot be negative");
        }
        satisfaction = Math.max(0, satisfaction - loss);
        if (satisfaction == 0) {
            abandon();
            return true;
        }
        return false;
    }

    // Compatibility method for callers that only need to apply the loss. 
    public void decreaseSatisfaction() {
        applyEndOfTurn();
    }

    private void abandon() {
        if (order != null && order.getStatus() != OrderStatus.PAID
                && order.getStatus() != OrderStatus.CANCELLED) {
            order.cancel();
        }
        status = CustomerStatus.LEFT;
        if (table != null) {
            table.freeTable();
        }
    }

    void markSeated(Table assignedTable) {
        if (status != CustomerStatus.WAITING) {
            throw new IllegalStateException("only a waiting customer can be seated");
        }
        if (table != null) {
            throw new IllegalStateException("customer is already assigned to a table");
        }
        table = Objects.requireNonNull(assignedTable, "table cannot be null");
        status = CustomerStatus.SEATED;
    }

    void releaseTable(Table releasedTable) {
        if (table == releasedTable) {
            table = null;
        }
    }

    void attachOrder(Order newOrder) {
        if (status != CustomerStatus.SEATED) {
            throw new IllegalStateException("customer must be seated before ordering");
        }
        if (order != null) {
            throw new IllegalStateException("customer already has an order");
        }
        if (Objects.requireNonNull(newOrder, "order cannot be null").getCustomer() != this) {
            throw new IllegalArgumentException("order belongs to a different customer");
        }
        order = newOrder;
    }

    void markServed(int turn) {
        if (status != CustomerStatus.SEATED || order == null
                || order.getStatus() != OrderStatus.READY) {
            throw new IllegalStateException("customer must have a ready order before serving");
        }
        order.markServed(turn);
        status = CustomerStatus.SERVED;
        servedTurn = turn;
    }

    /**
     * Finishes one full eating turn. Call only after the Waiter has acted,
     * so a customer served this turn cannot become ready to pay immediately.
     */
    public boolean finishEating(int currentTurn) {
        if (status != CustomerStatus.SERVED || currentTurn <= servedTurn) {
            return false;
        }
        status = CustomerStatus.READY_TO_PAY;
        readyToPayTurn = currentTurn;
        return true;
    }

    int markPaid(int turn) {
        if (status != CustomerStatus.READY_TO_PAY || order == null) {
            throw new IllegalStateException("customer is not ready to pay");
        }
        if (turn <= readyToPayTurn) {
            throw new IllegalStateException("payment starts on the turn after eating finishes");
        }
        order.markPaid(turn);
        status = CustomerStatus.PAID;
        satisfactionAtPayment = satisfaction;
        return order.getPriceCents();
    }

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

    // return -1 until the customer has paid.
    public int getSatisfactionAtPayment() {
        return satisfactionAtPayment;
    }
}
