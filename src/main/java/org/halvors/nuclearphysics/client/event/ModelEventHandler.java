package org.halvors.nuclearphysics.client.event;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.init.ModFluids;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(Side.CLIENT)
public class ModelEventHandler {
	private static final ModelEventHandler instance = new ModelEventHandler();

	/**
	 * Register this mod's {@link Fluid}, {@link Block} and {@link Item} models.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void registerAllModels(final ModelRegistryEvent event) {
		instance.registerFluidModels();
	}

	/**
	 * Register this mod's {@link Fluid} models.
	 */
	private void registerFluidModels() {
		for (final IFluidBlock fluid : ModFluids.fluidBlocks) {
			registerFluidModel(fluid);
		}
	}

	/**
	 * Register the block and item model for a {@link Fluid}.
	 *
	 * @param fluidBlock The Fluid's Block
	 */
	private void registerFluidModel(final IFluidBlock fluidBlock) {
		final Item item = Item.getItemFromBlock((Block) fluidBlock);
		final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Reference.PREFIX + "fluid", fluidBlock.getFluid().getName());

		if (item != null) {
			ModelBakery.registerItemVariants(item);
			ModelLoader.setCustomMeshDefinition(item, stack -> modelResourceLocation);
		}

		ModelLoader.setCustomStateMapper((Block) fluidBlock, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_) {
				return modelResourceLocation;
			}
		});
	}
}
