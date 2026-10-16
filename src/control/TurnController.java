package control;
import entities.*;
import boundaries.ControlUI;
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

    public void playTurn(){
        resetStaff();
        handleArrivals();
        waiterPhase();
        chefPhase();
        eatingAndPayment();
        endOfTurnDelay();
    }

    private void resetStaff(){
        for(Staff staff : restaurant.getAllStaff()){
            staff.resetForNewTurn();
        }
    }
    private void handleArrivals(){

    }
}
