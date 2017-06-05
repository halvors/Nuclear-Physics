package org.halvors.quantum.lib.thermal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.lib.science.ChemicalElement;

public class ThermalPhysics {
    public static final ThermalPhysics INSTNACE = new ThermalPhysics();
    public static final int ROOM_TEMPERATURE = 295;

    public static float getTemperatureForCoordinate(World world, int x, int z) {
        // TODO: Check if this is correct.
        //int averageTemperature = 273 + (int) ((world.getBiomeGenForCoords(x, z).getFloatTemperature() - 0.4D) * 50.0D);
        int averageTemperature = 273 + (int) ((world.getBiomeGenForCoords(x, z).temperature - 0.4D) * 50.0D);
        double dayNightVariance = averageTemperature * 0.05D;
        return (float)(averageTemperature + (world.isDaytime() ? dayNightVariance : -dayNightVariance));
    }

    public static double getEnergyForTemperatureChange(float mass, double specificHeatCapacity, float temperature) {
        return mass * specificHeatCapacity * temperature;
    }

    public static float getTemperatureForEnergy(float mass, long specificHeatCapacity, long energy) {
        return (float)energy / (mass * (float)specificHeatCapacity);
    }

    public static double getRequiredBoilWaterEnergy(World world, int x, int z) {
        return getRequiredBoilWaterEnergy(world, x, z, 1000);
    }

    public static double getRequiredBoilWaterEnergy(World world, int x, int z, int volume) {
        float temperatureChange = 373.0F - getTemperatureForCoordinate(world, x, z);
        float mass = getMass(volume, 1.0F);
        return getEnergyForTemperatureChange(mass, 4200.0D, temperatureChange) + getEnergyForStateChange(mass, 2257000.0D);
    }

    public static double getEnergyForStateChange(float mass, double latentHeatCapacity) {
        return mass * latentHeatCapacity;
    }

    public static float getMass(float volume, float density) {
        return volume / 1000.0F * density;
    }

    public static int getMass(FluidStack fluidStack) {
        return fluidStack.amount / 1000 * fluidStack.getFluid().getDensity(fluidStack);
    }

    public final HashMap<Vector3, Integer> thermalMap = new HashMap<>();

    public void update() {
        Iterator<Map.Entry<Vector3, Integer>> it = this.thermalMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Vector3, Integer> entry = (Map.Entry)it.next();

            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                Vector3 checkPos = entry.getKey().clone().translate(dir);
                int neighbourTemp = getTemperature(checkPos);

                entry.setValue((entry.getValue() + neighbourTemp) / 2);
            }
        }
    }

    public int addEnergyToFluid(FluidStack fluidStack, int specificHeatCapacity, long energy) {
        int mass = fluidStack.amount / 1000 * fluidStack.getFluid().getDensity(fluidStack);

        int changeInTemperature = (int)(energy / (mass * specificHeatCapacity));

        if ((fluidStack.getFluid() instanceof FluidThermal)) {
            ((FluidThermal) fluidStack.getFluid()).setTemperature(fluidStack, fluidStack.getFluid().getTemperature(fluidStack) + changeInTemperature);
        }

        return changeInTemperature;
    }

    public void addEnergy(Vector3 position, ChemicalElement element, long energy) {
        int mass = (int) (1.0F * element.density);
        int changeInTemperature = (int) (energy / (mass * element.heatData.specificHeat));

        setTemperature(position, getTemperature(position) + changeInTemperature);
    }

    public void setTemperature(Vector3 position, int temperature) {
        thermalMap.put(position, temperature);
    }

    public int getTemperature(Vector3 position) {
        if (thermalMap.containsKey(position)) {
            return thermalMap.get(position);
        }

        return 295;
    }
}
