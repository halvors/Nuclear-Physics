package org.halvors.nuclearphysics.common.science.unit;

import org.halvors.nuclearphysics.common.ConfigurationManager.General;

public class UnitDisplay {
    public static String getEnergyDisplay(final double energy) {
        final EnumElectricUnit unit = General.electricUnit;

        switch (unit) {
            case TESLA:
                return UnitDisplay.getDisplayShort(energy * General.toTesla, unit);

            case JOULE:
                return UnitDisplay.getDisplayShort(energy * General.toJoule, unit);
        }

        return getDisplayShort(energy, unit);
    }

    public static String getTemperatureDisplay(final double temperature) {
        return getDisplayShort(temperature, General.temperatureUnit);
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

    public static String getDisplay(double value, final EnumTemperatureUnit unit, final int decimalPlaces, final boolean shift, final boolean isShort) {
        String unitName = unit.getName();
        String prefix = "";

        value = unit.convertFromKelvin(value, shift);

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
