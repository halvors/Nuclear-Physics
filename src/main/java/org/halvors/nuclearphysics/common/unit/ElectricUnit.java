package org.halvors.nuclearphysics.common.unit;

import java.util.ArrayList;
import java.util.List;

public enum ElectricUnit {
    REDSTONE_FLUX("Redstone Flux", "RF"),
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
        return this == REDSTONE_FLUX ? name : name + "s";
    }

    public static ElectricUnit fromName(String name) {
        for (ElectricUnit unit : values()) {
            if (unit.name.equals(name)) {
                return unit;
            }
        }

        return REDSTONE_FLUX;
    }

    public static ElectricUnit fromSymbol(String symbol) {
        for (ElectricUnit unit : values()) {
            if (unit.symbol.equals(symbol)) {
                return unit;
            }
        }

        return REDSTONE_FLUX;
    }

    public static List<String> getSymbols() {
        List<String> symbols = new ArrayList<>();

        for (ElectricUnit unit : values()) {
            symbols.add(unit.getSymbol());
        }

        return symbols;
    }
}