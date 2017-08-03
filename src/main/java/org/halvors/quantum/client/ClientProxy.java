package org.halvors.quantum.client;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.client.gui.debug.GuiCreativeBuilder;
import org.halvors.quantum.client.gui.machine.GuiChemicalExtractor;
import org.halvors.quantum.client.gui.machine.GuiGasCentrifuge;
import org.halvors.quantum.client.gui.machine.GuiNuclearBoiler;
import org.halvors.quantum.client.gui.machine.GuiQuantumAssembler;
import org.halvors.quantum.client.gui.particle.GuiAccelerator;
import org.halvors.quantum.client.gui.reactor.fission.GuiReactorCell;
import org.halvors.quantum.client.render.block.machine.RenderChemicalExtractor;
import org.halvors.quantum.client.render.block.machine.RenderGasCentrifuge;
import org.halvors.quantum.client.render.block.machine.RenderNuclearBoiler;
import org.halvors.quantum.client.render.block.machine.RenderQuantumAssembler;
import org.halvors.quantum.client.render.block.reactor.RenderElectricTurbine;
import org.halvors.quantum.client.render.block.reactor.fission.RenderReactorCell;
import org.halvors.quantum.client.render.block.reactor.fission.RenderThermometer;
import org.halvors.quantum.client.render.entity.RenderParticle;
import org.halvors.quantum.common.CommonProxy;
import org.halvors.quantum.common.QuantumBlocks;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.block.debug.BlockCreativeBuilder;
import org.halvors.quantum.common.block.states.BlockStateElectromagnet;
import org.halvors.quantum.common.entity.EntityParticle;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;
import org.halvors.quantum.common.tile.machine.TileGasCentrifuge;
import org.halvors.quantum.common.tile.machine.TileNuclearBoiler;
import org.halvors.quantum.common.tile.machine.TileQuantumAssembler;
import org.halvors.quantum.common.tile.particle.TileAccelerator;
import org.halvors.quantum.common.tile.reactor.TileElectricTurbine;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.common.tile.reactor.fission.TileThermometer;

import java.sql.Ref;

/**
 * This is the client proxy used only by the client.
 *
 * @author halvors
 */
@SideOnly(Side.CLIENT)
@EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IGuiHandler {
	@Override
	public void preInit() {
	    // Register our domain to OBJLoader.
		OBJLoader.INSTANCE.addDomain(Reference.DOMAIN);
		//OBJModelContainer.init();

		// Register entity renderer.
		RenderingRegistry.registerEntityRenderingHandler(EntityParticle.class, RenderParticle::new);

		// Register custom state mapper.
		ModelLoader.setCustomStateMapper(QuantumBlocks.blockElectromagnet, (new StateMap.Builder()).withName(BlockStateElectromagnet.TYPE).withSuffix("_electromagnet").build());
	}

	@Override
	public void init() {
        // Register special renderer.
		ClientRegistry.bindTileEntitySpecialRenderer(TileChemicalExtractor.class, new RenderChemicalExtractor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileElectricTurbine.class, new RenderElectricTurbine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileGasCentrifuge.class, new RenderGasCentrifuge());
        ClientRegistry.bindTileEntitySpecialRenderer(TileNuclearBoiler.class, new RenderNuclearBoiler());
        ClientRegistry.bindTileEntitySpecialRenderer(TileThermometer.class, new RenderThermometer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileQuantumAssembler.class, new RenderQuantumAssembler());
        ClientRegistry.bindTileEntitySpecialRenderer(TileReactorCell.class, new RenderReactorCell());
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tile = world.getTileEntity(pos);
		Block block = world.getBlockState(pos).getBlock();

		if (block instanceof BlockCreativeBuilder) {
			return new GuiCreativeBuilder(pos);
		}

		if (tile instanceof TileAccelerator) {
			return new GuiAccelerator(player.inventory, (TileAccelerator) tile);
		} else if (tile instanceof TileChemicalExtractor) {
			return new GuiChemicalExtractor(player.inventory, (TileChemicalExtractor) tile);
		} else if (tile instanceof TileGasCentrifuge) {
			return new GuiGasCentrifuge(player.inventory, (TileGasCentrifuge) tile);
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
	public void registerItemRenderer(Item item, int metadata, String id) {
		registerItemRenderer(item, metadata, id, "inventory");
	}

	@Override
	public void registerItemRenderer(Item item, int metadata, String id, String variant) {
		ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(Reference.PREFIX + id, variant));
	}

	@Override
	public EntityPlayer getPlayer(MessageContext context) {
		if (context.side.isServer()) {
			return context.getServerHandler().playerEntity;
		} else {
			return Minecraft.getMinecraft().player;
		}
	}

	@Override
	public void handlePacket(Runnable runnable, EntityPlayer player) {
		if (player == null || player.world.isRemote) {
			Minecraft.getMinecraft().addScheduledTask(runnable);
		} else {
			((WorldServer) player.world).addScheduledTask(runnable); // Singleplayer
		}
	}
}