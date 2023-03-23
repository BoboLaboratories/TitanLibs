package it.titanet.titanlibs.commons.format;

import net.md_5.bungee.config.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

public class Format {

    private static Format defaultFormat;

    private final NavigableMap<BigDecimal, String> suffixes;
    private final String format;
    private final String displayName;
    private final String symbol;

    public Format(FormatConfiguration configuration) {
        format = configuration.format();
        symbol = configuration.symbol();
        displayName = configuration.displayName();
        suffixes = configuration.shorts();
    }

    private Format() {
        this.format = "###,##0.##";
        this.symbol = "";
        this.displayName = "";
        suffixes = new TreeMap<>();
        suffixes.put(new BigDecimal("1000"), "K");
        suffixes.put(new BigDecimal("1000000"), "M");
        suffixes.put(new BigDecimal("1000000000"), "B");
        suffixes.put(new BigDecimal("1000000000000"), "T");
        suffixes.put(new BigDecimal("1000000000000000"), "Q");
    }

    public String format(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) return "-" + format(value.multiply(BigDecimal.valueOf(-1)));

        if (value.compareTo(BigDecimal.valueOf(1_000)) < 0) return value.toPlainString();

        Map.Entry<BigDecimal, String> e = suffixes.floorEntry(value);
        BigDecimal divideBy = e.getKey();
        String suffix = e.getValue();
        BigDecimal finalValue = value.divide(divideBy);
        return decimalFormat(finalValue) + suffix;
    }

    public String getDisplayName() {
        return displayName;
    }

    private String decimalFormat(BigDecimal num) {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator(',');
        dfs.setGroupingSeparator('\'');
        DecimalFormat df = new DecimalFormat(format, dfs);
        return df.format(num);
    }

    public static String applyDefaultFormat(long num) {
        return applyDefaultFormat(new BigDecimal(num));
    }

    public static String applyDefaultFormat(double num) {
        return applyDefaultFormat(BigDecimal.valueOf(num));
    }

    public static String applyDefaultFormat(BigDecimal num) {
        if (defaultFormat == null) {
            defaultFormat = new Format();
        }
        return defaultFormat.format(num);
    }


    public record FormatConfiguration(@NotNull String format,
                                      @NotNull String symbol,
                                      @NotNull String displayName,
                                      @NotNull NavigableMap<BigDecimal, String> shorts) {

        public static FormatConfiguration fromSpigotConfig(@NotNull ConfigurationSection config) {
            String format = config.getString("format", "");
            String symbol = config.getString("symbol", "");
            String displayName = config.getString("display-name", "");
            ConfigurationSection shortsSection = Objects.requireNonNull(config.getConfigurationSection("short"));
            NavigableMap<BigDecimal, String> shorts = new TreeMap<>();
            for (String suffix : shortsSection.getKeys(false)) {
                BigDecimal number = new BigDecimal(shortsSection.getString(suffix, "-1"));
                shorts.put(number, suffix);
            }
            return new FormatConfiguration(format, symbol, displayName, shorts);
        }


        public static Configuration fromBungeeConfig(Configuration section) {
            return null;
        }

    }
}