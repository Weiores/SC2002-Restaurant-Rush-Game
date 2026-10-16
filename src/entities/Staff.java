package entities;

public abstract class Staff {

    private String name;
    private boolean actedThisTurn = false;

    public Staff(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean canAct(){
        return !actedThisTurn;
    }

    public void markActed(){
        actedThisTurn = true; 
    }
    
    public void resetForNewTurn(){
        actedThisTurn = false;
    }
}
