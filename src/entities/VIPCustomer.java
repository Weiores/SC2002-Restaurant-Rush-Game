package entities;

public final class VIPCustomer extends Customer {

    private static final int SATISFACTION_LOSS_PER_TURN = 5;

    public VIPCustomer(int arrivalTurn) {
        super(arrivalTurn);
    }

    @Override
    public int getSatisfactionLoss() {
        return SATISFACTION_LOSS_PER_TURN;
    }

    @Override
    public int getPriceCents(MenuItem menuItem) {
        return menuItem.getPriceCents() * 90 / 100;
    }

    @Override 
    public String getTypeName(){
        return "VIP";
    }
}
