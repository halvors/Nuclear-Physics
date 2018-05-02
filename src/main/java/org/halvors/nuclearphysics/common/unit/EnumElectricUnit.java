package org.halvors.nuclearphysics.common.unit;

import java.util.ArrayList;
import java.util.List;

public enum EnumElectricUnit {
    FORGE_ENERGY("Forge Energy", "FE"),
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
        return this == FORGE_ENERGY ? name : name + "s";
    }

    public static EnumElectricUnit fromName(final String name) {
        for (EnumElectricUnit unit : values()) {
            if (unit.name.equals(name)) {
                return unit;
            }
        }

        return FORGE_ENERGY;
    }

    public static EnumElectricUnit fromSymbol(final String symbol) {
        for (EnumElectricUnit unit : values()) {
            if (unit.symbol.equals(symbol)) {
                return unit;
            }
        }

        return FORGE_ENERGY;
    }

    public static List<String> getSymbols() {
        final List<String> symbols = new ArrayList<>();

        for (EnumElectricUnit unit : values()) {
            symbols.add(unit.getSymbol());
        }

        return symbols;
    }
}