package org.halvors.quantum.common;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.common.container.machine.ContainerCentrifuge;
import org.halvors.quantum.common.container.machine.ContainerChemicalExtractor;
import org.halvors.quantum.common.container.machine.ContainerNuclearBoiler;
import org.halvors.quantum.common.container.machine.ContainerQuantumAssembler;
import org.halvors.quantum.common.container.particle.ContainerAccelerator;
import org.halvors.quantum.common.container.reactor.fission.ContainerReactorCell;
import org.halvors.quantum.common.tile.machine.TileCentrifuge;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;
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
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof TileAccelerator) {
			new ContainerAccelerator(player.inventory, (TileAccelerator) tile);
		}

		if (tile instanceof TileAccelerator) {
			return new ContainerAccelerator(player.inventory, (TileAccelerator) tile);
		} else if (tile instanceof TileCentrifuge) {
			return new ContainerCentrifuge(player.inventory, (TileCentrifuge) tile);
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
}