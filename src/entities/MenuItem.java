package entities;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class MenuItem {
    public static final MenuItem SALAD = new MenuItem("Salad", 900, 1);
    public static final MenuItem BURGER = new MenuItem("Burger", 1500, 1);
    public static final MenuItem PASTA = new MenuItem("Pasta", 1800, 2);

    private static final List<MenuItem> STAGE_ONE_MENU = Collections.unmodifiableList(
            Arrays.asList(SALAD, BURGER, PASTA));

    private final String name;
    private final int priceCents;
    private final int preparationUnits;

    public MenuItem(String name, int priceCents, int preparationUnits) {
        this.name = Objects.requireNonNull(name, "name cannot be null").trim();
        if (this.name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        if (priceCents < 0) {
            throw new IllegalArgumentException("price cannot be negative");
        }
        if (preparationUnits < 1) {
            throw new IllegalArgumentException("preparation units must be positive");
        }
        this.priceCents = priceCents;
        this.preparationUnits = preparationUnits;
    }

    public static List<MenuItem> stageOneMenu() {
        return STAGE_ONE_MENU;
    }

    public String getName() {
        return name;
    }

    // Returns the price in integer cents. 
    public int getPriceCents() {
        return priceCents;
    }

    // Alias retained for compatibility with the supplied starter class. 
    public int getPrice() {
        return priceCents;
    }

    public int getPreparationUnits() {
        return preparationUnits;
    }

    // Alias retained for compatibility with the supplied starter class. 
    public int getPrepUnits() {
        return preparationUnits;
    }

    @Override
    public String toString() {
        return name;
    }
}
