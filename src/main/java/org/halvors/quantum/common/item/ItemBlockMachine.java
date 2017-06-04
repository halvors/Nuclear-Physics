package org.halvors.quantum.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.halvors.quantum.client.key.Key;
import org.halvors.quantum.client.key.KeyHandler;
import org.halvors.quantum.common.base.*;
import org.halvors.quantum.common.tile.TileEntity;
import org.halvors.quantum.common.tile.machine.TileEntityElectricityMeter;
import org.halvors.quantum.common.tile.machine.TileEntityElectricityStorage;
import org.halvors.quantum.common.util.LanguageUtils;
import org.halvors.quantum.common.util.energy.EnergyUtils;
import org.halvors.quantum.common.util.render.Color;

import java.util.List;

public class ItemBlockMachine extends ItemBlockMetadata {
	public ItemBlockMachine(Block block) {
		super(block);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		MachineType machineType = MachineType.getType(itemStack);

		return machineType.getUnlocalizedName();
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemStack) {
		MachineType machineType = MachineType.getType(itemStack);

		return machineType.getLocalizedName();
	}

	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
		MachineType machineType = MachineType.getType(itemStack);

		if (!KeyHandler.getIsKeyPressed(Key.SNEAK.getKeyBinding())) {
			list.add(LanguageUtils.localize("tooltip.hold") + " " + Color.AQUA + GameSettings.getKeyDisplayString(Key.SNEAK.getKeyBinding().getKeyCode()) + Color.GREY + " " + LanguageUtils.localize("tooltip.forDetails") + ".");
		} else {
			switch (machineType) {
				case BASIC_ELECTRICITY_METER:
				case ADVANCED_ELECTRICITY_METER:
				case ELITE_ELECTRICITY_METER:
				case ULTIMATE_ELECTRICITY_METER:
					list.add(Color.BRIGHT_GREEN + LanguageUtils.localize("tooltip.measuredEnergy") + ": " + Color.GREY + EnergyUtils.getEnergyDisplay(getElectricityCount(itemStack)));
					list.add(Color.AQUA + LanguageUtils.localize("tooltip.storedEnergy") + ": " + Color.GREY + EnergyUtils.getEnergyDisplay(getElectricityStored(itemStack)));
					break;

				default:
					list.add(Color.RED + LanguageUtils.localize("tooltip.noInformation"));
					break;
			}
		}
	}

	@Override
	public boolean placeBlockAt(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		boolean placed = super.placeBlockAt(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);

		if (placed) {
			TileEntity tileEntity = TileEntity.getTileEntity(world, x, y, z);

			if (tileEntity instanceof ITier) {
				ITier tiered = (ITier) tileEntity;
				tiered.setTier(getTier(itemStack));
			}

			if (tileEntity instanceof IElectricTier) {
				IElectricTier electricTiered = (IElectricTier) tileEntity;
				electricTiered.setElectricTier(getElectricTier(itemStack));
			}

			if (tileEntity instanceof TileEntityElectricityStorage) {
				TileEntityElectricityStorage tileEntityElectricityStorage = (TileEntityElectricityStorage) tileEntity;
				tileEntityElectricityStorage.getStorage().setEnergyStored(getElectricityStored(itemStack));
			}

			if (tileEntity instanceof TileEntityElectricityMeter) {
				TileEntityElectricityMeter tileEntityElectricityMeter = (TileEntityElectricityMeter) tileEntity;
				tileEntityElectricityMeter.setElectricityCount(getElectricityCount(itemStack));
			}
		}

		return placed;
	}

	private Tier.Base getTier(ItemStack itemStack) {
		if (itemStack.stackTagCompound != null) {
			return Tier.Base.values()[itemStack.stackTagCompound.getInteger("tier")];
		}

		return Tier.Base.BASIC;
	}

	public void setTier(ItemStack itemStack, Tier.Base tier) {
		if (itemStack.stackTagCompound == null) {
			itemStack.setTagCompound(new NBTTagCompound());
		}

		itemStack.stackTagCompound.setInteger("tier", tier.ordinal());
	}

	private Tier.Electric getElectricTier(ItemStack itemStack) {
		if (itemStack.stackTagCompound != null) {
			return Tier.Electric.values()[itemStack.stackTagCompound.getInteger("electricTier")];
		}

		return Tier.Electric.BASIC;
	}

	public void setElectricTier(ItemStack itemStack, Tier.Electric electricTier) {
		if (itemStack.stackTagCompound == null) {
			itemStack.setTagCompound(new NBTTagCompound());
		}

		itemStack.stackTagCompound.setInteger("electricTier", electricTier.ordinal());
	}

	private RedstoneControlType getRedstoneControlType(ItemStack itemStack) {
		if (itemStack.stackTagCompound != null) {
			return RedstoneControlType.values()[itemStack.stackTagCompound.getInteger("redstoneControlType")];
		}

		return RedstoneControlType.DISABLED;
	}

	public void setRedstoneControlType(ItemStack itemStack, RedstoneControlType redstoneControlType) {
		if (itemStack.stackTagCompound == null) {
			itemStack.setTagCompound(new NBTTagCompound());
		}

		itemStack.stackTagCompound.setInteger("redstoneControlType", redstoneControlType.ordinal());
	}

	private int getElectricityStored(ItemStack itemStack) {
		MachineType machineType = MachineType.getType(itemStack);

		switch (machineType) {
			case BASIC_ELECTRICITY_METER:
			case ADVANCED_ELECTRICITY_METER:
			case ELITE_ELECTRICITY_METER:
			case ULTIMATE_ELECTRICITY_METER:
				if (itemStack.stackTagCompound != null) {
					return itemStack.stackTagCompound.getInteger("electricityStored");
				}
		}

		return 0;
	}

	public void setElectricityStored(ItemStack itemStack, int electricityStored) {
		MachineType machineType = MachineType.getType(itemStack);

		switch (machineType) {
			case BASIC_ELECTRICITY_METER:
			case ADVANCED_ELECTRICITY_METER:
			case ELITE_ELECTRICITY_METER:
			case ULTIMATE_ELECTRICITY_METER:
				if (itemStack.stackTagCompound == null) {
					itemStack.setTagCompound(new NBTTagCompound());
				}

				itemStack.stackTagCompound.setInteger("electricityStored", electricityStored);
				break;
		}
	}

	private double getElectricityCount(ItemStack itemStack) {
		MachineType machineType = MachineType.getType(itemStack);

		switch (machineType) {
			case BASIC_ELECTRICITY_METER:
			case ADVANCED_ELECTRICITY_METER:
			case ELITE_ELECTRICITY_METER:
			case ULTIMATE_ELECTRICITY_METER:
				if (itemStack.stackTagCompound != null) {
					return itemStack.stackTagCompound.getDouble("electricityCount");
				}
		}

        return 0;
	}

	public void setElectricityCount(ItemStack itemStack, double electricityCount) {
		MachineType machineType = MachineType.getType(itemStack);

		switch (machineType) {
			case BASIC_ELECTRICITY_METER:
			case ADVANCED_ELECTRICITY_METER:
			case ELITE_ELECTRICITY_METER:
			case ULTIMATE_ELECTRICITY_METER:
				if (itemStack.stackTagCompound == null) {
					itemStack.setTagCompound(new NBTTagCompound());
				}

				itemStack.stackTagCompound.setDouble("electricityCount", electricityCount);
				break;
		}
	}
}
