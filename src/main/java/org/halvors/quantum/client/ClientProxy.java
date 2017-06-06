package org.halvors.quantum.client;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.client.gui.machine.GuiElectricityMeter;
import org.halvors.quantum.common.CommonProxy;
import org.halvors.quantum.common.base.MachineType;
import org.halvors.quantum.common.debug.block.BlockCreativeBuilder;
import org.halvors.quantum.common.debug.gui.GuiCreativeBuilder;
import org.halvors.quantum.common.reactor.GuiReactorCell;
import org.halvors.quantum.common.reactor.TileReactorCell;
import org.halvors.quantum.common.tile.machine.TileEntityElectricityMeter;
import org.halvors.quantum.common.tile.machine.TileEntityMachine;
import org.halvors.quantum.common.transform.vector.Vector3;

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
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		Block block = world.getBlock(x, y, z);

		Quantum.getLogger().info("Called gui!!!!!!!!!!!!!!!!!!");

		if (block instanceof BlockCreativeBuilder) {
			return new GuiCreativeBuilder(new Vector3(x, y, z));
		}

		if (tileEntity instanceof TileReactorCell) {
			Quantum.getLogger().info("Called gui tilereactorcell!!!!!!!!!!!!!!!!!!");

			return new GuiReactorCell(player, (TileReactorCell) tileEntity);
		}

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