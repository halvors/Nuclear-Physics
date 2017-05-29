package org.halvors.atomicscience.common.util.energy;

/**
 * An easy way to display information on electricity for the client.
 * This class is borrowed from Universal Electricity and is customized to fit our use.
 *
 * @author Calclavia
 */
public class EnergyDisplay {
	private static String getDisplay(double value, EnergyUnit energyUnit, int decimalPlaces, boolean isShort) {
		return getDisplay(value, energyUnit, decimalPlaces, isShort, 1);
	}

	/**
	 * Displays the energyUnit as text. Does handle negative numbers, and will place a negative sign in
	 * front of the output string showing this. Use string.replace to remove the negative sign if
	 * unwanted
	 */
	private static String getDisplay(double value, EnergyUnit energyUnit, int decimalPlaces, boolean isShort, double multiplier) {
		String unitName = energyUnit.getName();
		String prefix = "";

		if (value < 0) {
			value = Math.abs(value);
			prefix = "-";
		}

		value *= multiplier;

		if (isShort) {
			unitName = energyUnit.getSymbol();
		} else if (value > 1) {
			unitName = energyUnit.getPluralName();
		}

		if (value == 0) {
			return value + " " + unitName;
		} else {
			for (int i = 0; i < EnergyUnit.Prefix.values().length; i++) {
                EnergyUnit.Prefix lowerMeasure = EnergyUnit.Prefix.values()[i];

				if (lowerMeasure.isBellow(value) && lowerMeasure.ordinal() == 0) {
					return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(isShort) + unitName;
				}

				if (lowerMeasure.ordinal() + 1 >= EnergyUnit.Prefix.values().length) {
					return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(isShort) + unitName;
				}

                EnergyUnit.Prefix upperMeasure = EnergyUnit.Prefix.values()[i + 1];

				if ((lowerMeasure.isAbove(value) && upperMeasure.isBellow(value)) || lowerMeasure.getValue() == value) {
					return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(isShort) + unitName;
				}
			}
		}

		return prefix + roundDecimals(value, decimalPlaces) + " " + unitName;
	}

	public static String getDisplay(double value, EnergyUnit energyUnit) {
		return getDisplay(value, energyUnit, 2, false);
	}

	public static String getDisplay(double value, EnergyUnit energyUnit, EnergyUnit.Prefix prefix) {
		return getDisplay(value, energyUnit, 2, false, prefix.getValue());
	}

	public static String getDisplayShort(double value, EnergyUnit energyUnit) {
		return getDisplay(value, energyUnit, 2, true);
	}

	/**
	 * Gets a display for the value with a energyUnit that is in the specific prefix.
	 */
	public static String getDisplayShort(double value, EnergyUnit energyUnit, EnergyUnit.Prefix prefix) {
		return getDisplay(value, energyUnit, 2, true, prefix.getValue());
	}

	public static String getDisplayShort(double value, EnergyUnit energyUnit, int decimalPlaces) {
		return getDisplay(value, energyUnit, decimalPlaces, true);
	}

	public static String getDisplaySimple(double value, EnergyUnit energyUnit, int decimalPlaces) {
		if (value > 1) {
			if (decimalPlaces < 1) {
				return (int) value + " " + energyUnit.getPluralName();
			}

			return roundDecimals(value, decimalPlaces) + " " + energyUnit.getPluralName();
		}

		if (decimalPlaces < 1) {
			return (int) value + " " + energyUnit.getName();
		}

		return roundDecimals(value, decimalPlaces) + " " + energyUnit.getName();
	}

	/**
	 * Rounds a number to a specific number place places.
	 * @param d The number
	 * @param decimalPlaces The rounded number
	 * @return the rounded number.
	 */
	private static double roundDecimals(double d, int decimalPlaces) {
		int j = (int) (d * Math.pow(10, decimalPlaces));

		return j / Math.pow(10, decimalPlaces);
	}

	public static double roundDecimals(double d) {
		return roundDecimals(d, 2);
	}
}