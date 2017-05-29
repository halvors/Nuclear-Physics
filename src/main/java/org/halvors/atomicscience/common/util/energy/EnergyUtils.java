package org.halvors.atomicscience.common.util.energy;

import org.halvors.electrometrics.common.ConfigurationManager.Client;
import org.halvors.electrometrics.common.ConfigurationManager.General;

public class EnergyUtils {
	/**
	 * Converts the energy to the default energy system.
	 * @param energy the raw energy.
	 * @return the energy as a String.
	 */
	public static String getEnergyDisplay(double energy) {
		EnergyUnit energyUnit = Client.energyUnit;
		double multiplier = 1;

		switch (energyUnit) {
			case JOULES:
				multiplier = General.toJoules;
				break;

			case ELECTRICAL_UNITS:
				multiplier = General.toElectricalUnits;
				break;
		}

		return EnergyDisplay.getDisplayShort(energy * multiplier, energyUnit);
	}
}
