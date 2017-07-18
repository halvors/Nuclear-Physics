package org.halvors.quantum.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import org.halvors.quantum.common.block.BlockFluidToxicWaste;
import org.halvors.quantum.common.fluid.FluidQuantum;

import java.sql.Ref;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class QuantumFluids {
    public static final Fluid fluidDeuterium = new FluidQuantum("deuterium").setGaseous(true);
    public static final Fluid fluidUraniumHexaflouride = new FluidQuantum("uranium_hexafluoride").setGaseous(true);
    public static final Fluid fluidPlasma = new FluidQuantum("plasma").setGaseous(true);
    public static final Fluid fluidSteam = new FluidQuantum("steam").setGaseous(true);
    public static final Fluid fluidTritium = new FluidQuantum("tritium").setGaseous(true);

    public static FluidStack fluidStackDeuterium;
    public static FluidStack fluidStackUraniumHexaflouride;
    public static FluidStack fluidStackSteam;
    public static FluidStack fluidStackTritium;
    public static FluidStack fluidStackToxicWaste;
    public static FluidStack fluidStackWater;

    /**
     * The fluids registered by this mod. Includes fluids that were already registered by another mod.
     */
    public static final Set<Fluid> FLUIDS = new HashSet<>();

    /**
     * The fluid blocks from this mod only. Doesn't include blocks for fluids that were already registered by another mod.
     */
    public static final Set<IFluidBlock> MOD_FLUID_BLOCKS = new HashSet<>();

    public static final Fluid fluidToxicWaste = createFluid("toxic_waste", false,
            fluid -> fluid.setLuminosity(10).setDensity(1600).setViscosity(100),
            fluid -> new BlockFluidToxicWaste(fluid, new MaterialLiquid(MapColor.ADOBE)));

    public static final Fluid NORMAL = createFluid("normal", true,
            fluid -> fluid.setLuminosity(10).setDensity(1600).setViscosity(100),
            fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));

    public static final Fluid NORMAL_GAS = createFluid("normal_gas", true,
            fluid -> fluid.setLuminosity(10).setDensity(-1600).setViscosity(100).setGaseous(true),
            fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));

    public static final Fluid FINITE = createFluid("finite", false,
            fluid -> fluid.setLuminosity(10).setDensity(800).setViscosity(1500),
            fluid -> new BlockFluidFinite(fluid, new MaterialLiquid(MapColor.BLACK)));

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
            MOD_FLUID_BLOCKS.add(blockFactory.apply(fluid));
        } else {
            fluid = FluidRegistry.getFluid(name);
        }

        FLUIDS.add(fluid);

        return fluid;
    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {
        /**
         * Register this mod's fluid {@link Block}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            final IForgeRegistry<Block> registry = event.getRegistry();

            for (final IFluidBlock fluidBlock : MOD_FLUID_BLOCKS) {
                final Block block = (Block) fluidBlock;
                block.setRegistryName(Reference.ID, "fluid." + fluidBlock.getFluid().getName());
                block.setUnlocalizedName(Reference.ID + ":" + fluidBlock.getFluid().getUnlocalizedName());
                block.setCreativeTab(Quantum.getCreativeTab());
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

            for (final IFluidBlock fluidBlock : MOD_FLUID_BLOCKS) {
                final Block block = (Block) fluidBlock;
                final ItemBlock itemBlock = new ItemBlock(block);
                itemBlock.setRegistryName(block.getRegistryName());
                registry.register(itemBlock);
            }
        }
    }

    public static void registerFluidContainers() {
        registerTank(FluidRegistry.WATER);
        registerTank(FluidRegistry.LAVA);

        for (final Fluid fluid : FLUIDS) {
            registerBucket(fluid);
            registerTank(fluid);
        }
    }

    private static void registerBucket(Fluid fluid) {
        FluidRegistry.addBucketForFluid(fluid);
    }

    private static void registerTank(Fluid fluid) {
		/*
		final FluidStack fluidStack = new FluidStack(fluid, TileEntityFluidTank.CAPACITY);

		final Item item = Item.getItemFromBlock(ModBlocks.FLUID_TANK);
		assert item instanceof ItemFluidTank;

		((ItemFluidTank) item).addFluid(fluidStack);
		*/
    }
}
    /*
    public static final Fluid fluidDeuterium = new FluidQuantum("deuterium").setGaseous(true);
    public static final Fluid fluidUraniumHexaflouride = new FluidQuantum("uranium_hexafluoride").setGaseous(true);
    public static final Fluid fluidPlasma = new FluidQuantum("plasma").setGaseous(true);
    public static final Fluid fluidSteam = new FluidQuantum("steam").setGaseous(true);
    public static final Fluid fluidTritium = new FluidQuantum("tritium").setGaseous(true);
    public static final Fluid fluidToxicWaste = new FluidQuantum("toxic_waste");

    public static FluidStack fluidStackDeuterium;
    public static FluidStack fluidStackUraniumHexaflouride;
    public static FluidStack fluidStackSteam;
    public static FluidStack fluidStackTritium;
    public static FluidStack fluidStackToxicWaste;
    public static FluidStack fluidStackWater;

    public static void register() {
        // Register fluids.
        FluidRegistry.registerFluid(fluidDeuterium);
        FluidRegistry.registerFluid(fluidUraniumHexaflouride);
        FluidRegistry.registerFluid(fluidPlasma);
        FluidRegistry.registerFluid(fluidSteam);
        FluidRegistry.registerFluid(fluidTritium);
        FluidRegistry.registerFluid(fluidToxicWaste);

        fluidStackDeuterium = new FluidStack(fluidDeuterium, 0);
        fluidStackUraniumHexaflouride = new FluidStack(fluidUraniumHexaflouride, 0);
        fluidStackSteam = new FluidStack(fluidSteam, 0);
        fluidStackTritium = new FluidStack(fluidTritium, 0);
        fluidStackToxicWaste = new FluidStack(fluidToxicWaste, 0);
        fluidStackWater = new FluidStack(FluidRegistry.WATER, 0);


        FluidRegistry.addBucketForFluid(fluidToxicWaste);

        // Register fluid containers.
        FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("deuterium"), 200), new ItemStack(itemDeuteriumCell), new ItemStack(itemCell));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("tritium"), 200), new ItemStack(itemTritiumCell), new ItemStack(itemCell));
        FluidContainerRegistry.registerFluidContainer(fluidToxicWaste, new ItemStack(itemBucketToxicWaste), new ItemStack(Items.BUCKET));
        FluidContainerRegistry.registerFluidContainer(FluidRegistry.WATER, new ItemStack(itemWaterCell), new ItemStack(itemCell));
    }
}
*/