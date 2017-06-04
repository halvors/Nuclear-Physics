package org.halvors.quantum.common.base;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.ConfigurationManager.Machine;
import org.halvors.quantum.common.block.BlockMachine;
import org.halvors.quantum.common.tile.machine.TileEntityElectricityMeter;
import org.halvors.quantum.common.tile.machine.TileEntityMachine;
import org.halvors.quantum.common.util.LanguageUtils;

public enum MachineType {
	BASIC_ELECTRICITY_METER(0, "ElectricityMeter", TileEntityElectricityMeter.class),
	ADVANCED_ELECTRICITY_METER(1, "ElectricityMeter", TileEntityElectricityMeter.class),
	ELITE_ELECTRICITY_METER(2, "ElectricityMeter", TileEntityElectricityMeter.class),
	ULTIMATE_ELECTRICITY_METER(3, "ElectricityMeter", TileEntityElectricityMeter.class);

	private final int metadata;
	private final String name;
	private final Class<? extends TileEntityMachine> tileEntityClass;

	MachineType(int metadata, String name, Class<? extends TileEntityMachine> tileEntityClass) {
		this.metadata = metadata;
		this.name = name;
		this.tileEntityClass = tileEntityClass;
	}

	public String getUnlocalizedName() {
		switch (this) {
			case BASIC_ELECTRICITY_METER:
			case ADVANCED_ELECTRICITY_METER:
			case ELITE_ELECTRICITY_METER:
			case ULTIMATE_ELECTRICITY_METER:
				Tier.Electric electricTier = Tier.Electric.getFromMachineType(this);
				Tier.Base baseTier = electricTier != null ? electricTier.getBase() : Tier.Base.BASIC;

				return baseTier.getUnlocalizedName() + name;

			default:
				return name;
		}
	}

	public String getLocalizedName() {
		String localizedName = LanguageUtils.localize("tile." + name + ".name");

		switch (this) {
			case BASIC_ELECTRICITY_METER:
			case ADVANCED_ELECTRICITY_METER:
			case ELITE_ELECTRICITY_METER:
			case ULTIMATE_ELECTRICITY_METER:
				Tier.Electric electricTier = Tier.Electric.getFromMachineType(this);
				Tier.Base baseTier = electricTier != null ? electricTier.getBase() : Tier.Base.BASIC;

				return baseTier.getLocalizedName() + " " + localizedName;

			default:
				return localizedName;
		}
	}

	public int getMetadata() {
		return metadata;
	}

	public TileEntityMachine getTileEntity() {
		try {
			switch (this) {
				case BASIC_ELECTRICITY_METER:
				case ADVANCED_ELECTRICITY_METER:
				case ELITE_ELECTRICITY_METER:
				case ULTIMATE_ELECTRICITY_METER:
					return tileEntityClass.getConstructor(MachineType.class, Tier.Electric.class).newInstance(this, Tier.Electric.getFromMachineType(this));

				default:
					return tileEntityClass.newInstance();
			}
		} catch(Exception e) {
			e.printStackTrace();
			Quantum.getLogger().error("Unable to indirectly create TileEntity.");
		}

		return null;
	}

	public ItemStack getItemStack() {
		return new ItemStack(Quantum.blockMachine, 1, metadata);
	}

	public Item getItem() {
		return getItemStack().getItem();
	}

	public boolean isEnabled() {
		return Machine.isEnabled(this);
	}

	public static MachineType getType(Block block, int metadata) {
		if (block instanceof BlockMachine) {
			for (MachineType machineType : values()) {
				if (metadata == machineType.getMetadata()) {
					return machineType;
				}
			}
		}

		return null;
	}

	public static MachineType getType(ItemStack itemStack) {
		return getType(Block.getBlockFromItem(itemStack.getItem()), itemStack.getMetadata());
	}
}