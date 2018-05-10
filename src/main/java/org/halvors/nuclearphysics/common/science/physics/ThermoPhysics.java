package org.halvors.nuclearphysics.common.science.physics;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ThermoPhysics {
    public static final double ROOM_TEMPERATURE = 295;
    public static final double ICE_MELT_TEMPERATURE = 273.15;
    public static final double WATER_BOIL_TEMPERATURE = 373.15;

    public static final double LATENT_HEAT_EVAPORATION_WATER = 2257 * Math.pow(10, 3);

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Q = cmT + mL
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
        int specificHeatCapacity = ThermalProperties.getSpecificHeatCapacity(Blocks.WATER);

        return getEnergyForTemperatureChange(mass, specificHeatCapacity, temperatureChange) + getEnergyForStateChange(mass, LATENT_HEAT_EVAPORATION_WATER);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static double getTemperatureForEnergy(double mass, double specificHeatCapacity, double energy) {
        return energy / (mass * specificHeatCapacity);
    }
}