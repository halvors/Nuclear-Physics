package org.halvors.quantum.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
import org.halvors.quantum.client.render.machine.RenderChemicalExtractor;
import org.halvors.quantum.client.render.machine.RenderGasCentrifuge;
import org.halvors.quantum.client.render.machine.RenderNuclearBoiler;
import org.halvors.quantum.client.render.machine.RenderQuantumAssembler;
import org.halvors.quantum.client.render.reactor.RenderElectricTurbine;
import org.halvors.quantum.client.render.reactor.fission.RenderReactorCell;
import org.halvors.quantum.client.render.reactor.fission.RenderThermometer;
import org.halvors.quantum.common.*;
import org.halvors.quantum.common.block.debug.BlockCreativeBuilder;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;
import org.halvors.quantum.common.tile.machine.TileGasCentrifuge;
import org.halvors.quantum.common.tile.machine.TileNuclearBoiler;
import org.halvors.quantum.common.tile.machine.TileQuantumAssembler;
import org.halvors.quantum.common.tile.particle.TileAccelerator;
import org.halvors.quantum.common.tile.reactor.TileElectricTurbine;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.common.tile.reactor.fission.TileThermometer;
import org.halvors.quantum.common.utility.ResourceUtility;
import org.halvors.quantum.common.utility.type.ResourceType;

/**
 * This is the client proxy used only by the client.
 *
 * @author halvors
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IGuiHandler {
	@Override
	public void preInit() {
	    // Register our domain to OBJLoader.
		OBJLoader.INSTANCE.addDomain(Reference.DOMAIN);
		//OBJBakedModel.init();

        // Item Variants
        /*
        ModelBakery.registerItemVariants(Quantum.itemAntimatterCell,
                new ResourceLocation(Reference.PREFIX + "antimatter_milligram"),
                new ResourceLocation(Reference.PREFIX + "antimatter_gram")
        );
        */

		// Items.
        ModelLoader.setCustomModelResourceLocation(QuantumItems.itemAntimatterCell, 0, new ModelResourceLocation(Reference.PREFIX + "cellAntimatter_milligram", "inventory"));
        ModelLoader.setCustomModelResourceLocation(QuantumItems.itemAntimatterCell, 1, new ModelResourceLocation(Reference.PREFIX + "cellAntimatter_gram", "inventory"));
        ModelLoader.setCustomModelResourceLocation(QuantumItems.itemBreederFuel, 0, new ModelResourceLocation(Reference.PREFIX + "breederFuel", "inventory"));
        ModelLoader.setCustomModelResourceLocation(QuantumItems.itemCell, 0, new ModelResourceLocation(Reference.PREFIX + "cellEmpty", "inventory"));
        ModelLoader.setCustomModelResourceLocation(QuantumItems.itemDarkMatterCell, 0, new ModelResourceLocation(Reference.PREFIX + "cellDarkMatter", "inventory"));
        ModelLoader.setCustomModelResourceLocation(QuantumItems.itemDeuteriumCell, 0, new ModelResourceLocation(Reference.PREFIX + "cellDeuterium", "inventory"));
        ModelLoader.setCustomModelResourceLocation(QuantumItems.itemFissileFuel, 0, new ModelResourceLocation(Reference.PREFIX + "fissileFuel", "inventory"));
        ModelLoader.setCustomModelResourceLocation(QuantumItems.itemTritiumCell, 0, new ModelResourceLocation(Reference.PREFIX + "cellTritium", "inventory"));
        ModelLoader.setCustomModelResourceLocation(QuantumItems.itemWaterCell, 0, new ModelResourceLocation(Reference.PREFIX + "cellWater", "inventory"));

        //ModelLoader.setCustomModelResourceLocation(QuantumItems.itemBucketToxicWaste, 0, new ModelResourceLocation(Reference.PREFIX + "bucketToxicWaste", "inventory"));

        ModelLoader.setCustomModelResourceLocation(QuantumItems.itemUranium, 0, new ModelResourceLocation(Reference.PREFIX + "uranium", "inventory"));
        ModelLoader.setCustomModelResourceLocation(QuantumItems.itemUranium, 1, new ModelResourceLocation(Reference.PREFIX + "uranium", "inventory"));
        ModelLoader.setCustomModelResourceLocation(QuantumItems.itemYellowCake, 0, new ModelResourceLocation(Reference.PREFIX + "yellowcake", "inventory"));

        ModelLoader.setCustomModelResourceLocation(QuantumItems.itemHazmatMask, 0, new ModelResourceLocation(Reference.PREFIX + "hazmatMask", "inventory"));
        ModelLoader.setCustomModelResourceLocation(QuantumItems.itemHazmatBody, 0, new ModelResourceLocation(Reference.PREFIX + "hazmatBody", "inventory"));
        ModelLoader.setCustomModelResourceLocation(QuantumItems.itemHazmatLeggings, 0, new ModelResourceLocation(Reference.PREFIX + "hazmatLeggings", "inventory"));
        ModelLoader.setCustomModelResourceLocation(QuantumItems.itemHazmatBoots, 0, new ModelResourceLocation(Reference.PREFIX + "hazmatBoots", "inventory"));
	}

	@Override
	public void init() {
        // Register block rendering handler.
        //RenderingRegistry.registerBlockHandler(new BlockRenderingHandler());

        // Register special renderer.
		ClientRegistry.bindTileEntitySpecialRenderer(TileChemicalExtractor.class, new RenderChemicalExtractor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileElectricTurbine.class, new RenderElectricTurbine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileGasCentrifuge.class, new RenderGasCentrifuge());
        ClientRegistry.bindTileEntitySpecialRenderer(TileNuclearBoiler.class, new RenderNuclearBoiler());
        ClientRegistry.bindTileEntitySpecialRenderer(TileThermometer.class, new RenderThermometer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileQuantumAssembler.class, new RenderQuantumAssembler());
        //ClientRegistry.bindTileEntitySpecialRenderer(TilePlasmaHeater.class, new RenderPlasmaHeater());
        ClientRegistry.bindTileEntitySpecialRenderer(TileReactorCell.class, new RenderReactorCell());

        // Register entity renderer.
		//RenderingRegistry.registerEntityRenderingHandler(EntityParticle.class, new RenderParticle());
	}

	@SubscribeEvent
	public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
		event.getMap().registerSprite(ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "chemical_extractor"));
		event.getMap().registerSprite(ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "electric_turbine_large"));
		event.getMap().registerSprite(ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "electric_turbine_small"));
		event.getMap().registerSprite(ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "gas_centrifuge"));
		event.getMap().registerSprite(ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "nuclear_boiler"));
		event.getMap().registerSprite(ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "quantum_assembler"));
		event.getMap().registerSprite(ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "reactor_cell_bottom"));
		event.getMap().registerSprite(ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "reactor_cell_middle"));
		event.getMap().registerSprite(ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "reactor_cell_top"));
		event.getMap().registerSprite(ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "reactor_cell_top"));
		event.getMap().registerSprite(ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "reactor_fissile_material"));
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
	public void registerItemRenderer(Item item, int metadata, String id) {
		ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(Reference.PREFIX + id, "inventory"));
	}

	@Override
	public EntityPlayer getPlayer(MessageContext context) {
		if (context.side.isServer()) {
			return context.getServerHandler().playerEntity;
		} else {
			return Minecraft.getMinecraft().player;
		}
	}
}