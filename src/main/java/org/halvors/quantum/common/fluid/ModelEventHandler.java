package org.halvors.quantum.common.fluid;

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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.halvors.quantum.common.QuantumFluids;
import org.halvors.quantum.common.Reference;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ModelEventHandler {
	private static final ModelEventHandler INSTANCE = new ModelEventHandler();
	private static final String FLUID_MODEL_PATH = Reference.PREFIX + "fluid";

	private ModelEventHandler() {

	}

	/**
	 * Register this mod's {@link Fluid}, {@link Block} and {@link Item} models.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void registerAllModels(ModelRegistryEvent event) {
		INSTANCE.registerFluidModels();
	}

	/**
	 * Register this mod's {@link Fluid} models.
	 */
	private void registerFluidModels() {
		QuantumFluids.MOD_FLUID_BLOCKS.forEach(this::registerFluidModel);
	}

	/**
	 * Register the block and item model for a {@link Fluid}.
	 *
	 * @param fluidBlock The Fluid's Block
	 */
	private void registerFluidModel(IFluidBlock fluidBlock) {
		final Item item = Item.getItemFromBlock((Block) fluidBlock);
		final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(FLUID_MODEL_PATH, fluidBlock.getFluid().getName());

		ModelBakery.registerItemVariants(item);
		ModelLoader.setCustomMeshDefinition(item, MeshDefinitionFix.create(stack -> modelResourceLocation));
		ModelLoader.setCustomStateMapper((Block) fluidBlock, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_) {
				return modelResourceLocation;
			}
		});
	}
}
