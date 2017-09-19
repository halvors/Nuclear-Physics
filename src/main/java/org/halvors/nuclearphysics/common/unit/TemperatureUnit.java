package org.halvors.nuclearphysics.common.unit;

import java.util.ArrayList;
import java.util.List;

public enum TemperatureUnit {
    KELVIN("Kelvin", "K", 0, 1),
    CELSIUS("Celsius", "°C", 273.15, 1),
    FAHRENHEIT("Fahrenheit", "°F", 459.67, 9D / 5D);

    private String name;
    private String symbol;
    private double zeroOffset;
    private double intervalSize;

    TemperatureUnit(String name, String symbol, double zeroOffset, double intervalSize) {
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

    public double convertFromK(double T, boolean shift) {
        return (T * intervalSize) - (shift ? zeroOffset : 0);
    }

    public double convertToK(double T, boolean shift) {
        return (T + (shift ? zeroOffset : 0)) / intervalSize;
    }

    public static TemperatureUnit fromName(String name) {
        for (TemperatureUnit unit : values()) {
            if (unit.name.equals(name)) {
                return unit;
            }
        }

        return KELVIN;
    }

    public static TemperatureUnit fromSymbol(String symbol) {
        for (TemperatureUnit unit : values()) {
            if (unit.symbol.equals(symbol)) {
                return unit;
            }
        }

        return KELVIN;
    }

    public static List<String> getSymbols() {
        List<String> symbols = new ArrayList<>();

        for (TemperatureUnit unit : values()) {
            symbols.add(unit.getSymbol());
        }

        return symbols;
    }
}