package boundaries;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public final class InputReader {
    private final Scanner scanner;
    private final PrintStream output;

    public InputReader() {
        this(new Scanner(System.in), System.out);
    }

    // This constructor makes the class easier to test.
    public InputReader(Scanner scanner, PrintStream output) {
        this.scanner = Objects.requireNonNull(
                scanner,
                "scanner cannot be null");

        this.output = Objects.requireNonNull(
                output,
                "output cannot be null");
    }

    public int readInt(String prompt) {
        while (true) {
            String input = readLine(prompt);

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                output.println(
                        "Invalid input. Please enter a whole number.");
            }
        }
    }

    public int readInt(
            String prompt,
            int minimum,
            int maximum) {

        if (minimum > maximum) {
            throw new IllegalArgumentException(
                    "minimum cannot be greater than maximum");
        }

        while (true) {
            int value = readInt(prompt);

            if (value >= minimum && value <= maximum) {
                return value;
            }

            output.printf(
                    "Invalid choice. Please enter a number from %d to %d.%n",
                    minimum,
                    maximum);
        }
    }

    public int readChoice(
            String prompt,
            int numberOfOptions) {

        if (numberOfOptions < 1) {
            throw new IllegalArgumentException(
                    "numberOfOptions must be at least 1");
        }

        return readInt(prompt, 1, numberOfOptions);
    }

    public long readLong(String prompt) {
        while (true) {
            String input = readLine(prompt);

            try {
                return Long.parseLong(input);
            } catch (NumberFormatException exception) {
                output.println(
                        "Invalid input. Please enter a whole number.");
            }
        }
    }

    public boolean readYesNo(String prompt) {
        while (true) {
            String input = readLine(prompt)
                    .toLowerCase(Locale.ROOT);

            if (input.equals("y") || input.equals("yes")) {
                return true;
            }

            if (input.equals("n") || input.equals("no")) {
                return false;
            }

            output.println(
                    "Invalid input. Please enter Y or N.");
        }
    }

    public String readNonBlank(String prompt) {
        while (true) {
            String input = readLine(prompt);

            if (!input.isEmpty()) {
                return input;
            }

            output.println(
                    "Input cannot be blank. Please try again.");
        }
    }

    private String readLine(String prompt) {
        Objects.requireNonNull(
                prompt,
                "prompt cannot be null");

        output.print(prompt);
        output.flush();

        if (!scanner.hasNextLine()) {
            throw new IllegalStateException(
                    "The input stream was closed.");
        }

        return scanner.nextLine().trim();
    }
}
