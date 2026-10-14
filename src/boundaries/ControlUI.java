package boundaries;

import java.io.PrintStream;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class ControlUI {
    private static final String DIVIDER =
            "------------------------------------------------------------";

    private final InputReader inputReader;
    private final PrintStream output;

    public ControlUI() {
        this(new InputReader(), System.out);
    }

    public ControlUI(InputReader inputReader) {
        this(inputReader, System.out);
    }

    // This constructor makes the class easier to test.
    public ControlUI(
            InputReader inputReader,
            PrintStream output) {

        this.inputReader = Objects.requireNonNull(
                inputReader,
                "inputReader cannot be null");

        this.output = Objects.requireNonNull(
                output,
                "output cannot be null");
    }

    public void showWelcome(long seed) {
        output.println(DIVIDER);
        output.println("                    RESTAURANT RUSH");
        output.println(DIVIDER);
        output.println("You are the restaurant manager.");
        output.println(
                "Choose how the Waiter and Chef use their limited time.");
        output.println("Game seed: " + seed);
    }

    /*
     * Displays all information required at the beginning of a turn.
     *
     * Example customer status:
     * "Customer 1 | satisfaction 92 | SEATED | no order"
     */
    public void showTurnStart(
            int turnNumber,
            int maximumTurns,
            int revenueCents,
            Double averagePaidSatisfaction,
            List<String> waitingQueue,
            List<String> occupiedTables,
            List<String> customerStates) {

        requireList(waitingQueue, "waitingQueue");
        requireList(occupiedTables, "occupiedTables");
        requireList(customerStates, "customerStates");

        output.println();
        output.println(DIVIDER);
        output.printf(
                "TURN %d OF %d%n",
                turnNumber,
                maximumTurns);
        output.println(DIVIDER);

        output.println(
                "Revenue: " + formatMoney(revenueCents));

        output.println(
                "Average satisfaction of paid customers: "
                        + formatAverageSatisfaction(
                                averagePaidSatisfaction));

        printList(
                "Waiting queue",
                waitingQueue,
                "(empty)");

        printList(
                "Occupied tables",
                occupiedTables,
                "(none)");

        printList(
                "Customer status",
                customerStates,
                "(no active customers)");
    }

    public MenuOption askWaiterTask(
            List<MenuOption> availableOptions) {

        return askTask("Waiter", availableOptions);
    }

    public MenuOption askChefTask(
            List<MenuOption> availableOptions) {

        return askTask("Chef", availableOptions);
    }

    /*
     * The controller supplies the currently available actions.
     * The availableOptions list must also contain a Wait option.
     */
    public MenuOption askTask(
            String staffName,
            List<MenuOption> availableOptions) {

        requireText(staffName, "staffName");
        requireList(availableOptions, "availableOptions");

        if (availableOptions.isEmpty()) {
            throw new IllegalArgumentException(
                    "availableOptions must contain at least the Wait option");
        }

        output.println();
        output.println(staffName + " tasks available:");

        for (int index = 0;
                index < availableOptions.size();
                index++) {

            output.printf(
                    "  %d. %s%n",
                    index + 1,
                    availableOptions.get(index).getLabel());
        }

        int selectedNumber = inputReader.readChoice(
                "Choose the " + staffName + " task: ",
                availableOptions.size());

        return availableOptions.get(selectedNumber - 1);
    }

    public void showArrival(String customerDescription) {
        output.println(
                "Arrival: "
                        + requireText(
                                customerDescription,
                                "customerDescription"));
    }

    /*
     * Call this after the domain/controller has selected the
     * customer's food and created the order.
     *
     * The manager must not select the customer's food.
     */
    public void showOrderPlaced(
            String customerDescription,
            String menuItemName,
            int lockedPriceCents,
            int preparationUnits) {

        output.printf(
                "%s chose %s. Price: %s. Preparation units: %d.%n",
                requireText(
                        customerDescription,
                        "customerDescription"),
                requireText(
                        menuItemName,
                        "menuItemName"),
                formatMoney(lockedPriceCents),
                preparationUnits);
    }

    public void showEvent(String message) {
        output.println(
                requireText(message, "message"));
    }

    public void showActionSucceeded(String message) {
        output.println(
                "Completed: "
                        + requireText(message, "message"));
    }

    /*
     * The controller supplies the reason why the action failed.
     * The controller should ask the player again without marking
     * the staff member as used.
     */
    public void showActionRejected(String reason) {
        output.println(
                "Action not allowed: "
                        + requireText(reason, "reason"));

        output.println(
                "Please choose again. The staff task was not used.");
    }

    public void showTurnResults(
            int turnsUsed,
            int servedCount,
            int paidCount,
            int unhappyDepartures,
            int revenueCents,
            Double averagePaidSatisfaction) {

        showResults(
                "RESULTS AFTER TURN " + turnsUsed,
                turnsUsed,
                servedCount,
                paidCount,
                unhappyDepartures,
                revenueCents,
                averagePaidSatisfaction);
    }

    /*
     * TurnController calculates whether the player won.
     * ControlUI only displays the supplied result.
     */
    public void showFinalResult(
            boolean victory,
            int turnsUsed,
            int servedCount,
            int paidCount,
            int unhappyDepartures,
            int revenueCents,
            Double averagePaidSatisfaction) {

        output.println();
        output.println(DIVIDER);
        output.println(victory ? "VICTORY" : "DEFEAT");
        output.println(DIVIDER);

        showResults(
                "FINAL RESULTS",
                turnsUsed,
                servedCount,
                paidCount,
                unhappyDepartures,
                revenueCents,
                averagePaidSatisfaction);
    }

    /*
     * Converts integer cents to dollars with exactly two
     * decimal places.
     *
     * Example:
     * 4500 becomes $45.00
     */
    public static String formatMoney(int amountInCents) {
        long amount = amountInCents;
        String sign = amount < 0 ? "-" : "";
        long absoluteAmount = Math.abs(amount);

        return String.format(
                Locale.ROOT,
                "%s$%d.%02d",
                sign,
                absoluteAmount / 100,
                absoluteAmount % 100);
    }

    private void showResults(
            String heading,
            int turnsUsed,
            int servedCount,
            int paidCount,
            int unhappyDepartures,
            int revenueCents,
            Double averagePaidSatisfaction) {

        output.println();
        output.println(heading);
        output.println("Served count: " + servedCount);
        output.println("Paid customers: " + paidCount);
        output.println(
                "Unhappy departures: " + unhappyDepartures);
        output.println(
                "Revenue: " + formatMoney(revenueCents));

        output.println(
                "Average satisfaction of paid customers: "
                        + formatAverageSatisfaction(
                                averagePaidSatisfaction));

        output.println("Turns used: " + turnsUsed);
    }

    private void printList(
            String heading,
            List<String> items,
            String emptyMessage) {

        output.println();
        output.println(heading + ":");

        if (items.isEmpty()) {
            output.println("  " + emptyMessage);
            return;
        }

        for (String item : items) {
            output.println(
                    "  - "
                            + requireText(
                                    item,
                                    heading + " item"));
        }
    }

    private static String formatAverageSatisfaction(
            Double averagePaidSatisfaction) {

        if (averagePaidSatisfaction == null) {
            return "N/A";
        }

        return String.format(
                Locale.ROOT,
                "%.2f/100",
                averagePaidSatisfaction);
    }

    private static void requireList(
            List<?> list,
            String name) {

        Objects.requireNonNull(
                list,
                name + " cannot be null");
    }

    private static String requireText(
            String text,
            String name) {

        Objects.requireNonNull(
                text,
                name + " cannot be null");

        if (text.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    name + " cannot be blank");
        }

        return text;
    }

    /*
     * The id is used by TurnController.
     * The label is displayed to the player.
     *
     * Example:
     * id    = "SEAT:1:2"
     * label = "Seat Customer 1 at Table 2"
     */
    public static final class MenuOption {
        private final String id;
        private final String label;

        public MenuOption(
                String id,
                String label) {

            this.id = requireText(id, "id");
            this.label = requireText(label, "label");
        }

        public String getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
