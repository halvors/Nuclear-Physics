package org.halvors.nuclearphysics.common;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import org.halvors.nuclearphysics.common.network.PacketHandler;
import org.halvors.nuclearphysics.common.utility.energy.EnergyUnit;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationManager {
    public static final String CATEGORY_INTEGRATION = "integration";
    public static final String CATEGORY_CLIENT = "client";

    public static class General {
        public static boolean enableUpdateNotice;
        public static boolean destroyDisabledBlocks;
        public static double toJoules;
        public static double toElectricalUnits;

        public static int acceleratorEnergyCostPerTick = 4800000;
        public static int acceleratorAntimatterDensityMultiplier = 1;

        public static double fulminationOutputMultiplier = 1;
        public static double turbineOutputMultiplier = 1;
        public static double steamOutputMultiplier = 1;
        public static double fissionBoilVolumeMultiplier = 1;

        public static int uraniumHexaflourideRatio = 200;
        public static int waterPerDeutermium = 4;
        public static int deutermiumPerTritium = 4;
        public static double darkMatterSpawnChance = 0.2;

        public static boolean allowRadioactiveOres = true;
        public static boolean allowToxicWaste = true;
        public static boolean allowTurbineStacking = true;
        public static boolean allowOreDictionaryCompatibility = true;
        public static boolean allowAlternateRecipes = true;
        public static boolean allowIC2UraniumCompression = true;

        //public static int[] quantumAssemblerRecipes = new int[0]; // TODO: Implement this. // Comment: Put a list of block/item IDs to be used by the NuclearPhysics Assembler. Separate by commas, no space.
        public static int quantumAssemblerGenerateMode = 1; // Comment: 0 = Do not generate, 1 = Generate items only, 2 = Generate all

    }

    public static class Integration {
        public static boolean isMekanismEnabled;
    }

    public static class Client {
        public static EnergyUnit energyUnit;
    }

    public static void loadConfiguration(Configuration configuration) {
        configuration.load();

        // General.
        General.enableUpdateNotice = configuration.get(Configuration.CATEGORY_GENERAL, "enableUpdateNotice", true).getBoolean();
        General.destroyDisabledBlocks = configuration.get(Configuration.CATEGORY_GENERAL, "destroyDisabledBlocks", true).getBoolean();
        General.toJoules = configuration.get(Configuration.CATEGORY_GENERAL, "RFToJoules", 2.5).getDouble();
        General.toElectricalUnits = configuration.get(Configuration.CATEGORY_GENERAL, "RFToElectricalUnits", 0.25).getDouble();

        General.acceleratorEnergyCostPerTick = configuration.get(Configuration.CATEGORY_GENERAL, "acceleratorEnergyCostPerTick", 4800000).getInt();
        General.acceleratorAntimatterDensityMultiplier = configuration.get(Configuration.CATEGORY_GENERAL, "acceleratorAntimatterDensityMultiplier", 1).getInt();

        General.fulminationOutputMultiplier = configuration.get(Configuration.CATEGORY_GENERAL, "fulminationOutputMultiplier", 1).getDouble();
        General.turbineOutputMultiplier = configuration.get(Configuration.CATEGORY_GENERAL, "turbineOutputMultiplier", 1).getDouble();
        General.steamOutputMultiplier = configuration.get(Configuration.CATEGORY_GENERAL, "steamOutputMultiplier", 1).getDouble();
        General.fissionBoilVolumeMultiplier = configuration.get(Configuration.CATEGORY_GENERAL, "fissionBoilVolumeMultiplier", 1).getDouble();

        General.uraniumHexaflourideRatio = configuration.get(Configuration.CATEGORY_GENERAL, "uraniumHexaflourideRatio", 200).getInt();
        General.waterPerDeutermium = configuration.get(Configuration.CATEGORY_GENERAL, "waterPerDeutermium", 4).getInt();
        General.deutermiumPerTritium = configuration.get(Configuration.CATEGORY_GENERAL, "deutermiumPerTritium", 4).getInt();
        General.darkMatterSpawnChance = configuration.get(Configuration.CATEGORY_GENERAL, "darkMatterSpawnChance", 0.2).getDouble();

        General.allowRadioactiveOres = configuration.get(Configuration.CATEGORY_GENERAL, "allowRadioactiveOres", true).getBoolean();
        General.allowToxicWaste = configuration.get(Configuration.CATEGORY_GENERAL, "allowToxicWaste", true).getBoolean();
        General.allowTurbineStacking = configuration.get(Configuration.CATEGORY_GENERAL, "allowTurbineStacking", true).getBoolean();
        General.allowOreDictionaryCompatibility = configuration.get(Configuration.CATEGORY_GENERAL, "allowOreDictionaryCompatibility", true).getBoolean();
        General.allowAlternateRecipes = configuration.get(Configuration.CATEGORY_GENERAL, "allowAlternateRecipes", true).getBoolean();
        General.allowIC2UraniumCompression = configuration.get(Configuration.CATEGORY_GENERAL, "allowIC2UraniumCompression", true).getBoolean();

        //General.quantumAssemblerRecipes = new int[0]; // TODO: Implement this. // Comment: Put a list of block/item IDs to be used by the NuclearPhysics Assembler. Separate by commas, no space.
        General.quantumAssemblerGenerateMode = configuration.get(Configuration.CATEGORY_GENERAL, "quantumAssemblerGenerateMode", 1).getInt(); // Comment: 0 = Do not generate, 1 = Generate items only, 2 = Generate all

        // Integration.
        Integration.isMekanismEnabled = configuration.get(CATEGORY_INTEGRATION, "Mekanism", Loader.isModLoaded("Mekanism")).getBoolean();

        // Client.
        Client.energyUnit = EnergyUnit.getUnitFromSymbol(configuration.get(CATEGORY_CLIENT, "EnergyUnitType", EnergyUnit.REDSTONE_FLUX.getSymbol(), "The default energy system to display. " + EnergyUnit.getSymbols(), EnergyUnit.getSymbols().toArray(new String[EnergyUnit.getSymbols().size()])).getString());

        configuration.save();
    }

    public static void saveConfiguration(Configuration configuration) {
        configuration.save();
    }

    public static void readConfiguration(ByteBuf dataStream) {
        // General.
        General.enableUpdateNotice = dataStream.readBoolean();
        General.destroyDisabledBlocks = dataStream.readBoolean();
        General.toJoules = dataStream.readDouble();
        General.toElectricalUnits = dataStream.readDouble();

        General.acceleratorEnergyCostPerTick = dataStream.readInt();
        General.acceleratorAntimatterDensityMultiplier = dataStream.readInt();

        General.fulminationOutputMultiplier = dataStream.readDouble();
        General.turbineOutputMultiplier = dataStream.readDouble();
        General.steamOutputMultiplier = dataStream.readDouble();
        General.fissionBoilVolumeMultiplier = dataStream.readDouble();

        General.uraniumHexaflourideRatio = dataStream.readInt();
        General.waterPerDeutermium = dataStream.readInt();
        General.deutermiumPerTritium = dataStream.readInt();
        General.darkMatterSpawnChance = dataStream.readDouble();

        General.allowRadioactiveOres = dataStream.readBoolean();
        General.allowToxicWaste = dataStream.readBoolean();
        General.allowTurbineStacking = dataStream.readBoolean();
        General.allowOreDictionaryCompatibility = dataStream.readBoolean();
        General.allowAlternateRecipes = dataStream.readBoolean();
        General.allowIC2UraniumCompression = dataStream.readBoolean();

        //General.quantumAssemblerRecipes = new int[0]; // TODO: Implement this. // Comment: Put a list of block/item IDs to be used by the NuclearPhysics Assembler. Separate by commas, no space.
        General.quantumAssemblerGenerateMode = dataStream.readInt(); // Comment: 0 = Do not generate, 1 = Generate items only, 2 = Generate all

        // Integration.
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

        objects.add(General.acceleratorEnergyCostPerTick);
        objects.add(General.acceleratorAntimatterDensityMultiplier);

        objects.add(General.fulminationOutputMultiplier);
        objects.add(General.turbineOutputMultiplier);
        objects.add(General.steamOutputMultiplier);
        objects.add(General.fissionBoilVolumeMultiplier);

        objects.add(General.uraniumHexaflourideRatio);
        objects.add(General.waterPerDeutermium);
        objects.add(General.deutermiumPerTritium);
        objects.add( General.darkMatterSpawnChance);

        objects.add(General.allowRadioactiveOres);
        objects.add(General.allowToxicWaste);
        objects.add(General.allowTurbineStacking);
        objects.add(General.allowOreDictionaryCompatibility);
        objects.add(General.allowAlternateRecipes);
        objects.add(General.allowIC2UraniumCompression);

        //objects.add(General.quantumAssemblerRecipe);
        objects.add(General.quantumAssemblerGenerateMode);

        // Integration.
        objects.add(Integration.isMekanismEnabled);

        // Client.
        // We don't sync this as this is client specific changes that the server shouldn't care about.

        PacketHandler.writeObjects(objects, dataStream);
    }
}
