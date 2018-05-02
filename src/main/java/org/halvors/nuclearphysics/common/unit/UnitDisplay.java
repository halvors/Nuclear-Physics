package org.halvors.nuclearphysics.common.unit;

import org.halvors.nuclearphysics.common.ConfigurationManager.General;

public class UnitDisplay {
    public static String getEnergyDisplay(final double energy) {
        final EnumElectricUnit unit = General.electricUnit;

        switch (unit) {
            case TESLA:
                return UnitDisplay.getDisplayShort(energy * General.toTesla, unit);

            case JOULES:
                return UnitDisplay.getDisplayShort(energy * General.toJoules, unit);
        }

        return getDisplayShort(energy, unit);
    }

    public static String getTemperatureDisplay(final double temperature) {
        return getDisplayShort(temperature, General.temperatureUnit);
    }

    public enum EnumPrefix {
        FEMTO("Femto", "f", 0.000000000000001D),
        PICO("Pico", "p", 0.000000000001D),
        NANO("Nano", "n", 0.000000001D),
        MICRO("Micro", "u", 0.000001D),
        MILLI("Milli", "m", 0.001D),
        BASE("", "", 1),
        KILO("Kilo", "k", 1000D),
        MEGA("Mega", "M", 1000000D),
        GIGA("Giga", "G", 1000000000D),
        TERA("Tera", "T", 1000000000000D),
        PETA("Peta", "P", 1000000000000000D),
        EXA("Exa", "E", 1000000000000000000D),
        ZETTA("Zetta", "Z", 1000000000000000000000D),
        YOTTA("Yotta", "Y", 1000000000000000000000000D);

        public final String name;
        public final String symbol;
        public final double value;

        EnumPrefix(final String name, final String symbol, final double value) {
            this.name = name;
            this.symbol = symbol;
            this.value = value;
        }

        public String getName(final boolean isShort) {
            if (isShort) {
                return symbol;
            } else {
                return name;
            }
        }

        public double process(final double value) {
            return value / value;
        }

        public boolean above(final double value) {
            return value > value;
        }

        public boolean below(final double value) {
            return value < value;
        }
    }

    /**
     * Displays the unit as text. Does handle negative numbers, and will place a negative sign in
     * front of the output string showing this. Use string.replace to remove the negative sign if
     * unwanted
     */
    public static String getDisplay(double value, final EnumElectricUnit unit, final int decimalPlaces, final boolean isShort) {
        String unitName = unit.getName();
        String prefix = "";

        if (value < 0) {
            value = Math.abs(value);
            prefix = "-";
        }

        if (isShort) {
            unitName = unit.getSymbol();
        } else if (value > 1) {
            unitName = unit.getPlural();
        }

        if (value == 0) {
            return value + " " + unitName;
        } else {
            for (int i = 0; i < EnumPrefix.values().length; i++) {
                final EnumPrefix lowerMeasure = EnumPrefix.values()[i];

                if (lowerMeasure.below(value) && lowerMeasure.ordinal() == 0) {
                    return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(isShort) + unitName;
                }

                if (lowerMeasure.ordinal() + 1 >= EnumPrefix.values().length) {
                    return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(isShort) + unitName;
                }

                final EnumPrefix upperMeasure = EnumPrefix.values()[i + 1];

                if ((lowerMeasure.above(value) && upperMeasure.below(value)) || lowerMeasure.value == value) {
                    return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(isShort) + unitName;
                }
            }
        }

        return prefix + roundDecimals(value, decimalPlaces) + " " + unitName;
    }

    public static String getDisplayShort(final double value, final EnumElectricUnit unit) {
        return getDisplay(value, unit, 2, true);
    }

    public static String getDisplayShort(final double value, final EnumElectricUnit unit, final int decimalPlaces) {
        return getDisplay(value, unit, decimalPlaces, true);
    }

    public static String getDisplaySimple(final double value, final EnumElectricUnit unit, final int decimalPlaces) {
        if (value > 1) {
            if (decimalPlaces < 1) {
                return (int) value + " " + unit.getPlural();
            }

            return roundDecimals(value, decimalPlaces) + " " + unit.getPlural();
        }

        if (decimalPlaces < 1) {
            return (int) value + " " + unit.getName();
        }

        return roundDecimals(value, decimalPlaces) + " " + unit.getName();
    }

    public static String getDisplay(double value, final EnumTemperatureUnit unit, final int decimalPlaces, final boolean shift, final boolean isShort) {
        String unitName = unit.getName();
        String prefix = "";

        value = unit.convertFromK(value, shift);

        if (value < 0) {
            value = Math.abs(value);
            prefix = "-";
        }

        if (isShort) {
            unitName = unit.getSymbol();
        }

        if (value == 0) {
            return value + (isShort ? "" : " ") + unitName;
        } else {
            for (int i = 0; i < EnumPrefix.values().length; i++) {
                final EnumPrefix lowerMeasure = EnumPrefix.values()[i];

                if (lowerMeasure.below(value) && lowerMeasure.ordinal() == 0) {
                    return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + (isShort ? "" : " ") + lowerMeasure.getName(isShort) + unitName;
                }

                if (lowerMeasure.ordinal() + 1 >= EnumPrefix.values().length) {
                    return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + (isShort ? "" : " ") + lowerMeasure.getName(isShort) + unitName;
                }

                final EnumPrefix upperMeasure = EnumPrefix.values()[i + 1];

                if ((lowerMeasure.above(value) && upperMeasure.below(value)) || lowerMeasure.value == value) {
                    return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + (isShort ? "" : " ") + lowerMeasure.getName(isShort) + unitName;
                }
            }
        }

        return prefix + roundDecimals(value, decimalPlaces) + (isShort ? "" : " ") + unitName;
    }

    public static String getDisplayShort(final double value, final EnumTemperatureUnit unit) {
        return getDisplayShort(value, true, unit);
    }

    public static String getDisplayShort(final double value, final boolean shift, final EnumTemperatureUnit unit) {
        return getDisplayShort(value, unit, shift, 2);
    }

    public static String getDisplayShort(final double value, final EnumTemperatureUnit unit, final boolean shift, final int decimalPlaces) {
        return getDisplay(value, unit, decimalPlaces, shift, true);
    }

    public static double roundDecimals(final double value, final int decimalPlaces) {
        int j = (int) (value * Math.pow(10, decimalPlaces));
        return j / Math.pow(10, decimalPlaces);
    }

    public static double roundDecimals(final double value) {
        return roundDecimals(value, 2);
    }
}
