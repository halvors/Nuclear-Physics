package org.halvors.quantum.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.client.gui.debug.GuiCreativeBuilder;
import org.halvors.quantum.client.gui.machine.GuiChemicalExtractor;
import org.halvors.quantum.client.gui.machine.GuiGasCentrifuge;
import org.halvors.quantum.client.gui.machine.GuiNuclearBoiler;
import org.halvors.quantum.client.gui.machine.GuiQuantumAssembler;
import org.halvors.quantum.client.gui.particle.GuiAccelerator;
import org.halvors.quantum.client.gui.reactor.fission.GuiReactorCell;
import org.halvors.quantum.common.CommonProxy;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.block.debug.BlockCreativeBuilder;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;
import org.halvors.quantum.common.tile.machine.TileGasCentrifuge;
import org.halvors.quantum.common.tile.machine.TileNuclearBoiler;
import org.halvors.quantum.common.tile.machine.TileQuantumAssembler;
import org.halvors.quantum.common.tile.particle.TileAccelerator;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

/**
 * This is the client proxy used only by the client.
 *
 * @author halvors
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IGuiHandler {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
	    super.preInit(event);

	    // Register our domain to OBJLoader.
		//OBJLoader.INSTANCE.addDomain(Reference.DOMAIN);

        // Item Variants
        /*
        ModelBakery.registerItemVariants(Quantum.itemAntimatterCell,
                new ResourceLocation(Reference.PREFIX + "antimatter_milligram"),
                new ResourceLocation(Reference.PREFIX + "antimatter_gram")
        );
        */

		// Items.
        /*
        ModelLoader.setCustomModelResourceLocation(Quantum.itemAntimatterCell, 0, new ModelResourceLocation(Reference.PREFIX + "cellAntimatter_milligram", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Quantum.itemAntimatterCell, 1, new ModelResourceLocation(Reference.PREFIX + "cellAntimatter_gram", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Quantum.itemBreederFuel, 0, new ModelResourceLocation(Reference.PREFIX + "breederFuel", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Quantum.itemCell, 0, new ModelResourceLocation(Reference.PREFIX + "cellEmpty", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Quantum.itemDarkMatterCell, 0, new ModelResourceLocation(Reference.PREFIX + "cellDarkMatter", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Quantum.itemDeuteriumCell, 0, new ModelResourceLocation(Reference.PREFIX + "cellDeuterium", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Quantum.itemFissileFuel, 0, new ModelResourceLocation(Reference.PREFIX + "fissileFuel", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Quantum.itemTritiumCell, 0, new ModelResourceLocation(Reference.PREFIX + "cellTritium", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Quantum.itemWaterCell, 0, new ModelResourceLocation(Reference.PREFIX + "cellWater", "inventory"));

        ModelLoader.setCustomModelResourceLocation(Quantum.itemBucketToxicWaste, 0, new ModelResourceLocation(Reference.PREFIX + "bucketToxicWaste", "inventory"));

        ModelLoader.setCustomModelResourceLocation(Quantum.itemUranium, 0, new ModelResourceLocation(Reference.PREFIX + "uranium", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Quantum.itemUranium, 1, new ModelResourceLocation(Reference.PREFIX + "uranium", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Quantum.itemYellowCake, 0, new ModelResourceLocation(Reference.PREFIX + "yellowcake", "inventory"));

        ModelLoader.setCustomModelResourceLocation(Quantum.itemHazmatMask, 0, new ModelResourceLocation(Reference.PREFIX + "hazmatMask", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Quantum.itemHazmatBody, 0, new ModelResourceLocation(Reference.PREFIX + "hazmatBody", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Quantum.itemHazmatLeggings, 0, new ModelResourceLocation(Reference.PREFIX + "hazmatLeggings", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Quantum.itemHazmatBoots, 0, new ModelResourceLocation(Reference.PREFIX + "hazmatBoots", "inventory"));

		// Blocks.
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockAccelerator), 0, new ModelResourceLocation(Reference.PREFIX + "accelerator", "inventory"));
		//ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockChemicalExtractor), 0, new ModelResourceLocation(Reference.PREFIX + "chemicalExtractor", "inventory"));
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockControlRod), 0, new ModelResourceLocation(Reference.PREFIX + "controlRod", "inventory"));
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockElectromagnet), 0, new ModelResourceLocation(Reference.PREFIX + "electromagnet", "inventory"));
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockFulmination), 0, new ModelResourceLocation(Reference.PREFIX + "fulmination", "inventory"));
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockGasCentrifuge), 0, new ModelResourceLocation(Reference.PREFIX + "gasCentrifuge", "inventory"));
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockGasFunnel), 0, new ModelResourceLocation(Reference.PREFIX + "gasFunnel", "inventory"));
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockNuclearBoiler), 0, new ModelResourceLocation(Reference.PREFIX + "nuclearBoiler", "inventory"));
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockSiren), 0, new ModelResourceLocation(Reference.PREFIX + "siren", "inventory"));
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockThermometer), 0, new ModelResourceLocation(Reference.PREFIX + "thermometer", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockUraniumOre), 0, new ModelResourceLocation(Reference.PREFIX + "uraniumOre", "inventory"));
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockPlasma), 0, new ModelResourceLocation(Reference.PREFIX + "plasma", "inventory"));
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockPlasmaHeater), 0, new ModelResourceLocation(Reference.PREFIX + "plasmaHeater", "inventory"));
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockQuantumAssembler), 0, new ModelResourceLocation(Reference.PREFIX + "quantumAssembler", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockRadioactiveGrass), 0, new ModelResourceLocation(Reference.PREFIX + "radioactiveGrass", "inventory"));
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockReactorCell), 0, new ModelResourceLocation(Reference.PREFIX + "reactorCell", "inventory"));
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockToxicWaste), 0, new ModelResourceLocation(Reference.PREFIX + "toxicWaste", "inventory"));
        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockElectricTurbine), 0, new ModelResourceLocation(Reference.PREFIX + "electricTurbine", "inventory"));

        //ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Quantum.blockCreativeBuilder), 0, new ModelResourceLocation(Reference.PREFIX + "creativeBuilder", "inventory"));
        */
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

        // Register block rendering handler.
        //RenderingRegistry.registerBlockHandler(new BlockRenderingHandler());

        // Register special renderer.
        /*
		ClientRegistry.bindTileEntitySpecialRenderer(TileChemicalExtractor.class, new RenderChemicalExtractor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileElectricTurbine.class, new RenderElectricTurbine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileGasCentrifuge.class, new RenderGasCentrifuge());
        ClientRegistry.bindTileEntitySpecialRenderer(TileNuclearBoiler.class, new RenderNuclearBoiler());
        ClientRegistry.bindTileEntitySpecialRenderer(TileThermometer.class, new RenderThermometer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileQuantumAssembler.class, new RenderQuantumAssembler());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePlasmaHeater.class, new RenderPlasmaHeater());
        ClientRegistry.bindTileEntitySpecialRenderer(TileReactorCell.class, new RenderReactorCell());
        */

        // Register entity renderer.
		//RenderingRegistry.registerEntityRenderingHandler(EntityParticle.class, new RenderParticle());
	}

    @Override
    public void registerItemRenderer(Item item, int metadata, String id) {
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(Reference.PREFIX + id, "inventory"));
    }

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		IBlockState state = world.getBlockState(new BlockPos(x, y, z));

		if (state instanceof BlockCreativeBuilder) {
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
			return Minecraft.getMinecraft().player;
		}
	}
}