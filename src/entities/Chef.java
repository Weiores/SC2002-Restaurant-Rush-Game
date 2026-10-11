package entities;
import java.util.Objects;

// advance one selected order by one
public final class Chef extends Staff {
    public Chef() {
        this("Chef");
    }

    public Chef(String name) {
        super(name);
    }

    // Returns true when this preparation unit makes the order ready.
    public boolean prepareOneUnit(Order order, int turn) {
        return Objects.requireNonNull(order, "order cannot be null")
                .advancePreparation(turn);
    }
}
