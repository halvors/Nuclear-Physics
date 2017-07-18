package org.halvors.quantum.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.halvors.quantum.common.container.machine.ContainerChemicalExtractor;
import org.halvors.quantum.common.container.machine.ContainerGasCentrifuge;
import org.halvors.quantum.common.container.machine.ContainerNuclearBoiler;
import org.halvors.quantum.common.container.machine.ContainerQuantumAssembler;
import org.halvors.quantum.common.container.particle.ContainerAccelerator;
import org.halvors.quantum.common.container.reactor.fission.ContainerReactorCell;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;
import org.halvors.quantum.common.tile.machine.TileGasCentrifuge;
import org.halvors.quantum.common.tile.machine.TileNuclearBoiler;
import org.halvors.quantum.common.tile.machine.TileQuantumAssembler;
import org.halvors.quantum.common.tile.particle.TileAccelerator;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;

/**
 * This is the common proxy used by both the client and the server.
 *
 * @author halvors
 */
public class CommonProxy implements IGuiHandler {
	public void preInit() {

	}

	public void init() {

	}

	public void postInit() {

	}

	public void registerItemRenderer(Item item, int metadata, String id) {

	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

		if (tile instanceof TileAccelerator) {
			return new ContainerAccelerator(player.inventory, (TileAccelerator) tile);
		} else if (tile instanceof TileGasCentrifuge) {
			return new ContainerGasCentrifuge(player.inventory, (TileGasCentrifuge) tile);
		} else if (tile instanceof TileChemicalExtractor) {
			return new ContainerChemicalExtractor(player.inventory, (TileChemicalExtractor) tile);
		} else if (tile instanceof TileNuclearBoiler) {
			return new ContainerNuclearBoiler(player.inventory, (TileNuclearBoiler) tile);
		} else if (tile instanceof TileQuantumAssembler) {
			return new ContainerQuantumAssembler(player.inventory, (TileQuantumAssembler) tile);
		} else if (tile instanceof TileReactorCell) {
			return new ContainerReactorCell(player.inventory, (TileReactorCell) tile);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	public EntityPlayer getPlayer(MessageContext context) {
		return context.getServerHandler().playerEntity;
	}
}