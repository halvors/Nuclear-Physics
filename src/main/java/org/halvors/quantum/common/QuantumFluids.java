package org.halvors.quantum.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import org.halvors.quantum.common.block.fluid.BlockFluidPlasma;
import org.halvors.quantum.common.block.fluid.BlockFluidToxicWaste;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class QuantumFluids {
    /**
     * The fluids registered by this mod. Includes fluids that were already registered by another mod.
     */
    public static final Set<Fluid> fluids = new HashSet<>();

    /**
     * The fluid blocks from this mod only. Doesn't include blocks for fluids that were already registered by another mod.
     */
    public static final Set<IFluidBlock> fluidBlocks = new HashSet<>();

    public static final Fluid gasDeuterium = createFluid("deuterium", false,
            fluid -> fluid.setGaseous(true),
            fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));

    public static final Fluid gasUraniumHexaflouride = createFluid("uranium_hexafluoride", false,
            fluid -> fluid.setGaseous(true),
            fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));

    public static final Fluid plasma = createFluid("plasma", false,
            fluid -> fluid.setGaseous(true),
            fluid -> new BlockFluidPlasma(fluid, new MaterialLiquid(MapColor.ADOBE)));

    public static final Fluid gasSteam = createFluid("steam", false,
            fluid -> fluid.setGaseous(true),
            fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));

    public static final Fluid gasTritium = createFluid("tritium", false,
            fluid -> fluid.setGaseous(true),
            fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));

    public static final Fluid fluidToxicWaste = createFluid("toxic_waste", false,
            fluid -> fluid.setDensity(100).setViscosity(100),
            fluid -> new BlockFluidToxicWaste(fluid, new MaterialLiquid(MapColor.ADOBE)));

    public static final FluidStack fluidStackDeuterium = new FluidStack(FluidRegistry.getFluid("deuterium"), 0);
    public static final FluidStack fluidStackUraniumHexaflouride = new FluidStack(FluidRegistry.getFluid("uranium_hexafluoride"), 0);
    public static final FluidStack stackPlasma = new FluidStack(FluidRegistry.getFluid("plasma"), 0);
    public static final FluidStack fluidStackSteam = new FluidStack(FluidRegistry.getFluid("steam"), 0);
    public static final FluidStack fluidStackTritium = new FluidStack(FluidRegistry.getFluid("tritium"), 0);
    public static final FluidStack fluidStackToxicWaste = new FluidStack(FluidRegistry.getFluid("toxic_waste"), 0);
    public static final FluidStack fluidStackWater = new FluidStack(FluidRegistry.WATER, 0);

    /**
     * Create a {@link Fluid} and its {@link IFluidBlock}, or use the existing ones if a fluid has already been registered with the same name.
     *
     * @param name                 The name of the fluid
     * @param hasFlowIcon          Does the fluid have a flow icon?
     * @param fluidPropertyApplier A function that sets the properties of the {@link Fluid}
     * @param blockFactory         A function that creates the {@link IFluidBlock}
     * @return The fluid and block
     */
    private static <T extends Block & IFluidBlock> Fluid createFluid(String name, boolean hasFlowIcon, Consumer<Fluid> fluidPropertyApplier, Function<Fluid, T> blockFactory) {
        final String texturePrefix = Reference.PREFIX + "fluids/";
        final ResourceLocation still = new ResourceLocation(texturePrefix + name + "_still");
        final ResourceLocation flowing = hasFlowIcon ? new ResourceLocation(texturePrefix + name + "_flow") : still;

        Fluid fluid = new Fluid(name, still, flowing);
        final boolean useOwnFluid = FluidRegistry.registerFluid(fluid);

        if (useOwnFluid) {
            fluidPropertyApplier.accept(fluid);
            fluidBlocks.add(blockFactory.apply(fluid));
        } else {
            fluid = FluidRegistry.getFluid(name);
        }

        fluids.add(fluid);

        return fluid;
    }

    @EventBusSubscriber
    public static class RegistrationHandler {
        /**
         * Register this mod's fluid {@link Block}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            final IForgeRegistry<Block> registry = event.getRegistry();

            for (final IFluidBlock fluidBlock : fluidBlocks) {
                final Block block = (Block) fluidBlock;
                final Fluid fluid = fluidBlock.getFluid();

                block.setUnlocalizedName(fluid.getUnlocalizedName());
                block.setRegistryName(fluid.getName());

                if (!fluid.isGaseous() || block instanceof BlockFluidPlasma) {
                    block.setCreativeTab(Quantum.getCreativeTab());
                }

                registry.register(block);
            }
        }

        /**
         * Register this mod's fluid {@link ItemBlock}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            final IForgeRegistry<Item> registry = event.getRegistry();

            for (final IFluidBlock fluidBlock : fluidBlocks) {
                final Block block = (Block) fluidBlock;
                final ItemBlock itemBlock = new ItemBlock(block);
                itemBlock.setRegistryName(block.getRegistryName());

                registry.register(itemBlock);
            }

            registerFluidContainers();
        }
    }

    public static void registerFluidContainers() {
        for (final Fluid fluid : fluids) {
            if (!fluid.isGaseous()) {
                FluidRegistry.addBucketForFluid(fluid);
            }
        }
    }
}