package org.halvors.nuclearphysics.common.science.physics;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;

/**
 * A thermal block manager
 *
 * @author Calclavia
 */
public class ThermoPhysics {
    public static final double ROOM_TEMPERATURE = 295;
    public static final double ICE_MELT_TEMPERATURE = 273.15;
    public static final double WATER_BOIL_TEMPERATURE = 373.15;

    private static final Map<IBlockState, Integer> stateToSpecificHeatCapacityMap = new HashMap<>();
    private static final Map<Material, Integer> materialToSpecificHeatCapacityMap = new HashMap<>();

    static {
        /*
         * TODO: Data should be fetched from: https://www.engineeringtoolbox.com/specific-heat-capacity-d_391.html
         *                                    https://www.engineeringtoolbox.com/specific-heat-solids-d_154.html
         */

        register(Material.IRON, 449); // Updated.
        register(Material.AIR, 1005); // Updated.
        register(Material.GROUND, 8000);
        register(Material.WOOD, 2000); // Updated.
        register(Material.ROCK, 8400);
        register(Material.ANVIL, 5000);
        register(Material.WATER, 4182); // Updated.
        register(Material.LAVA, 9000);
        register(Material.LEAVES, 8400);
        register(Material.PLANTS, 8400);
        register(Material.VINE, 8400);
        register(Material.SPONGE, 8400);
        register(Material.CLOTH, 1400);
        register(Material.FIRE, 1000);
        register(Material.SAND, 800); // Updated.
        register(Material.CIRCUITS, 1000);
        register(Material.CARPET, 1130);
        register(Material.GLASS, 840); // Updated.
        register(Material.REDSTONE_LIGHT, 1000);
        register(Material.TNT, 1000);
        register(Material.CORAL, 8400);
        register(Material.ICE, 2090); // Updated.
        register(Material.SNOW, 2090);
        register(Material.CRAFTED_SNOW, 4200);
        register(Material.CACTUS, 8400);
        register(Material.CLAY, 920); // Updated.
        register(Material.GOURD, 8400);
        register(Material.DRAGON_EGG, 8400);
        register(Material.PORTAL, 1000);
        register(Material.CAKE, 2000);
        register(Material.WEB, 8400);
        register(Material.PISTON, 4500);
    }

    /**
     * Registers a block state with a specific heating value
     *
     * @param specificHeatCapacity - The specific heat capacity in J/Kg K
     */
    public static void register(final IBlockState state, int specificHeatCapacity) {
        if (state != null && specificHeatCapacity > 0) {
            stateToSpecificHeatCapacityMap.put(state, specificHeatCapacity);
        }
    }

    /**
     * Registers a material with a specific heating value
     *
     * @param specificHeatCapacity - The specific heat capacity in J/Kg K
     */
    public static void register(final Material material, int specificHeatCapacity) {
        if (material != null && specificHeatCapacity > 0) {
            materialToSpecificHeatCapacityMap.put(material, specificHeatCapacity);
        }
    }

    /**
     * Gets the specific heat capacity of a certain material
     */
    public static int getSpecificHeatCapacity(final IBlockState state) {
        if (stateToSpecificHeatCapacityMap.containsKey(state)) {
            stateToSpecificHeatCapacityMap.getOrDefault(state, 0);
        }

        return materialToSpecificHeatCapacityMap.getOrDefault(state.getMaterial(), 0);
    }

    public static int getSpecificHeatCapacity(final Block block) {
        return getSpecificHeatCapacity(block.getDefaultState());
    }

    public static int getSpecificHeatCapacity(final Fluid fluid) {
        return getSpecificHeatCapacity(fluid.getBlock());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static double getTemperatureForEnergy(double mass, double specificHeatCapacity, double energy) {
        return energy / (mass * specificHeatCapacity);
    }

    /**
     * Q = cmT
     *
     * Q = (4.2*10^3 J/kgC^-1) (1 kg) (80C)
     * Q = 336000 J
     *
     * @param world - world this happens in
     * @param x - coordinate of block
     * @param z - coordinate of block
     * @param volume - in liters
     *
     * @return The required energy to boil volume of water into steam in joules
     */
    public static double getRequiredBoilWaterEnergy(World world, int x, int z, int volume) {
        double temperatureChange = WATER_BOIL_TEMPERATURE - getDefaultTemperature(world, new BlockPos(x, 0, z));
        double mass = getMass(new FluidStack(FluidRegistry.WATER, volume));
        int specificHeatCapacity = getSpecificHeatCapacity(Blocks.WATER);

        return getEnergyForTemperatureChange(mass, specificHeatCapacity, temperatureChange) + ThermoPhysics.getEnergyForStateChange(mass, 2257000);
    }

    /**
     * Temperature: 0.5f = 22C
     *
     * @return The temperature of the coordinate in the world in kelvin.
     */
    public static double getDefaultTemperature(final World world, final BlockPos pos) {
        final double averageTemperature = ICE_MELT_TEMPERATURE + (world.getBiome(pos).getFloatTemperature(pos) - 0.4) * 50;
        final double dayNightVariance = averageTemperature * 0.05; // TODO: Check if this could be handled differently.

        return averageTemperature + (world.isDaytime() ? dayNightVariance : -dayNightVariance);
    }

    /**
     * Q = cmT
     *
     * @param mass                 - in kg
     * @param specificHeatCapacity - in J/kg*K
     * @param temperature          - in K
     *
     * @return Q, energy in joules (J)
     */
    public static double getEnergyForTemperatureChange(final double mass, final double specificHeatCapacity, final double temperature) {
        return mass * specificHeatCapacity * temperature;
    }

    /**
     * Q = mL
     *
     * @param mass               - in kg
     * @param latentHeatCapacity - in J/kg
     *
     * @return Q, energy in joules (J)
     */
    public static double getEnergyForStateChange(final double mass, final double latentHeatCapacity) {
        return mass * latentHeatCapacity;
    }

    /**
     * Gets the mass of an object from volume and density.
     *
     * Mass (kg) = Volume (Cubic Meters) * Densitry (kg/m-cubed)
     *
     * Formula: m = ρ * V
     *
     * @param density - in kg/m^3
     * @param volume  - in liters
     *
     * @return The mass in kg.
     */
    public static double getMass(final double volume, final double density) {
        return density * (volume / Math.pow(10, 3));
    }

    /**
     * Get the mass of a fluid stack.
     *
     * @param fluidStack - the fluid stack to get the mass of.
     *
     * @return The mass in kg.
     */
    public static double getMass(final FluidStack fluidStack) {
        return getMass(fluidStack.getFluid().getDensity(), fluidStack.amount);
    }
}