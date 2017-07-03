package org.halvors.quantum.common;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import org.halvors.quantum.common.fluid.FluidQuantum;

public class QuantumFluids {
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

        // Add support for universal bucket.
        FluidRegistry.enableUniversalBucket();

        FluidRegistry.addBucketForFluid(fluidToxicWaste);

        // Register fluid containers.
        /*
        FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("deuterium"), 200), new ItemStack(itemDeuteriumCell), new ItemStack(itemCell));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("tritium"), 200), new ItemStack(itemTritiumCell), new ItemStack(itemCell));
        FluidContainerRegistry.registerFluidContainer(fluidToxicWaste, new ItemStack(itemBucketToxicWaste), new ItemStack(Items.BUCKET));
        FluidContainerRegistry.registerFluidContainer(FluidRegistry.WATER, new ItemStack(itemWaterCell), new ItemStack(itemCell));
        */
    }
}
