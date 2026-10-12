package control;
import entities.*;
import java.util.*;

public class Restaurant {
    private final Queue<Customer> waitingQueue = new LinkedList<>();    //use queue for fcfs ordering
    private final List<Order> activeOrders = new ArrayList<>();
    private final List<Table> tables = new ArrayList<>();
    private final List<MenuItem> menu = new ArrayList<>();

    private final Waiter waiter;
    private final Chef chef;
    private final Cashier cashier;

    private int revenueCents = 0;
    private int servedCount = 0;
    private int unhappyDepartures = 0;
    private final List<Integer> paidSatisfactions = new ArrayList<>();

    //constructor
    public Restaurant(int numTables){
        for(int i = 1; i<=numTables; i++){
            tables.add(new Table(i));
        }
        this.waiter = new Waiter("waiter");
        this.cashier = new Cashier("cashier");
        this.chef = new Chef("chef");

        menu.addAll(MenuItem.stageOneMenu());
    }

}
