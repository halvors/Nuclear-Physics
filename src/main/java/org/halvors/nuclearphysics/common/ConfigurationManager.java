package org.halvors.nuclearphysics.common;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.common.config.Configuration;
import org.halvors.nuclearphysics.common.network.PacketHandler;
import org.halvors.nuclearphysics.common.science.unit.EnumElectricUnit;
import org.halvors.nuclearphysics.common.science.unit.EnumTemperatureUnit;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationManager {
    public static class General {
        public static EnumElectricUnit electricUnit;
        public static EnumTemperatureUnit temperatureUnit;
        public static double toTesla;
        public static double toJoule;
        public static double fromTesla;
        public static double fromJoules;

        public static boolean enableAntimatterPower;
        public static boolean enableBoilingOfWaterBlocks;
        public static boolean enableOreRegeneration;
        public static boolean enableRadiationRoisoning;

        public static int uraniumPerChunk;

        public static int antimatterDensityMultiplier;
        public static double fulminationOutputMultiplier;
        public static double turbineOutputMultiplier;
        public static double steamOutputMultiplier;
        public static double fissionBoilVolumeMultiplier; // Not implemented?

        public static int uraniumHexaflourideRatio;
        public static int waterPerDeutermium;
        public static int deutermiumPerTritium;
        public static double darkMatterSpawnChance;

        public static boolean allowRadioactiveOres;
        public static boolean allowToxicWaste;
        //public static boolean allowTurbineStacking = true;
        //public static boolean allowAlternateRecipes = true;
        //public static boolean allowIC2UraniumCompression = true;
        public static boolean allowGeneratedQuantumAssemblerRecipes;
    }

    public static void loadConfiguration(final Configuration configuration) {
        configuration.load();

        General.electricUnit = EnumElectricUnit.fromSymbol(configuration.get(Configuration.CATEGORY_GENERAL, "electricUnit", EnumElectricUnit.JOULE.getSymbol(), null, EnumElectricUnit.getSymbols().toArray(new String[EnumElectricUnit.values().length])).getString());
        General.temperatureUnit = EnumTemperatureUnit.fromSymbol(configuration.get(Configuration.CATEGORY_GENERAL, "temperatureUnit", EnumTemperatureUnit.KELVIN.getSymbol(), null, EnumTemperatureUnit.getSymbols().toArray(new String[EnumTemperatureUnit.values().length])).getString());
        General.toTesla = configuration.get(Configuration.CATEGORY_GENERAL, "toTesla", 1).getDouble();
        General.toJoule = configuration.get(Configuration.CATEGORY_GENERAL, "toJoule", 0.4).getDouble();
        General.fromTesla = configuration.get(Configuration.CATEGORY_GENERAL, "fromTesla", 1).getDouble();
        General.fromJoules = configuration.get(Configuration.CATEGORY_GENERAL, "fromJoules", 2.5).getDouble();

        General.enableAntimatterPower = configuration.get(Configuration.CATEGORY_GENERAL, "enableAntimatterPower", true).getBoolean();
        General.enableBoilingOfWaterBlocks = configuration.get(Configuration.CATEGORY_GENERAL, "enableBoilingOfWaterBlocks", true).getBoolean();
        General.enableOreRegeneration = configuration.get(Configuration.CATEGORY_GENERAL, "enableOreRegeneration", true).getBoolean();
        General.enableRadiationRoisoning = configuration.get(Configuration.CATEGORY_GENERAL, "enableRadiationRoisoning", true).getBoolean();

        General.uraniumPerChunk = configuration.get(Configuration.CATEGORY_GENERAL, "uraniumPerChunk", 9).getInt();

        General.antimatterDensityMultiplier = configuration.get(Configuration.CATEGORY_GENERAL, "antimatterDensityMultiplier", 1).getInt();
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
        //General.allowTurbineStacking = configuration.get(Configuration.CATEGORY_GENERAL, "allowTurbineStacking", true).getBoolean();
        //General.allowAlternateRecipes = configuration.get(Configuration.CATEGORY_GENERAL, "allowAlternateRecipes", true).getBoolean();
        //General.allowIC2UraniumCompression = configuration.get(Configuration.CATEGORY_GENERAL, "allowIC2UraniumCompression", true).getBoolean();
        General.allowGeneratedQuantumAssemblerRecipes = configuration.get(Configuration.CATEGORY_GENERAL, "allowGeneratedQuantumAssemblerRecipes", true).getBoolean();

        configuration.save();
    }

    public static void saveConfiguration(final Configuration configuration) {
        configuration.save();
    }

    public static void readConfiguration(final ByteBuf dataStream) {
        General.electricUnit = EnumElectricUnit.values()[dataStream.readInt()];
        General.temperatureUnit = EnumTemperatureUnit.values()[dataStream.readInt()];
        General.toTesla = dataStream.readDouble();
        General.toJoule = dataStream.readDouble();
        General.fromTesla = dataStream.readDouble();
        General.fromJoules = dataStream.readDouble();

        General.enableAntimatterPower = dataStream.readBoolean();
        General.enableBoilingOfWaterBlocks = dataStream.readBoolean();
        General.enableOreRegeneration = dataStream.readBoolean();
        General.enableRadiationRoisoning = dataStream.readBoolean();

        General.uraniumPerChunk = dataStream.readInt();

        General.antimatterDensityMultiplier = dataStream.readInt();
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
        //General.allowTurbineStacking = dataStream.readBoolean();
        //General.allowAlternateRecipes = dataStream.readBoolean();
        //General.allowIC2UraniumCompression = dataStream.readBoolean();
        General.allowGeneratedQuantumAssemblerRecipes = dataStream.readBoolean();
    }

    public static void writeConfiguration(final ByteBuf dataStream) {
        final List<Object> objects = new ArrayList<>();

        objects.add(General.electricUnit.ordinal());
        objects.add(General.temperatureUnit.ordinal());
        objects.add(General.toTesla);
        objects.add(General.toJoule);
        objects.add(General.fromTesla);
        objects.add(General.fromJoules);

        objects.add(General.enableAntimatterPower);
        objects.add(General.enableBoilingOfWaterBlocks);
        objects.add(General.enableOreRegeneration);
        objects.add(General.enableRadiationRoisoning);

        objects.add(General.uraniumPerChunk);

        objects.add(General.antimatterDensityMultiplier);
        objects.add(General.fulminationOutputMultiplier);
        objects.add(General.turbineOutputMultiplier);
        objects.add(General.steamOutputMultiplier);
        objects.add(General.fissionBoilVolumeMultiplier);

        objects.add(General.uraniumHexaflourideRatio);
        objects.add(General.waterPerDeutermium);
        objects.add(General.deutermiumPerTritium);
        objects.add(General.darkMatterSpawnChance);

        objects.add(General.allowRadioactiveOres);
        objects.add(General.allowToxicWaste);
        //objects.add(General.allowTurbineStacking);
        //objects.add(General.allowAlternateRecipes);
        //objects.add(General.allowIC2UraniumCompression);
        objects.add(General.allowGeneratedQuantumAssemblerRecipes);

        PacketHandler.writeObjects(objects, dataStream);
    }
}
