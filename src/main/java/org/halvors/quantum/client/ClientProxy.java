package org.halvors.quantum.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.client.gui.debug.GuiCreativeBuilder;
import org.halvors.quantum.client.gui.machine.GuiChemicalExtractor;
import org.halvors.quantum.client.gui.machine.GuiGasCentrifuge;
import org.halvors.quantum.client.gui.machine.GuiNuclearBoiler;
import org.halvors.quantum.client.gui.machine.GuiQuantumAssembler;
import org.halvors.quantum.client.gui.particle.GuiAccelerator;
import org.halvors.quantum.client.gui.reactor.fission.GuiReactorCell;
import org.halvors.quantum.client.render.BlockRenderingHandler;
import org.halvors.quantum.client.render.machine.RenderCentrifuge;
import org.halvors.quantum.client.render.machine.RenderChemicalExtractor;
import org.halvors.quantum.client.render.machine.RenderNuclearBoiler;
import org.halvors.quantum.client.render.machine.RenderQuantumAssembler;
import org.halvors.quantum.client.render.particle.RenderParticle;
import org.halvors.quantum.client.render.reactor.RenderElectricTurbine;
import org.halvors.quantum.client.render.reactor.fission.RenderReactorCell;
import org.halvors.quantum.client.render.reactor.fission.RenderThermometer;
import org.halvors.quantum.client.render.reactor.fusion.RenderPlasmaHeater;
import org.halvors.quantum.common.CommonProxy;
import org.halvors.quantum.common.block.debug.BlockCreativeBuilder;
import org.halvors.quantum.common.entity.particle.EntityParticle;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;
import org.halvors.quantum.common.tile.machine.TileGasCentrifuge;
import org.halvors.quantum.common.tile.machine.TileNuclearBoiler;
import org.halvors.quantum.common.tile.machine.TileQuantumAssembler;
import org.halvors.quantum.common.tile.particle.TileAccelerator;
import org.halvors.quantum.common.tile.reactor.TileElectricTurbine;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.common.tile.reactor.fission.TileThermometer;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasmaHeater;
import org.halvors.quantum.common.transform.vector.Vector3;

/**
 * This is the client proxy used only by the client.
 *
 * @author halvors
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IGuiHandler {
	@Override
	public void init() {
		super.init();

        // Register block rendering handler.
        RenderingRegistry.registerBlockHandler(new BlockRenderingHandler());

        // Register special renderer.
        ClientRegistry.bindTileEntitySpecialRenderer(TileChemicalExtractor.class, new RenderChemicalExtractor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileElectricTurbine.class, new RenderElectricTurbine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileGasCentrifuge.class, new RenderCentrifuge());
        ClientRegistry.bindTileEntitySpecialRenderer(TileNuclearBoiler.class, new RenderNuclearBoiler());
        ClientRegistry.bindTileEntitySpecialRenderer(TileThermometer.class, new RenderThermometer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileQuantumAssembler.class, new RenderQuantumAssembler());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePlasmaHeater.class, new RenderPlasmaHeater());
        ClientRegistry.bindTileEntitySpecialRenderer(TileReactorCell.class, new RenderReactorCell());

        // Register entity renderer.
		RenderingRegistry.registerEntityRenderingHandler(EntityParticle.class, new RenderParticle());
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		Block block = world.getBlock(x, y, z);

		if (block instanceof BlockCreativeBuilder) {
			return new GuiCreativeBuilder(new Vector3(x, y, z));
		}

		if (tile instanceof TileAccelerator) {
			return new GuiAccelerator(player.inventory, (TileAccelerator) tile);
		} else if (tile instanceof TileGasCentrifuge) {
			return new GuiGasCentrifuge(player.inventory, (TileGasCentrifuge) tile);
		} else if (tile instanceof TileChemicalExtractor) {
			return new GuiChemicalExtractor(player.inventory, (TileChemicalExtractor) tile);
		} else if (tile instanceof TileNuclearBoiler) {
			return new GuiNuclearBoiler(player.inventory, (TileNuclearBoiler) tile);
		} else if (tile instanceof TileQuantumAssembler) {
			return new GuiQuantumAssembler(player.inventory, (TileQuantumAssembler) tile);
		} else if (tile instanceof TileReactorCell) {
			return new GuiReactorCell(player.inventory, (TileReactorCell) tile);
		}

		return null;
	}

	@Override
	public EntityPlayer getPlayer(MessageContext context) {
		if (context.side.isServer()) {
			return context.getServerHandler().playerEntity;
		} else {
			return Minecraft.getMinecraft().thePlayer;
		}
	}
}