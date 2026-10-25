package entities;

public final class CriticCustomer extends Customer {

    private static final int SATISFACTION_LOSS_PER_TURN = 12;

    public CriticCustomer(int arrivalTurn) {
        super(arrivalTurn);
    }

    @Override
    public int getSatisfactionLoss() {
        return SATISFACTION_LOSS_PER_TURN;
    }

    @Override 
    public String getTypeName(){
        return "Critic";
    }
}
