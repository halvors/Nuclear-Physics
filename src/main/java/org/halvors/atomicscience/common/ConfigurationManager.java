package org.halvors.atomicscience.common;

import cpw.mods.fml.common.Loader;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.common.config.Configuration;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.network.NetworkHandler;
import org.halvors.electrometrics.common.util.energy.EnergyUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationManager {
    public static final String CATEGORY_MACHINE = "machine";
    public static final String CATEGORY_INTEGRATION = "integration";
    public static final String CATEGORY_CLIENT = "client";

    public static class General {
        public static boolean enableUpdateNotice;
        public static boolean destroyDisabledBlocks;

        public static double toJoules;
        public static double toElectricalUnits;
    }

    public static class Machine {
        private static final Map<MachineType, Boolean> machines = new HashMap<>();

        public static boolean isEnabled(MachineType machineType) {
            return machines.get(machineType) != null && machines.get(machineType);
        }

        public static void setEntry(MachineType machineType, boolean isEnabled) {
            machines.put(machineType, isEnabled);
        }
    }

    public static class Integration {
        public static boolean isVersionCheckerEnabled;
        public static boolean isBuildCraftEnabled;
        public static boolean isCoFHCoreEnabled;
        public static boolean isMekanismEnabled;
    }

    public static class Client {
        public static EnergyUnit energyUnit;
    }

    public static void loadConfiguration(Configuration configuration) {
        configuration.load();

        // General.
        General.enableUpdateNotice = configuration.get(Configuration.CATEGORY_GENERAL, "EnableUpdateNotice", true).getBoolean();
        General.destroyDisabledBlocks = configuration.get(Configuration.CATEGORY_GENERAL, "DestroyDisabledBlocks", true).getBoolean();

        General.toJoules = configuration.get(Configuration.CATEGORY_GENERAL, "RFToJoules", 2.5).getDouble();
        General.toElectricalUnits = configuration.get(Configuration.CATEGORY_GENERAL, "RFToElectricalUnits", 0.25).getDouble();

        // Machine.
        for (MachineType machineType : MachineType.values()) {
            Machine.setEntry(machineType, configuration.get(CATEGORY_MACHINE, machineType.getUnlocalizedName() + "Enabled", true).getBoolean());
        }

        // Integration.
        Integration.isVersionCheckerEnabled = configuration.get(CATEGORY_INTEGRATION, "VersionChecker", Loader.isModLoaded("VersionChecker")).getBoolean();
        Integration.isBuildCraftEnabled = configuration.get(CATEGORY_INTEGRATION, "BuildCraft", Loader.isModLoaded("BuildCraft|Core")).getBoolean();
        Integration.isCoFHCoreEnabled = configuration.get(CATEGORY_INTEGRATION, "CoFHCore", Loader.isModLoaded("CoFHCore")).getBoolean();
        Integration.isMekanismEnabled = configuration.get(CATEGORY_INTEGRATION, "Mekanism", Loader.isModLoaded("Mekanism")).getBoolean();

        // Client.
        Client.energyUnit = EnergyUnit.getUnitFromSymbol(configuration.get(CATEGORY_CLIENT, "EnergyUnitType", EnergyUnit.REDSTONE_FLUX.getSymbol(), "The default energy system to display. " + EnergyUnit.getSymbols(), EnergyUnit.getSymbols().toArray(new String[EnergyUnit.getSymbols().size()])).getString());

        configuration.save();
    }

    public static void saveConfiguration(Configuration configuration) {
        configuration.get(CATEGORY_CLIENT, "EnergyUnitType", EnergyUnit.REDSTONE_FLUX.getSymbol(), "The default energy system to display. " + EnergyUnit.getSymbols(), EnergyUnit.getSymbols().toArray(new String[EnergyUnit.getSymbols().size()])).set(Client.energyUnit.getSymbol());

        configuration.save();
    }

    public static void readConfiguration(ByteBuf dataStream) {
        // General.
        General.enableUpdateNotice = dataStream.readBoolean();
        General.destroyDisabledBlocks = dataStream.readBoolean();

        General.toJoules = dataStream.readDouble();
        General.toElectricalUnits = dataStream.readDouble();

        // Machine.
        for (MachineType machineType : MachineType.values()) {
            Machine.setEntry(machineType, dataStream.readBoolean());
        }

        // Integration.
        Integration.isVersionCheckerEnabled = dataStream.readBoolean();
        Integration.isBuildCraftEnabled = dataStream.readBoolean();
        Integration.isCoFHCoreEnabled = dataStream.readBoolean();
        Integration.isMekanismEnabled = dataStream.readBoolean();

        // Client.
        // We don't sync this as this is client specific changes that the server shouldn't care about.
    }

    public static void writeConfiguration(ByteBuf dataStream) {
        List<Object> objects = new ArrayList<>();

        // General.
        objects.add(General.enableUpdateNotice);
        objects.add(General.destroyDisabledBlocks);

        objects.add(General.toJoules);
        objects.add(General.toElectricalUnits);

        // Machine.
        for (MachineType machineType : MachineType.values()) {
            objects.add(Machine.isEnabled(machineType));
        }

        // Integration.
        objects.add(Integration.isVersionCheckerEnabled);
        objects.add(Integration.isBuildCraftEnabled);
        objects.add(Integration.isCoFHCoreEnabled);
        objects.add(Integration.isMekanismEnabled);

        // Client.
        // We don't sync this as this is client specific changes that the server shouldn't care about.

        NetworkHandler.writeObjects(objects, dataStream);
    }
}
