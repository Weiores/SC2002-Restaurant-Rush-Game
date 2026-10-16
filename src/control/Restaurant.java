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
            if(t.isOccupied() && t.getCustomer().getStatus() == CustomerStatus.SEATED && getOrderFor(t.getCustomer()) == null){
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

    //get customers who are eating
    public List<Customer> getEatingCustomers(){
        List<Customer> eatenCustomers = new ArrayList<>();
        for(Table t : tables){
            if(t.isOccupied() && t.getCustomer().getStatus() == CustomerStatus.SERVED){
                eatenCustomers.add(t.getCustomer());
            }
        }
        return eatenCustomers;
    }

    public List<Customer> getReadytoPayCustomers(){
        List<Customer> readytoPayCustomers = new ArrayList<>();
        for(Table t: tables){
            if(t.isOccupied() && t.getCustomer().getStatus() == CustomerStatus.READY_TO_PAY){
                readytoPayCustomers.add(t.getCustomer());
            }
        }
        readytoPayCustomers.sort(Comparator.comparingInt(Customer::getReadyToPayTurn).thenComparingInt(Customer::getArrivalTurn));
        return readytoPayCustomers;
    }

    public List<Customer> getCustomersLosingSatisfaction(){
        List<Customer> satisfactionList = new ArrayList<>();
        for(Table t : tables){
            if(t.isOccupied() && t.getCustomer().getStatus() == CustomerStatus.SEATED){
                satisfactionList.add(t.getCustomer());
            }
        }
        satisfactionList.addAll(waitingQueue);
        return satisfactionList;
    }

    public Order getOrderFor(Customer c){
        return c.getOrder();
    }

    public Table getTableOf(Customer c){
        return c.getTable();
    }

    public int getRevenueCents(){
        return revenueCents;
    }

    public int getServedCount() {
        return servedCount;
    }

    public int getUnhappyDepartures() {
        return unhappyDepartures;
    }

    public int getPaidCount() {
        return paidSatisfactions.size();
    }

    public OptionalDouble getAverageSatisfaction(){
        if(paidSatisfactions.isEmpty()){
            return OptionalDouble.empty();
        }
        int sum = 0;
        for(int s : paidSatisfactions){
            sum += s;
        }
        return OptionalDouble.of((double) sum / paidSatisfactions.size());
    }

    public void addToQueue(Customer c){
        waitingQueue.add(c);
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public Chef getChef() {
        return chef;
    }

    public List<Staff> getAllStaff(){
        return List.of(waiter, chef, cashier);
    }

    //action methods
    public ActionResult seatCustomer(Customer c, Table t){
        if(!waitingQueue.contains(c)){
            return new ActionResult(false, c.getCustomerId() + " is not in waiting queue.");
        }
        if(t.isOccupied()){
            return new ActionResult(false, "Table " + t.getTableNumber() + "is already occupied.");
        }

        waitingQueue.remove(c);
        t.seatCustomer(c);
        return new ActionResult(true, c.getCustomerId() + " seated at table " + t.getTableNumber());
    }

    public ActionResult takeOrder(Customer c, Random rng, int currentTurn){
        if(c.getStatus() != CustomerStatus.SEATED){
            return new ActionResult(false, "Customer " + c.getCustomerId() + " is not seated yet.");
        }
        if(c.getOrder() != null){
            return new ActionResult(false, "Customer " + c.getCustomerId() + " already has an order.");
        }
        
        MenuItem choice = c.chooseMenuItem(menu, rng);
        Order order = new Order(c, choice, currentTurn);
        c.attachOrder(order);
        activeOrders.add(order);

        //TODO: ask to move ConsoleUI's formatMoney method into a shared class for control classes to utilise as well
        return new ActionResult(true, "Customer " + c.getCustomerId() + " has ordered " + choice.getName() + " (" + order.getPriceCents() + ").");
    }

    public ActionResult advanceOrder(Order o, int currentTurn){
        if(o.getStatus() != OrderStatus.ORDERED){
            return new ActionResult(false, "This order is not being prepared.");
        }
        boolean ready = o.advancePreparation(currentTurn);

        if(ready){
            return new ActionResult(true, "Order for " + o.getCustomer().getCustomerId() + " is READY to serve.");
        }
        else{
            return new ActionResult(true, o.getOrderId() + " for Customer " + o.getCustomer().getCustomerId() + ": " + o.getCompletedPreparationUnits() + "/" + o.getRequiredPreparationUnits() + " units done.");
        }
    }

    public ActionResult serveOrder(Order o, int currentTurn){
        if(o.getStatus() != OrderStatus.READY){
            return new ActionResult(false, "Order " + o.getOrderId() + " is not ready yet.");
        }
        if(o.getReadyTurn() >= currentTurn){
            return new ActionResult(false, "Dish was finished this turn. Serve it next turn");
        }
        o.getCustomer().markServed(currentTurn);
        servedCount++;
        return new ActionResult(true, o.getMenuItem().getName() + " successfully served to Customer " + o.getCustomer().getCustomerId());
    }

    public String processPayment(Customer c, int currentTurn){
        int bill = c.markPaid(currentTurn);
        revenueCents += bill;
        paidSatisfactions.add(c.getSatisfactionAtPayment());
        activeOrders.remove(c.getOrder());
        Table t = c.getTable();
        if(t != null){
            t.freeTable();
        }
        //TODO: ask to move ConsoleUI's formatMoney method into a shared class for control classes to utilise as well
        return "Customer " + c.getCustomerId() + " paid " + bill + ".";
    }

    public boolean processEndOfTurn(Customer c){
        if(c.applyEndOfTurn()){
            waitingQueue.remove(c);
            if(c.getOrder() != null){
                activeOrders.remove(c.getOrder());
            }
            unhappyDepartures++;
            return true;
        }
        return false;
    }
}

