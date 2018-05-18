package org.halvors.nuclearphysics.common.science.physics;

import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.nuclearphysics.api.BlockPos;

public class ThermalPhysics {
    public static final double ROOM_TEMPERATURE = 295;
    public static final double ICE_MELT_TEMPERATURE = 273.15;
    public static final double WATER_BOIL_TEMPERATURE = 373.2;

    /**
     * Temperature: 0.5f = 22C
     *
     * @param world - world in this context
     * @param pos - the block position
     *
     * @return The temperature of the coordinate in the world in kelvin.
     */
    public static double getTemperatureForCoordinate(final World world, final BlockPos pos) {
        final double worldTemperature = world.getBiomeGenForCoords(pos.getX(), pos.getZ()).getFloatTemperature(pos.getX(), pos.getY(), pos.getZ());
        final double averageTemperature = ICE_MELT_TEMPERATURE + ((worldTemperature - 0.4) * 50);
        final double dayNightVariance = averageTemperature * 0.05;

        return averageTemperature + (world.isDaytime() ? dayNightVariance : -dayNightVariance);
    }

    public static double getEnergyForTemperatureChange(final double mass, final double specificHeatCapacity, final double temperature) {
        return mass * specificHeatCapacity * temperature;
    }

    public static double getTemperatureForEnergy(final double mass, final long specificHeatCapacity, final long energy) {
        return energy / (mass * specificHeatCapacity);
    }

    public static double getRequiredBoilWaterEnergy(final World world, final BlockPos pos) {
        return getRequiredBoilWaterEnergy(world, pos, 1000);
    }

    public static double getRequiredBoilWaterEnergy(final World world, final BlockPos pos, final int volume) {
        final double temperatureChange = WATER_BOIL_TEMPERATURE - getTemperatureForCoordinate(world, pos);
        final double mass = getMass(volume, 1);

        return getEnergyForTemperatureChange(mass, 4200, temperatureChange) + getEnergyForStateChange(mass, 2257000);
    }

    public static double getEnergyForStateChange(final double mass, final double latentHeatCapacity) {
        return mass * latentHeatCapacity;
    }

    public static double getMass(final double volume, final double density) {
        return (volume / 1000 * density);
    }

    public static double getMass(final FluidStack fluidStack) {
        return getMass(fluidStack.amount, fluidStack.getFluid().getDensity());
    }
}
