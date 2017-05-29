package org.halvors.atomicscience.common.base;

import org.halvors.electrometrics.common.util.LanguageUtils;
import org.halvors.electrometrics.common.util.render.Color;

public class Tier {
	public enum Base {
		BASIC("Basic", Color.BRIGHT_GREEN),
		ADVANCED("Advanced", Color.DARK_RED),
		ELITE("Elite", Color.DARK_BLUE),
		ULTIMATE("Ultimate", Color.PURPLE);

		private final String name;
		private final Color color;

		Base(String name, Color color) {
			this.name = name;
			this.color = color;
		}

		public String getUnlocalizedName() {
			return name;
		}

		public String getLocalizedName() {
			return LanguageUtils.localize("tier." + name);
		}

		public Color getColor() {
			return color;
		}
	}

	public enum Electric {
		BASIC(5000000, 2000), // 800 J
		ADVANCED(20000000, 8000), // 3200 J
		ELITE(80000000, 32000), // 12800 J
		ULTIMATE(320000000, 128000); // 51200 J

		private final int capacity;
		private final int maxTransfer;

		Electric(int capacity, int maxTransfer) {
			this.capacity = capacity;
			this.maxTransfer = maxTransfer;
		}

		public Base getBase() {
			return Base.values()[ordinal()];
		}

		public int getCapacity() {
			return capacity;
		}

		public int getMaxTransfer() {
			return maxTransfer;
		}

		public MachineType getMachineType() {
			return MachineType.values()[ordinal()];
		}

		public static Electric getFromMachineType(MachineType machineType) {
			switch (machineType) {
				case BASIC_ELECTRICITY_METER:
				case ADVANCED_ELECTRICITY_METER:
				case ELITE_ELECTRICITY_METER:
				case ULTIMATE_ELECTRICITY_METER:
					return values()[machineType.getMetadata()];

				default:
					return null;
			}
		}
	}
}