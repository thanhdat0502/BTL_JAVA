package utils;

import java.text.NumberFormat;
import java.util.Locale;

public final class CurrencyUtils {
    private static final NumberFormat FORMAT=NumberFormat.getNumberInstance(new Locale("vi","VN"));
    private CurrencyUtils(){}
    public static String format(long amount){return FORMAT.format(amount)+" ₫";}
}
