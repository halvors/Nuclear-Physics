package org.halvors.nuclearphysics.common.science.unit;

import java.util.ArrayList;
import java.util.List;

public enum EnumTemperatureUnit {
    KELVIN("Kelvin", "K", 0, 1),
    CELSIUS("Celsius", "°C", 273.15, 1),
    FAHRENHEIT("Fahrenheit", "°F", 459.67, 9D / 5D);

    private final String name;
    private final String symbol;
    private final double zeroOffset;
    private final double intervalSize;

    EnumTemperatureUnit(final String name, final String symbol, final double zeroOffset, final  double intervalSize) {
        this.name = name;
        this.symbol = symbol;
        this.zeroOffset = zeroOffset;
        this.intervalSize = intervalSize;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getZeroOffset() {
        return zeroOffset;
    }

    public double getIntervalSize() {
        return intervalSize;
    }

    public double convertFromKelvin(final double T, final boolean shift) {
        return (T * intervalSize) - (shift ? zeroOffset : 0);
    }

    public double convertToKelvin(final double T, final boolean shift) {
        return (T + (shift ? zeroOffset : 0)) / intervalSize;
    }

    public static EnumTemperatureUnit fromName(final String name) {
        for (EnumTemperatureUnit unit : values()) {
            if (unit.getName().equals(name)) {
                return unit;
            }
        }

        return KELVIN;
    }

    public static EnumTemperatureUnit fromSymbol(final String symbol) {
        for (EnumTemperatureUnit unit : values()) {
            if (unit.getSymbol().equals(symbol)) {
                return unit;
            }
        }

        return KELVIN;
    }

    public static List<String> getSymbols() {
        final List<String> symbols = new ArrayList<>();

        for (EnumTemperatureUnit unit : values()) {
            symbols.add(unit.getSymbol());
        }

        return symbols;
    }
}