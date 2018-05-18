package org.halvors.nuclearphysics.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.client.event.TextureEventHandler;
import org.halvors.nuclearphysics.client.gui.debug.GuiCreativeBuilder;
import org.halvors.nuclearphysics.client.gui.machine.GuiChemicalExtractor;
import org.halvors.nuclearphysics.client.gui.machine.GuiGasCentrifuge;
import org.halvors.nuclearphysics.client.gui.machine.GuiNuclearBoiler;
import org.halvors.nuclearphysics.client.gui.machine.GuiQuantumAssembler;
import org.halvors.nuclearphysics.client.gui.particle.GuiParticleAccelerator;
import org.halvors.nuclearphysics.client.gui.reactor.GuiReactorCell;
import org.halvors.nuclearphysics.client.render.block.BlockRenderingHandler;
import org.halvors.nuclearphysics.client.render.block.machine.RenderChemicalExtractor;
import org.halvors.nuclearphysics.client.render.block.machine.RenderGasCentrifuge;
import org.halvors.nuclearphysics.client.render.block.machine.RenderNuclearBoiler;
import org.halvors.nuclearphysics.client.render.block.machine.RenderQuantumAssembler;
import org.halvors.nuclearphysics.client.render.block.reactor.RenderElectricTurbine;
import org.halvors.nuclearphysics.client.render.block.reactor.RenderReactorCell;
import org.halvors.nuclearphysics.client.render.block.reactor.RenderThermometer;
import org.halvors.nuclearphysics.client.render.block.reactor.fusion.RenderPlasmaHeater;
import org.halvors.nuclearphysics.client.render.entity.RenderParticle;
import org.halvors.nuclearphysics.client.render.item.ItemRenderingHandler;
import org.halvors.nuclearphysics.common.CommonProxy;
import org.halvors.nuclearphysics.common.block.debug.BlockCreativeBuilder;
import org.halvors.nuclearphysics.common.entity.EntityParticle;
import org.halvors.nuclearphysics.common.init.ModItems;
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
		// Register entity renderer.
		RenderingRegistry.registerEntityRenderingHandler(EntityParticle.class, new RenderParticle());
	}

	@Override
	public void init() {
		// Register texture event handler.
		MinecraftForge.EVENT_BUS.register(new TextureEventHandler());

        // Register special renderer.
		ClientRegistry.bindTileEntitySpecialRenderer(TileChemicalExtractor.class, new RenderChemicalExtractor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileElectricTurbine.class, new RenderElectricTurbine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileGasCentrifuge.class, new RenderGasCentrifuge());
        ClientRegistry.bindTileEntitySpecialRenderer(TileNuclearBoiler.class, new RenderNuclearBoiler());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePlasmaHeater.class, new RenderPlasmaHeater());
		ClientRegistry.bindTileEntitySpecialRenderer(TileQuantumAssembler.class, new RenderQuantumAssembler());
		ClientRegistry.bindTileEntitySpecialRenderer(TileReactorCell.class, new RenderReactorCell());
        ClientRegistry.bindTileEntitySpecialRenderer(TileThermometer.class, new RenderThermometer());

		// Register block rendering handler.
		RenderingRegistry.registerBlockHandler(BlockRenderingHandler.getInstance());


		// Register item rendering handler.
		ItemRenderingHandler itemRenderingHandler = new ItemRenderingHandler();
		MinecraftForgeClient.registerItemRenderer(ModItems.itemCell, itemRenderingHandler);
	}

	@Override
	public Object getClientGuiElement(final int id, final EntityPlayer player, final World world, final int x, final int y, final int z) {
		final BlockPos pos = new BlockPos(x, y, z);
		final TileEntity tile = pos.getTileEntity(world);
		final Block block = pos.getBlock(world);

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
			return context.getServerHandler().playerEntity;
		} else {
			return Minecraft.getMinecraft().thePlayer;
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