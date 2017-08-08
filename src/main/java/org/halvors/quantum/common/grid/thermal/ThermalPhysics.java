package org.halvors.quantum.common.grid.thermal;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.quantum.common.utility.type.Pair;

import java.util.HashMap;
import java.util.Map.Entry;

public class ThermalPhysics {
    public static final ThermalPhysics instance = new ThermalPhysics();
    public static final int roomTemperature = 295;
    public static final int iceMeltTemperature = 273;
    public static final int waterBoilTemperature = 373;

    /** Temperature: 0.5f = 22C
     *
     * @return The temperature of the coordinate in the world in kelvin.
     */
    public static float getTemperatureForCoordinate(World world, BlockPos pos) {
        int averageTemperature = 273 + (int) ((world.getBiome(pos).getFloatTemperature(pos) - 0.4) * 50);
        double dayNightVariance = averageTemperature * 0.05;

        return (float) (averageTemperature + (world.isDaytime() ? dayNightVariance : -dayNightVariance));
    }

    public static double getEnergyForTemperatureChange(float mass, double specificHeatCapacity, float temperature) {
        return mass * specificHeatCapacity * temperature;
    }

    public static float getTemperatureForEnergy(float mass, long specificHeatCapacity, long energy) {
        return energy / (mass * specificHeatCapacity);
    }

    public static double getRequiredBoilWaterEnergy(World world, BlockPos pos) {
        return getRequiredBoilWaterEnergy(world, pos, 1000);
    }

    public static double getRequiredBoilWaterEnergy(World world, BlockPos pos, int volume) {
        float temperatureChange = waterBoilTemperature - getTemperatureForCoordinate(world, pos);
        float mass = getMass(volume, 1);

        return getEnergyForTemperatureChange(mass, 4200, temperatureChange) + getEnergyForStateChange(mass, 2257000);
    }

    public static double getEnergyForStateChange(float mass, double latentHeatCapacity) {
        return mass * latentHeatCapacity;
    }

    public static float getMass(float volume, float density) {
        return (volume / 1000 * density);
    }

    public static int getMass(FluidStack fluidStack) {
        return (fluidStack.amount / 1000) * fluidStack.getFluid().getDensity(fluidStack);
    }

    // A map of the temperature of the blocks.
    public final HashMap<Pair<World, BlockPos>, Integer> thermalMap = new HashMap<>();

    public void update() {
        for (Entry<Pair<World, BlockPos>, Integer> entry : thermalMap.entrySet()) {
            for (EnumFacing side : EnumFacing.VALUES) {
                BlockPos checkPos = entry.getKey().getRight().offset(side);
                int neighbourTemp = getTemperature(entry.getKey().getLeft(), checkPos);

                entry.setValue((entry.getValue() + neighbourTemp) / 2);
            }
        }
    }

    public void setTemperature(World world, BlockPos pos, int temperature) {
        thermalMap.put(new Pair<>(world, pos), temperature);
    }

    public int getTemperature(World world, BlockPos pos) {
        if (thermalMap.containsKey(new Pair<>(world, pos))) {
            return thermalMap.get(new Pair<>(world, pos));
        }

        return roomTemperature;
    }
}
