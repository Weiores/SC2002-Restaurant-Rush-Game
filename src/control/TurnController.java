package control;
import entities.*;
import boundaries.ControlUI;
import boundaries.ControlUI.MenuOption;
import java.util.function.Supplier;

import java.util.*;

public class TurnController {
    private final GameConfig config;
    private final Restaurant restaurant;
    private final ControlUI ui;
    private final Random rng;

    private int currentTurn = 0;

    public TurnController(GameConfig config, ControlUI ui){
        this.config = config;
        this.ui = ui;
        this.restaurant = new Restaurant(GameConfig.NUM_TABLES);
        this.rng = new Random(config.getSeed());
    }

    public void run(){
        for(currentTurn = 1; currentTurn <= GameConfig.MAX_TURNS; currentTurn++){
            playTurn();
        }
    }

    private void playTurn(){
        resetStaff();
        handleArrivals();
        waiterPhase();
        chefPhase();
        eatingAndPayment();
        endOfTurnDecay();
    }

    private void resetStaff(){
        for(Staff staff : restaurant.getAllStaff()){
            staff.resetForNewTurn();
        }
    }

    private void handleArrivals(){
        Customer c = config.returnCustomerthisTurn(currentTurn);
        if(c != null){
            restaurant.addToQueue(c);
        }
    }

    //build waiter's options for customers
    private Map<MenuOption, Supplier<ActionResult>> buildWaiterOptions(){
        Map<MenuOption, Supplier<ActionResult>> actions = new LinkedHashMap<>();

        //seat method
        for(Customer c : restaurant.getWaitingQueue()){
            for(Table t : restaurant.getFreeTables()){
                actions.put(new MenuOption("SEAT " + c.getCustomerId() + " : " + t.getTableNumber(), "Seat Customer " + c.getCustomerId() + " at Table " + t.getTableNumber()), () -> restaurant.seatCustomer(c, t));
            }
        }

        //take order method
        for(Customer c : restaurant.getSeatedWithoutOrder()){
            actions.put(new MenuOption("ORDER " + c.getCustomerId(), "Take order from Customer " + c.getCustomerId()), ()-> restaurant.takeOrder(c, rng, currentTurn));
        }

        //serve method
        for(Order o : restaurant.getReadyOrders()){
            actions.put(new MenuOption("SERVE " + o.getOrderId(), "Serve " + o.getMenuItem().getName() + " to Customer " + o.getCustomer().getCustomerId()), ()->restaurant.serveOrder(o, currentTurn));
        }

        //wait method for when others are acting or nothing else to do
        actions.put(new MenuOption("WAIT", "Wait"), null);
        return actions;
    }

    //facilitate the waiter's option picking phase
    private void waiterPhase(){
        while(true){
            Map<MenuOption, Supplier<ActionResult>> actions = buildWaiterOptions();
            MenuOption choice = ui.askWaiterTask(new ArrayList<>(actions.keySet()));

            Supplier<ActionResult> action = actions.get(choice);
            if(action == null){
                ui.showEvent("The Waiter waits.");
                return;
            }
            ActionResult result = action.get();
            if(result.isSuccess()){
                ui.showActionSucceeded(result.getMessage());
                restaurant.getWaiter().markActed();
                return;
            }
            ui.showActionRejected(result.getMessage());
        }
    }

    //repeating same methods for Chef now
    private Map<MenuOption, Supplier<ActionResult>> buildChefOptions(){
        Map<MenuOption, Supplier<ActionResult>> actions = new LinkedHashMap<>();

        //cook method
        for(Order o : restaurant.getOrdersinPrep()){
            actions.put(new MenuOption("COOK " + o.getOrderId(), "Chef is now cooking " + o.getMenuItem().getName() + " for Customer " + o.getCustomer().getCustomerId() + " (" + o.getCompletedPreparationUnits() + "/" + o.getRequiredPreparationUnits() + ") units done."), ()->restaurant.advanceOrder(o, currentTurn));
        }
        actions.put(new MenuOption("WAIT", "Wait"), null);
        return actions;

    }

    private void chefPhase(){
        while(true){
            if (restaurant.getOrdersinPrep().isEmpty()) {
                ui.showEvent("The Chef has nothing to prepare and waits.");
                return;
            }
            Map<MenuOption, Supplier<ActionResult>> actions = buildChefOptions();
            MenuOption choice = ui.askChefTask(new ArrayList<>(actions.keySet()));

            Supplier<ActionResult> action = actions.get(choice);

            if(action == null){
                ui.showEvent("the Chef Waits.");
                return;
            }

            ActionResult result = action.get();

            if(result.isSuccess()){
                ui.showActionSucceeded(result.getMessage());
                restaurant.getChef().markActed();
                return;
            }
            ui.showActionRejected(result.getMessage());

        }
    }

    public void eatingAndPayment(){
        for(Customer c : restaurant.getEatingCustomers()){
            if(c.finishEating(currentTurn)){
                ui.showEvent("Customer " + c.getCustomerId() + " has finished eating and is ready to pay.");
            }
        }

        for(Customer c: restaurant.getReadytoPayCustomers()){
            if(c.getReadyToPayTurn() < currentTurn){
                ui.showEvent(restaurant.processPayment(c, currentTurn));
                restaurant.getCashier().markActed();
                break;
            }
        }
    }

    private void endOfTurnDecay(){
        for(Customer c : restaurant.getCustomersLosingSatisfaction()){
            if(restaurant.processEndOfTurn(c)){
                ui.showEvent("Customer " + c.getCustomerId() + " ran out of patience and left.");
                return;
            }
        }
    }



}
