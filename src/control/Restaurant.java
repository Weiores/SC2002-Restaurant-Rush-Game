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

    //query methods
    public List<Table> getFreeTables(){
        List<Table> result = new ArrayList<>();
        for(Table t : tables){
            if(!t.isOccupied()){
                result.add(t);
            }
        }
        return result;
    }

    public List<Table> getTables(){
        return Collections.unmodifiableList(tables);
    }
    public List<MenuItem> getMenuItems(){
        return Collections.unmodifiableList(menu);
    }

    public Collection<Customer> getWaitingQueue(){
        return Collections.unmodifiableCollection(waitingQueue);
    }

    // get customers who have seated but havent ordered
    public List<Customer> getSeatedWithoutOrder(){
        List<Customer> seatedCustomers = new ArrayList<>();
        for(Table t : tables){
            if(t.isOccupied() && t.getCustomer().getStatus() == CustomerStatus.SEATED){
                seatedCustomers.add(t.getCustomer());
            }
        }
        return seatedCustomers;
    }

    // get the orders that are ready to serve
    public List<Order> getReadyOrders(){
        List<Order> readyOrders = new ArrayList<>();
        for(Order order : activeOrders){
            if(order.getStatus() == OrderStatus.READY){
                readyOrders.add(order);
            }
        }
        return readyOrders;
    }

    // getting the orders that the chef can prepare
    public List<Order> getOrdersinPrep(){
        List<Order> orderstoPrep = new ArrayList<>();
        for(Order order : activeOrders){
            if(order.getStatus() == OrderStatus.ORDERED){
                orderstoPrep.add(order);
            }
        }
        return orderstoPrep;
    }

    //get customers who have eaten but havent paid
    public List<Customer> getEatenCustomers(){
        List<Customer> eatenCustomers = new ArrayList<>();
        for(Table t : tables){
            if(t.getCustomer().getStatus() == CustomerStatus.SERVED){
                eatenCustomers.add(t.getCustomer());
            }
        }
        return eatenCustomers;
    }

}
