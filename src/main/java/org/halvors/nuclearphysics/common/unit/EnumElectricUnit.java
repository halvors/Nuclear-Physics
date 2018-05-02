package org.halvors.nuclearphysics.common.unit;

import java.util.ArrayList;
import java.util.List;

public enum EnumElectricUnit {
    REDSTONE_FLUX("Redstone Flux", "RF"),
    TESLA("Tesla", "T"),
    JOULES("Joule", "J"),
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

    public String getPlural() {
        return this == REDSTONE_FLUX ? name : name + "s";
    }

    public static EnumElectricUnit fromName(final String name) {
        for (EnumElectricUnit unit : values()) {
            if (unit.name.equals(name)) {
                return unit;
            }
        }

        return REDSTONE_FLUX;
    }

    public static EnumElectricUnit fromSymbol(final String symbol) {
        for (EnumElectricUnit unit : values()) {
            if (unit.symbol.equals(symbol)) {
                return unit;
            }
        }

        return REDSTONE_FLUX;
    }

    public static List<String> getSymbols() {
        final List<String> symbols = new ArrayList<>();

        for (EnumElectricUnit unit : values()) {
            symbols.add(unit.getSymbol());
        }

        return symbols;
    }
}