package org.halvors.nuclearphysics.common.science.unit;

import java.util.ArrayList;
import java.util.List;

public enum EnumElectricUnit {
    JOULE("Joule", "J"),
    REDSTONE_FLUX("Redstone Flux", "RF"),
    TESLA("Tesla", "T"),
    ELECTRICAL_UNITS("Electrical Unit", "EU");

    private final String name;
    private final String symbol;

    EnumElectricUnit(final String name, final String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public static EnumElectricUnit fromName(final String name) {
        for (EnumElectricUnit unit : values()) {
            if (unit.getName().equals(name)) {
                return unit;
            }
        }

        return JOULE;
    }

    public static EnumElectricUnit fromSymbol(final String symbol) {
        for (EnumElectricUnit unit : values()) {
            if (unit.getSymbol().equals(symbol)) {
                return unit;
            }
        }

        return JOULE;
    }

    public static List<String> getSymbols() {
        final List<String> symbols = new ArrayList<>();

        for (EnumElectricUnit unit : values()) {
            symbols.add(unit.getSymbol());
        }

        return symbols;
    }
}