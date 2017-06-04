package org.halvors.quantum.client;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.halvors.quantum.client.gui.machine.GuiElectricityMeter;
import org.halvors.quantum.common.CommonProxy;
import org.halvors.quantum.common.base.MachineType;
import org.halvors.quantum.common.tile.TileEntity;
import org.halvors.quantum.common.tile.machine.TileEntityElectricityMeter;
import org.halvors.quantum.common.tile.machine.TileEntityMachine;

/**
 * This is the client proxy used only by the client.
 *
 * @author halvors
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = TileEntity.getTileEntity(world, x, y, z);

		if (tileEntity instanceof TileEntityMachine) {
			TileEntityMachine tileEntityMachine = (TileEntityMachine) tileEntity;
			MachineType machineType = tileEntityMachine.getMachineType();

			switch (machineType) {
				case BASIC_ELECTRICITY_METER:
				case ADVANCED_ELECTRICITY_METER:
				case ELITE_ELECTRICITY_METER:
				case ULTIMATE_ELECTRICITY_METER:
					return new GuiElectricityMeter((TileEntityElectricityMeter) tileEntity);
			}
		}

		return null;
	}
}