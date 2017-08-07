package org.halvors.quantum.common.utility.energy;

import java.util.ArrayList;
import java.util.List;

/**
 * Universal Electricity's units are in KILOJOULES, KILOWATTS and KILOVOLTS. Try to make your
 * energy ratio as close to real life as possible.
 */
public enum EnergyUnit {
    /*
    AMPERE("Amp", "I"),
    AMP_HOUR("Amp Hour", "Ah"),
    VOLTAGE("Volt", "V"),
    WATT("Watt", "W"),
    WATT_HOUR("Watt Hour", "Wh"),
    RESISTANCE("Ohm", "R"),
    CONDUCTANCE("Siemen", "S"),
    JOULES("Joule", "J"),
    LITER("Liter", "L"),
    NEWTON_METER("Newton Meter", "Nm"),
    REDFLUX("Redstone-Flux", "Rf"),
    MINECRAFT_JOULES("Minecraft-Joules", "Mj"),
    ELECTRICAL_UNITS("Electrical-Units", "Eu");
    */

	REDSTONE_FLUX("Redstone Flux", "RF"),
	JOULES("Joule", "J"),
	MINECRAFT_JOULES("Minecraft Joule", "MJ"),
	ELECTRICAL_UNITS("Electrical Unit", "EU");

	private final String name;
	private final String symbol;

	EnergyUnit(String name, String symbol) {
		this.name = name;
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getPluralName() {
		return name + "s";
	}

    public static List<String> getNames() {
        List<String> names = new ArrayList<>();

        for (EnergyUnit energyUnit : values()) {
            names.add(energyUnit.getName());
        }

        return names;
    }

    public static List<String> getSymbols() {
        List<String> symbols = new ArrayList<>();

        for (EnergyUnit energyUnit : values()) {
            symbols.add(energyUnit.getSymbol());
        }

        return symbols;
    }

    public static List<String> getPluralNames() {
        List<String> pluralNames = new ArrayList<>();

        for (EnergyUnit energyUnit : values()) {
            pluralNames.add(energyUnit.getPluralName());
        }

        return pluralNames;
    }

    public static EnergyUnit getUnitFromName(String name) {
        for (EnergyUnit energyUnit : values()) {
            if (energyUnit.name.equals(name)) {
                return energyUnit;
            }
        }

        return REDSTONE_FLUX;
    }

    public static EnergyUnit getUnitFromSymbol(String symbol) {
        for (EnergyUnit energyUnit : values()) {
            if (energyUnit.symbol.equals(symbol)) {
                return energyUnit;
            }
        }

        return REDSTONE_FLUX;
    }

    /**
     * Metric system of measurement.
     */
    public enum Prefix {
        MICRO("Micro", "u", 0.000001),
        MILLI("Milli", "m", 0.001),
        BASE("", "", 1),
        KILO("Kilo", "k", 1000),
        MEGA("Mega", "M", 1000000),
        GIGA("Giga", "G", 1000000000),
        TERA("Tera", "T", 1000000000000d),
        PETA("Peta", "P", 1000000000000000d),
        EXA("Exa", "E", 1000000000000000000d),
        ZETTA("Zetta", "Z", 1000000000000000000000d),
        YOTTA("Yotta", "Y", 1000000000000000000000000d);

        /** long name for the unit */
        private final String name;
        /** short unit version of the unit */
        private final String symbol;
        /** Point by which a number is consider to be of this unit */
        private final double value;

        Prefix(String name, String symbol, double value) {
            this.name = name;
            this.symbol = symbol;
            this.value = value;
        }

        public String getName(boolean getShort) {
            if (getShort) {
                return symbol;
            } else {
                return name;
            }
        }

        public double getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public String getSymbol() {
            return symbol;
        }

        /** Divides the value by the unit value start */
        public double process(double value) {
            return value / this.value;
        }

        /** Checks if a value is above the unit value start */
        public boolean isAbove(double value) {
            return value > this.value;
        }

        /** Checks if a value is lower than the unit value start */
        public boolean isBellow(double value) {
            return value < this.value;
        }
    }
}