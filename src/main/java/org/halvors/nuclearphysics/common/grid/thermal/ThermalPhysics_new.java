package org.halvors.nuclearphysics.common.grid.thermal;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.nuclearphysics.common.type.Pair;

import java.util.HashMap;
import java.util.Map.Entry;

public class ThermalPhysics_new {
    public static final int ROOM_TEMPERATURE = 295;

    /**
     * Temperature: 0.5f = 22C
     *
     * @return The temperature of the coordinate in the world in kelvin.
     */
    public static double getTemperatureForCoordinate(World world, BlockPos pos) {
        int averageTemperature = 273 + (int) ((world.getBiome(pos).getFloatTemperature(pos) - 0.4) * 50);
        double dayNightVariance = averageTemperature * 0.05;

        return averageTemperature + (world.isDaytime() ? dayNightVariance : -dayNightVariance);
    }

    /**
     * Q = mcT
     *
     * @param mass - KG
     * @param specificHeatCapacity - J/KG K
     * @param temperature - K
     *
     * @return Q, energy in joules
     * */
    public static double getEnergyForTemperatureChange(float mass, double specificHeatCapacity, float temperature) {
        return mass * specificHeatCapacity * temperature;
    }

    public static double getTemperatureForEnergy(float mass, long specificHeatCapacity, long energy) {
        return energy / (mass * specificHeatCapacity);
    }

    public static double getRequiredBoilWaterEnergy(World world, BlockPos pos) {
        return getRequiredBoilWaterEnergy(world, pos, 1000);
    }

    public static double getRequiredBoilWaterEnergy(World world, BlockPos pos, int volume) {
        double temperatureChange = 373 - ThermalPhysics.getTemperatureForCoordinate(world, pos);
        double mass = getMass(volume, 1);

        return ThermalPhysics.getEnergyForTemperatureChange(mass, 4200, temperatureChange) + ThermalPhysics.getEnergyForStateChange(mass, 2257000);
    }

    /**
     * Q = mL
     *
     * @param mass - KG
     * @param latentHeatCapacity - J/KG
     *
     * @return Q, energy in J
     */
    public static double getEnergyForStateChange(float mass, double latentHeatCapacity) {
        return mass * latentHeatCapacity;
    }

    /** Gets the mass of an object from volume and density.
     *
     * @param volume - in liters
     * @param density - in kg/m^3
     *
     * @return
     */
    public static double getMass(float volume, float density) {
        return (volume / 1000 * density);
    }

    /**
     * Mass (KG) = Volume (Cubic Meters) * Densitry (kg/m-cubed)
     *
     * @param fluidStack
     *
     * @return The mass in KG
     */
    public static double getMass(FluidStack fluidStack) {
        return getMass(fluidStack.amount, fluidStack.getFluid().getDensity());
    }

    /**
     * A map of the temperature of the blocks
     */
    public final HashMap<Pair<IBlockAccess, BlockPos>, Integer> thermalMap = new HashMap<>();

    public void update() {
        /** Reach thermal equilibrium */

        for (Entry<Pair<IBlockAccess, BlockPos>, Integer> entry : thermalMap.entrySet()) {
            for (EnumFacing dir : EnumFacing.values()) {
                BlockPos checkPos = entry.getKey().getRight().offset(dir);
                int neighbourTemp = getTemperature(entry.getKey().getLeft(), checkPos);

                entry.setValue((entry.getValue() + neighbourTemp) / 2);
            }
        }
    }

    /** Adds energy to the fluid and thereby, increasing its temperature.
     *
     * @param fluidStack - The fluid stack we are changing.
     * @param specificHeatCapacity - E.g: Water: 4200. Iron: 450.
     * @param energy - Amount of energy to put into the fluid.
     * @return Change in temperature in Kelvin */
    public int addEnergyToFluid(FluidStack fluidStack, int specificHeatCapacity, long energy) {
        // Mass (KG) = Volume (Cubic Meters) * Densitry (kg/m-cubed)
        int mass = (fluidStack.amount / 1000) * fluidStack.getFluid().getDensity(fluidStack);

        // c = Q/(mT); Therefore: Temperature (in Kelvin) = Q/mc
        int changeInTemperature = (int) (energy / (mass * specificHeatCapacity));

        /*
        if (fluidStack.getFluid() instanceof FluidThermal) {
            ((FluidThermal) fluidStack.getFluid()).setTemperature(fluidStack, fluidStack.getFluid().getTemperature(fluidStack) + changeInTemperature);
        }
        */

        return changeInTemperature;
    }

    /** Adds energy to a block in the form of heat. */
    public void addEnergy(IBlockAccess world, BlockPos pos, ChemElement element, long energy) {
        // Mass (KG) = Volume (Cubic Meters) * Densitry (kg/m-cubed)
        int mass = (int) (1 * element.density);

        // c = Q/(mT); Therefore: Temperature (in Kelvin) = Q/mc
        int changeInTemperature = (int) (energy / (mass * element.heatData.specificHeat));

        setTemperature(world, pos, getTemperature(pos) + changeInTemperature);
    }

    public void setTemperature(IBlockAccess world, BlockPos pos, int temperature) {
        thermalMap.put(new Pair<>(world, pos), temperature);
    }

    public int getTemperature(IBlockAccess world, BlockPos pos) {
        final Pair<IBlockAccess, BlockPos> key = new Pair<>(world, pos);

        if (thermalMap.containsKey(key)) {
            return thermalMap.get(key);
        }

        return ROOM_TEMPERATURE;
    }
}
