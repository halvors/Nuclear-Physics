package org.halvors.nuclearphysics.common.init;

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
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.block.fluid.BlockFluidPlasma;
import org.halvors.nuclearphysics.common.block.fluid.BlockFluidRadioactive;
import org.halvors.nuclearphysics.common.item.block.ItemBlockTooltip;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModFluids {
    /**
     * The FLUIDS registered by this mod. Includes fluids that were already registered by another mod.
     */
    public static final Set<Fluid> FLUIDS = new HashSet<>();

    /**
     * The fluid blocks from this mod only. Doesn't include blocks for fluids that were already registered by another mod.
     */
    public static final Set<IFluidBlock> FLUID_BLOCKS = new HashSet<>();

    public static final Fluid deuterium = createFluid("deuterium", false,
            fluid -> fluid.setDensity(1110), // Density: 1.11 g/cm3
            fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));

    public static final Fluid uraniumHexaflouride = createFluid("uranium_hexafluoride", false,
            fluid -> fluid.setDensity(5090).setGaseous(true), // Density: 5.09 g/cm3
            fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));

    public static final Fluid plasma = createFluid("plasma", false,
            fluid -> fluid.setLuminosity(7).setGaseous(true), // Density: ? g/cm3
            fluid -> new BlockFluidPlasma(fluid, new MaterialLiquid(MapColor.ADOBE)));

    public static final Fluid steam = createFluid("steam", false,
            fluid -> fluid.setDensity(1).setGaseous(true), // Density: 0.0006 g/cm3
            fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));

    public static final Fluid tritium = createFluid("tritium", false,
            fluid -> fluid.setDensity(1215).setLuminosity(15), // Density: 1.215 g/cm3
            fluid -> new BlockFluidRadioactive(fluid, new MaterialLiquid(MapColor.ADOBE)));

    public static final Fluid toxicWaste = createFluid("toxic_waste", false,
            fluid -> fluid.setDensity(8900), // Density: 8.9 g/cm3
            fluid -> new BlockFluidRadioactive(fluid, new MaterialLiquid(MapColor.ADOBE)));

    public static final FluidStack fluidStackDeuterium = new FluidStack(FluidRegistry.getFluid("deuterium"), 0);
    public static final FluidStack fluidStackUraniumHexaflouride = new FluidStack(FluidRegistry.getFluid("uranium_hexafluoride"), 0);
    public static final FluidStack fluidStackPlasma = new FluidStack(FluidRegistry.getFluid("plasma"), 0);
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
            FLUID_BLOCKS.add(blockFactory.apply(fluid));
        } else {
            fluid = FluidRegistry.getFluid(name);
        }

        FLUIDS.add(fluid);

        return fluid;
    }

    private static void registerFluidContainers() {
        for (final Fluid fluid : FLUIDS) {
            if (fluid == toxicWaste) {
                FluidRegistry.addBucketForFluid(fluid);
            }
        }
    }

    @EventBusSubscriber
    public static class RegistrationHandler {
        /**
         * Register this mod's fluid {@link Block}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            final IForgeRegistry<Block> registry = event.getRegistry();

            for (final IFluidBlock fluidBlock : FLUID_BLOCKS) {
                final Block block = (Block) fluidBlock;
                final Fluid fluid = fluidBlock.getFluid();

                block.setUnlocalizedName(fluid.getUnlocalizedName());
                block.setRegistryName(fluid.getName());

                if (fluid == plasma) {
                    block.setCreativeTab(NuclearPhysics.getCreativeTab());
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
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            final IForgeRegistry<Item> registry = event.getRegistry();

            for (final IFluidBlock fluidBlock : FLUID_BLOCKS) {
                registry.register(new ItemBlockTooltip((Block) fluidBlock));
            }

            registerFluidContainers();
        }
    }
}