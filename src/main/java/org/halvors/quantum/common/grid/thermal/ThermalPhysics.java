package org.halvors.quantum.common.grid.thermal;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import java.util.HashMap;
import java.util.Map;

public class ThermalPhysics {
    public static final ThermalPhysics instance = new ThermalPhysics();
    public static final int roomTemperature = 295;
    public static final int iceMeltTemperature = 273;
    public static final int waterBoilTemperature = 373;

    /** Temperature: 0.5f = 22C
     *
     * @return The temperature of the coordinate in the world in kelvin.
     */
    public static float getTemperatureForCoordinate(World world, int x, int z) {
        BlockPos pos = new BlockPos(x, 0, z);
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

    public static double getRequiredBoilWaterEnergy(World world, int x, int z) {
        return getRequiredBoilWaterEnergy(world, x, z, 1000);
    }

    public static double getRequiredBoilWaterEnergy(World world, int x, int z, int volume) {
        float temperatureChange = waterBoilTemperature - getTemperatureForCoordinate(world, x, z);
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
    public final HashMap<Vector3, Integer> thermalMap = new HashMap<>();

    public void update() {

        for (Map.Entry<Vector3, Integer> entry : thermalMap.entrySet()) {
            for (EnumFacing dir : EnumFacing.VALUES) {
                Vector3 checkPos = entry.getKey().clone().translate(dir);
                int neighbourTemp = getTemperature(checkPos);

                entry.setValue((entry.getValue() + neighbourTemp) / 2);
            }
        }
    }

    public int addEnergyToFluid(FluidStack fluidStack, int specificHeatCapacity, long energy) {
        int mass = (fluidStack.amount / 1000) * fluidStack.getFluid().getDensity(fluidStack);

        int changeInTemperature = (int) (energy / (mass * specificHeatCapacity));

        if (fluidStack.getFluid() instanceof FluidThermal) {
            ((FluidThermal) fluidStack.getFluid()).setTemperature(fluidStack, fluidStack.getFluid().getTemperature(fluidStack) + changeInTemperature);
        }

        return changeInTemperature;
    }

    public void setTemperature(Vector3 position, int temperature) {
        thermalMap.put(position, temperature);
    }

    public int getTemperature(Vector3 position) {
        if (thermalMap.containsKey(position)) {
            return thermalMap.get(position);
        }

        return roomTemperature;
    }
}
