package org.halvors.nuclearphysics.client;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.debug.GuiCreativeBuilder;
import org.halvors.nuclearphysics.client.gui.machine.GuiChemicalExtractor;
import org.halvors.nuclearphysics.client.gui.machine.GuiGasCentrifuge;
import org.halvors.nuclearphysics.client.gui.machine.GuiNuclearBoiler;
import org.halvors.nuclearphysics.client.gui.machine.GuiQuantumAssembler;
import org.halvors.nuclearphysics.client.gui.particle.GuiParticleAccelerator;
import org.halvors.nuclearphysics.client.gui.reactor.GuiReactorCell;
import org.halvors.nuclearphysics.client.render.block.machine.RenderChemicalExtractor;
import org.halvors.nuclearphysics.client.render.block.machine.RenderGasCentrifuge;
import org.halvors.nuclearphysics.client.render.block.machine.RenderNuclearBoiler;
import org.halvors.nuclearphysics.client.render.block.machine.RenderQuantumAssembler;
import org.halvors.nuclearphysics.client.render.block.reactor.RenderElectricTurbine;
import org.halvors.nuclearphysics.client.render.block.reactor.RenderReactorCell;
import org.halvors.nuclearphysics.client.render.block.reactor.RenderThermometer;
import org.halvors.nuclearphysics.client.render.block.reactor.fusion.RenderPlasmaHeater;
import org.halvors.nuclearphysics.client.render.entity.RenderParticle;
import org.halvors.nuclearphysics.common.CommonProxy;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.block.debug.BlockCreativeBuilder;
import org.halvors.nuclearphysics.common.entity.EntityParticle;
import org.halvors.nuclearphysics.common.tile.machine.TileChemicalExtractor;
import org.halvors.nuclearphysics.common.tile.machine.TileGasCentrifuge;
import org.halvors.nuclearphysics.common.tile.machine.TileNuclearBoiler;
import org.halvors.nuclearphysics.common.tile.machine.TileQuantumAssembler;
import org.halvors.nuclearphysics.common.tile.particle.TileParticleAccelerator;
import org.halvors.nuclearphysics.common.tile.reactor.TileElectricTurbine;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;
import org.halvors.nuclearphysics.common.tile.reactor.TileThermometer;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TilePlasmaHeater;

/**
 * This is the client proxy used only by the client.
 *
 * @author halvors
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IGuiHandler {
	@Override
	public void preInit() {
	    // Register our domain to OBJLoader.
		OBJLoader.INSTANCE.addDomain(Reference.DOMAIN);

		// Register entity renderer.
		RenderingRegistry.registerEntityRenderingHandler(EntityParticle.class, RenderParticle::new);
	}

	@Override
	public void init() {
        // Register special renderer.
		ClientRegistry.bindTileEntitySpecialRenderer(TileChemicalExtractor.class, new RenderChemicalExtractor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileElectricTurbine.class, new RenderElectricTurbine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileGasCentrifuge.class, new RenderGasCentrifuge());
        ClientRegistry.bindTileEntitySpecialRenderer(TileNuclearBoiler.class, new RenderNuclearBoiler());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePlasmaHeater.class, new RenderPlasmaHeater());
		ClientRegistry.bindTileEntitySpecialRenderer(TileQuantumAssembler.class, new RenderQuantumAssembler());
		ClientRegistry.bindTileEntitySpecialRenderer(TileReactorCell.class, new RenderReactorCell());
        ClientRegistry.bindTileEntitySpecialRenderer(TileThermometer.class, new RenderThermometer());
	}

	@Override
	public void registerBlockRenderer(final Block block, final IProperty property, final String name) {
		ModelLoader.setCustomStateMapper(block, (new StateMap.Builder()).withName(property).withSuffix("_" + name).build());
	}

	@Override
	public void registerBlockRendererAndIgnore(final Block block, final IProperty property) {
		ModelLoader.setCustomStateMapper(block, (new StateMap.Builder()).ignore(property).build());
	}

	@Override
	public void registerItemRenderer(final Item item, final int metadata, final String id) {
		registerItemRenderer(item, metadata, id, "inventory");
	}

	@Override
	public void registerItemRenderer(final Item item, final int metadata, final String id, final String variant) {
		ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(Reference.PREFIX + id, variant));
	}

	@Override
	public Object getClientGuiElement(final int id, final EntityPlayer player, final World world, final int x, final int y, final int z) {
		final BlockPos pos = new BlockPos(x, y, z);
		final TileEntity tile = world.getTileEntity(pos);
		final IBlockState state = world.getBlockState(pos);
		final Block block = state.getBlock();

		if (block instanceof BlockCreativeBuilder) {
			return new GuiCreativeBuilder(block, pos);
		}

		if (tile instanceof TileChemicalExtractor) {
			return new GuiChemicalExtractor(player.inventory, (TileChemicalExtractor) tile);
		} else if (tile instanceof TileGasCentrifuge) {
			return new GuiGasCentrifuge(player.inventory, (TileGasCentrifuge) tile);
		} else if (tile instanceof TileNuclearBoiler) {
			return new GuiNuclearBoiler(player.inventory, (TileNuclearBoiler) tile);
		} else if (tile instanceof TileParticleAccelerator) {
			return new GuiParticleAccelerator(player.inventory, (TileParticleAccelerator) tile);
		} else if (tile instanceof TileQuantumAssembler) {
			return new GuiQuantumAssembler(player.inventory, (TileQuantumAssembler) tile);
		} else if (tile instanceof TileReactorCell) {
			return new GuiReactorCell(player.inventory, (TileReactorCell) tile);
		}

		return null;
	}

	@Override
	public EntityPlayer getPlayer(final MessageContext context) {
		if (context.side.isServer()) {
			return context.getServerHandler().player;
		} else {
			return Minecraft.getMinecraft().player;
		}
	}

	@Override
	public void addScheduledTask(final Runnable runnable, final IBlockAccess world) {
		if (world == null || isClient()) {
			Minecraft.getMinecraft().addScheduledTask(runnable);
		} else {
			super.addScheduledTask(runnable, world);
		}
	}

	@Override
	public boolean isClient() {
		return !isServer();
	}

	@Override
	public boolean isPaused() {
		final Minecraft minecraft = FMLClientHandler.instance().getClient();
		final IntegratedServer integratedServer = minecraft.getIntegratedServer();

		if (minecraft.isSingleplayer() && integratedServer != null && !integratedServer.getPublic()) {
			final GuiScreen screen = minecraft.currentScreen;

			return screen != null && screen.doesGuiPauseGame();
		}

		return false;
	}
}