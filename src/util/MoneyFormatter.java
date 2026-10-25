package util;

import java.util.Locale;

public final class MoneyFormatter {
    private MoneyFormatter() {
    }

    public static String format(int amountInCents) {
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
}
