package entities;

public final class RegularCustomer extends Customer {

    private static final int SATISFACTION_LOSS_PER_TURN = 8;

    public RegularCustomer(int arrivalTurn) {
        super(arrivalTurn);
    }

    @Override
    public int getSatisfactionLoss() {
        return SATISFACTION_LOSS_PER_TURN;
    }

    @Override 
    public String getTypeName(){
        return "Regular";
    }
}
