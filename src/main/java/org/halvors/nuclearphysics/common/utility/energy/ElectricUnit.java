package org.halvors.nuclearphysics.common.utility.energy;

import java.util.ArrayList;
import java.util.List;

public enum ElectricUnit {
    FORGE_ENERGY("Forge Energy", "FE"),
    TESLA("Tesla", "T"),
    JOULES("Joule", "J"),
    ELECTRICAL_UNITS("Electrical Unit", "EU");

    private String name;
    private String symbol;

    ElectricUnit(String name, String symbol) {
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

    public static ElectricUnit fromName(String name) {
        for (ElectricUnit unit : values()) {
            if (unit.name.equals(name)) {
                return unit;
            }
        }

        return FORGE_ENERGY;
    }

    public static ElectricUnit fromSymbol(String symbol) {
        for (ElectricUnit unit : values()) {
            if (unit.symbol.equals(symbol)) {
                return unit;
            }
        }

        return FORGE_ENERGY;
    }

    public static List<String> getSymbols() {
        List<String> symbols = new ArrayList<>();

        for (ElectricUnit unit : values()) {
            symbols.add(unit.getSymbol());
        }

        return symbols;
    }
}