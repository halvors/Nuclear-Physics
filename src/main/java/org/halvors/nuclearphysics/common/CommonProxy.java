package org.halvors.nuclearphysics.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.container.machine.ContainerChemicalExtractor;
import org.halvors.nuclearphysics.common.container.machine.ContainerGasCentrifuge;
import org.halvors.nuclearphysics.common.container.machine.ContainerNuclearBoiler;
import org.halvors.nuclearphysics.common.container.machine.ContainerQuantumAssembler;
import org.halvors.nuclearphysics.common.container.particle.ContainerParticleAccelerator;
import org.halvors.nuclearphysics.common.container.reactor.ContainerReactorCell;
import org.halvors.nuclearphysics.common.tile.machine.TileChemicalExtractor;
import org.halvors.nuclearphysics.common.tile.machine.TileGasCentrifuge;
import org.halvors.nuclearphysics.common.tile.machine.TileNuclearBoiler;
import org.halvors.nuclearphysics.common.tile.machine.TileQuantumAssembler;
import org.halvors.nuclearphysics.common.tile.particle.TileParticleAccelerator;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;

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

	@Override
	public Object getServerGuiElement(final int id, final EntityPlayer player, final World world, final int x, final int y, final int z) {
		final TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof TileParticleAccelerator) {
			return new ContainerParticleAccelerator(player.inventory, (TileParticleAccelerator) tile);
		} else if (tile instanceof TileChemicalExtractor) {
			return new ContainerChemicalExtractor(player.inventory, (TileChemicalExtractor) tile);
		} else if (tile instanceof TileGasCentrifuge) {
			return new ContainerGasCentrifuge(player.inventory, (TileGasCentrifuge) tile);
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
	public Object getClientGuiElement(final int id, final EntityPlayer player, final World world, final int x, final int y, final int z) {
		return null;
	}

	public EntityPlayer getPlayer(final MessageContext context) {
		return context.getServerHandler().playerEntity;
	}

	public void addScheduledTask(final Runnable runnable, final IBlockAccess world) {
		//((WorldServer) world).addScheduledTask(runnable);
	}

	public boolean isClient() {
		return false;
	}

	public boolean isServer() {
		return FMLCommonHandler.instance().getSide().isServer();
	}

	public boolean isPaused() {
		return false;
	}
}