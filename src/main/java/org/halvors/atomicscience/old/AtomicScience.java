package org.halvors.atomicscience.old;

import atomicscience.base.ItemCell;
import atomicscience.fission.BlockUraniumOre;
import atomicscience.fission.ItemBreederFuel;
import atomicscience.fission.ItemFissileFuel;
import atomicscience.fission.ItemRadioactive;
import atomicscience.fission.ItemUranium;
import atomicscience.fission.reactor.BlockToxicWaste;
import atomicscience.fission.reactor.TileControlRod;
import atomicscience.fission.reactor.TileReactorCell;
import atomicscience.fusion.BlockPlasmaHeater;
import atomicscience.fusion.TileElectromagnet;
import atomicscience.fusion.TilePlasma;
import atomicscience.fusion.TilePlasmaHeater;
import atomicscience.particle.accelerator.BlockAccelerator;
import atomicscience.particle.accelerator.EntityParticle;
import atomicscience.particle.accelerator.ItemDarkMatter;
import atomicscience.particle.accelerator.TileAccelerator;
import atomicscience.particle.fulmination.FulminationHandler;
import atomicscience.particle.fulmination.ItemAntimatter;
import atomicscience.particle.fulmination.TileFulmination;
import atomicscience.particle.quantum.BlockQuantumAssembler;
import atomicscience.particle.quantum.TileQuantumAssembler;
import atomicscience.process.BlockChemicalExtractor;
import atomicscience.process.ItemHazmat;
import atomicscience.process.TileChemicalExtractor;
import atomicscience.process.fission.BlockCentrifuge;
import atomicscience.process.fission.BlockNuclearBoiler;
import atomicscience.process.fission.TileCentrifuge;
import atomicscience.process.fission.TileNuclearBoiler;
import atomicscience.process.sensor.TileSiren;
import atomicscience.process.sensor.TileThermometer;
import atomicscience.process.turbine.BlockElectricTurbine;
import atomicscience.process.turbine.TileElectricTurbine;
import atomicscience.process.turbine.TileFunnel;
import atomicscience.schematic.SchematicAccelerator;
import atomicscience.schematic.SchematicBreedingReactor;
import atomicscience.schematic.SchematicFissionReactor;
import atomicscience.schematic.SchematicFusionReactor;
import calclavia.api.atomicscience.IElectromagnet;
import calclavia.api.atomicscience.PlasmaEvent.SpawnPlasmaEvent;
import calclavia.api.atomicscience.QuantumAssemblerRecipes;
import calclavia.components.creative.BlockCreativeBuilder;
import calclavia.lib.config.ConfigHandler;
import calclavia.lib.content.ContentRegistry;
import calclavia.lib.flag.FlagRegistry;
import calclavia.lib.flag.ModFlag;
import calclavia.lib.network.PacketAnnotation;
import calclavia.lib.network.PacketHandler;
import calclavia.lib.network.PacketTile;
import calclavia.lib.prefab.block.BlockRadioactive;
import calclavia.lib.prefab.ore.OreGenBase;
import calclavia.lib.prefab.ore.OreGenReplaceStone;
import calclavia.lib.prefab.ore.OreGenerator;
import calclavia.lib.recipe.UniversalRecipe;
import calclavia.lib.render.RenderUtility;
import calclavia.lib.thermal.EventThermal.EventThermalUpdate;
import calclavia.lib.utility.LanguageUtility;
import calclavia.lib.utility.nbt.NBTUtility;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.Items;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent.Post;
import net.minecraftforge.client.event.TextureStitchEvent.Pre;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.EventBus;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import com.calclavia.core.lib.transform.vector.Vector3;
import com.calclavia.core.lib.transform.vector.VectorWorld;

@Mod(modid="AtomicScience", name="Atomic Science", version="1.2.0", dependencies="required-after:CalclaviaCore")
@NetworkMod(channels={"AtomicScience"}, clientSideRequired=true, serverSideRequired=false, packetHandler=PacketHandler.class)
public class AtomicScience {
    public static final String ID = "AtomicScience";
    public static final Logger LOGGER = Logger.getLogger("AtomicScience");
    public static final String CHANNEL = "AtomicScience";
    public static final PacketTile PACKET_TILE = new PacketTile("AtomicScience");
    public static final PacketAnnotation PACKET_ANNOTATION = new PacketAnnotation("AtomicScience");
    public static final String DOMAIN = "atomicscience";
    public static final String PREFIX = "atomicscience:";
    public static final String BASE_DIRECTORY_NO_SLASH = "atomicscience/";
    public static final String BASE_DIRECTORY = "/atomicscience/";
    public static final String ASSET_DIRECTORY = "/assets/atomicscience/";
    private static final String YU_YAN_WEN_JIAN = "/assets/atomicscience/languages/";
    public static final String TEXTURE_DIRECTORY = "textures/";
    public static final String GUI_TEXTURE_DIRECTORY = "textures/gui/";
    public static final String BLOCK_TEXTURE_DIRECTORY = "textures/blocks/";
    public static final String ITEM_TEXTURE_DIRECTORY = "textures/items/";
    public static final String MODEL_DIRECTORY = "models/";
    public static final String MODEL_PATH = "/assets/atomicscience/models/";
    public static final int ENTITY_ID_PREFIX = 49;
    public static final EnumArmorMaterial heYi = EnumHelper.addArmorMaterial("HAZMAT", 0, new int[] { 0, 0, 0, 0 }, 0);
    public static final String QIZI_FAN_WU_SU_BAO_ZHA = FlagRegistry.registerFlag("ban_antimatter_power");
    public static final String MAJOR_VERSION = "1";
    public static final String MINOR_VERSION = "2";
    public static final String REVISION_VERSION = "0";
    public static final String VERSION = "1.2.0";
    public static final String BUILD_VERSION = "83";
    public static final String NAME = "Atomic Science";
    public static final ContentRegistry contentRegistry = new ContentRegistry(Settings.CONFIGURATION, Settings.idManager, "AtomicScience").setPrefix("atomicscience:").setTab(TabAS.INSTANCE);
    private static final String[] YU_YAN = { "en_US", "pl_PL", "de_DE" };
    @Mod.Instance("AtomicScience")
    public static AtomicScience instance;
    @SidedProxy(clientSide="atomicscience.ClientProxy", serverSide="atomicscience.CommonProxy")
    public static CommonProxy proxy;
    @Mod.Metadata("AtomicScience")
    public static ModMetadata metadata;
    public static Block blockRadioactive;
    public static Block blockCentrifuge;
    public static Block blockElectricTurbine;
    public static Block blockNuclearBoiler;
    public static Block blockControlRod;
    public static Block blockThermometer;
    public static Block blockFusionCore;
    public static Block blockPlasma;
    public static Block blockElectromagnet;
    public static Block blockChemicalExtractor;
    public static Block blockSiren;
    public static Block blockSteamFunnel;
    public static Block blockAccelerator;
    public static Block blockFulmination;
    public static Block blockQuantumAssembler;
    public static Block blockReactorCell;
    public static Item itemCell;
    public static Item itemFissileFuel;
    public static Item itemBreedingRod;
    public static Item itemDarkMatter;
    public static Item itemAntimatter;
    public static Item itemDeuteriumCell;
    public static Item itemTritiumCell;
    public static Item itemWaterCell;
    public static Item itemBucketToxic;
    public static Block blockUraniumOre;
    public static Item itemYellowCake;
    public static Item itemUranium;
    public static Item itemHazmatTop;
    public static Item itemHazmatBody;
    public static Item itemHazmatLeggings;
    public static Item itemHazmatBoots;
    public static Block blockToxicWaste;
    public static FluidStack FLUIDSTACK_WATER;
    public static FluidStack FLUIDSTACK_URANIUM_HEXAFLOURIDE;
    public static FluidStack FLUIDSTACK_STEAM;
    public static FluidStack FLUIDSTACK_DEUTERIUM;
    public static FluidStack FLUIDSTACK_TRITIUM;
    public static FluidStack FLUIDSTACK_TOXIC_WASTE;
    public static Fluid FLUID_URANIUM_HEXAFLOURIDE;
    public static Fluid FLUID_PLASMA;
    public static Fluid FLUID_STEAM;
    public static Fluid FLUID_DEUTERIUM;
    public static Fluid FLUID_TRITIUM;
    public static Fluid FLUID_TOXIC_WASTE;
    public static OreGenBase uraniumOreGeneration;

    public static boolean isItemStackEmptyCell(ItemStack itemStack)
    {
        return isItemStackOreDictionaryCompatible(itemStack, new String[] { "cellEmpty" });
    }

    public static boolean isItemStackWaterCell(ItemStack itemStack)
    {
        return isItemStackOreDictionaryCompatible(itemStack, new String[] { "cellWater" });
    }

    public static boolean isItemStackUraniumOre(ItemStack itemStack)
    {
        return isItemStackOreDictionaryCompatible(itemStack, new String[] { "dropUranium", "oreUranium" });
    }

    public static boolean isItemStackDeuteriumCell(ItemStack itemStack)
    {
        return isItemStackOreDictionaryCompatible(itemStack, new String[] { "molecule_1d", "molecule_1h2", "cellDeuterium" });
    }

    public static boolean isItemStackTritiumCell(ItemStack itemStack)
    {
        return isItemStackOreDictionaryCompatible(itemStack, new String[] { "molecule_h3", "cellTritium" });
    }

    public static boolean isItemStackOreDictionaryCompatible(ItemStack itemStack, String... names)
    {
        if ((itemStack != null) && (names != null) && (names.length > 0))
        {
            String name = OreDictionary.getOreName(OreDictionary.getOreID(itemStack));
            for (String compareName : names) {
                if (name.equals(compareName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getFluidAmount(FluidStack fluid)
    {
        if (fluid != null) {
            return fluid.amount;
        }
        return 0;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        instance = this;
        Modstats.instance().getReporter().registerMod(this);
        MinecraftForge.EVENT_BUS.register(this);
        NetworkRegistry.instance().registerGuiHandler(this, proxy);

        PacketAnnotation.register(TileElectricTurbine.class);
        PacketAnnotation.register(TileReactorCell.class);
        PacketAnnotation.register(TileThermometer.class);

        LOGGER.fine("Loaded Languages: " + LanguageUtility.loadLanguages("/assets/atomicscience/languages/", YU_YAN));

        BlockCreativeBuilder.register(new SchematicAccelerator());
        BlockCreativeBuilder.register(new SchematicBreedingReactor());
        BlockCreativeBuilder.register(new SchematicFissionReactor());
        BlockCreativeBuilder.register(new SchematicFusionReactor());

        Settings.CONFIGURATION.load();
        try
        {
            ConfigHandler.configure(Settings.CONFIGURATION, "atomicscience");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Settings.load();

        PacketAnnotation.register(TileAccelerator.class);
        PacketAnnotation.register(TileChemicalExtractor.class);
        PacketAnnotation.register(TileNuclearBoiler.class);
        PacketAnnotation.register(TileElectricTurbine.class);

        FLUID_URANIUM_HEXAFLOURIDE = new Fluid("uraniumhexafluoride").setGaseous(true);
        FLUID_STEAM = new Fluid("steam").setGaseous(true);
        FLUID_DEUTERIUM = new Fluid("deuterium").setGaseous(true);
        FLUID_TRITIUM = new Fluid("tritium").setGaseous(true);
        FLUID_TOXIC_WASTE = new Fluid("toxicwaste");
        FLUID_PLASMA = new Fluid("plasma").setGaseous(true);

        FluidRegistry.registerFluid(FLUID_URANIUM_HEXAFLOURIDE);
        FluidRegistry.registerFluid(FLUID_STEAM);
        FluidRegistry.registerFluid(FLUID_TRITIUM);
        FluidRegistry.registerFluid(FLUID_DEUTERIUM);
        FluidRegistry.registerFluid(FLUID_TOXIC_WASTE);
        FluidRegistry.registerFluid(FLUID_PLASMA);

        FLUIDSTACK_WATER = new FluidStack(FluidRegistry.WATER, 0);
        FLUIDSTACK_URANIUM_HEXAFLOURIDE = new FluidStack(FLUID_URANIUM_HEXAFLOURIDE, 0);
        FLUIDSTACK_STEAM = new FluidStack(FluidRegistry.getFluidID("steam"), 0);
        FLUIDSTACK_DEUTERIUM = new FluidStack(FluidRegistry.getFluidID("deuterium"), 0);
        FLUIDSTACK_TRITIUM = new FluidStack(FluidRegistry.getFluidID("tritium"), 0);
        FLUIDSTACK_TOXIC_WASTE = new FluidStack(FluidRegistry.getFluidID("toxicwaste"), 0);

        blockRadioactive = contentRegistry.createBlock(BlockRadioactive.class).func_71864_b("atomicscience:radioactive").func_111022_d("atomicscience:radioactive").func_71849_a(TabAS.INSTANCE);
        blockUraniumOre = contentRegistry.createBlock(BlockUraniumOre.class);

        blockElectricTurbine = contentRegistry.createTile(BlockElectricTurbine.class, TileElectricTurbine.class);
        blockCentrifuge = contentRegistry.createTile(BlockCentrifuge.class, TileCentrifuge.class);
        blockReactorCell = contentRegistry.newBlock(TileReactorCell.class);
        blockNuclearBoiler = contentRegistry.createTile(BlockNuclearBoiler.class, TileNuclearBoiler.class);
        blockChemicalExtractor = contentRegistry.createTile(BlockChemicalExtractor.class, TileChemicalExtractor.class);
        blockFusionCore = contentRegistry.createTile(BlockPlasmaHeater.class, TilePlasmaHeater.class);
        blockControlRod = contentRegistry.newBlock(TileControlRod.class);
        blockThermometer = contentRegistry.newBlock(TileThermometer.class);
        blockPlasma = contentRegistry.newBlock(TilePlasma.class);
        blockElectromagnet = contentRegistry.newBlock(TileElectromagnet.class);
        blockSiren = contentRegistry.newBlock(TileSiren.class);
        blockSteamFunnel = contentRegistry.newBlock(TileFunnel.class);
        blockAccelerator = contentRegistry.createTile(BlockAccelerator.class, TileAccelerator.class);
        blockFulmination = contentRegistry.newBlock(TileFulmination.class);
        blockQuantumAssembler = contentRegistry.createTile(BlockQuantumAssembler.class, TileQuantumAssembler.class);
        blockToxicWaste = contentRegistry.createBlock(BlockToxicWaste.class).func_71849_a(null);

        itemHazmatTop = new ItemHazmat(Settings.CONFIGURATION.getItem("HazmatTop", Settings.getNextItemID()).getInt(), heYi, proxy.getArmorIndex("hazmat"), 0).func_77655_b("atomicscience:hazmatMask");
        itemHazmatBody = new ItemHazmat(Settings.CONFIGURATION.getItem("HazmatBody", Settings.getNextItemID()).getInt(), heYi, proxy.getArmorIndex("hazmat"), 1).func_77655_b("atomicscience:hazmatBody");
        itemHazmatLeggings = new ItemHazmat(Settings.CONFIGURATION.getItem("HazmatBottom", Settings.getNextItemID()).getInt(), heYi, proxy.getArmorIndex("hazmat"), 2).func_77655_b("atomicscience:hazmatLeggings");
        itemHazmatBoots = new ItemHazmat(Settings.CONFIGURATION.getItem("HazmatBoots", Settings.getNextItemID()).getInt(), heYi, proxy.getArmorIndex("hazmat"), 3).func_77655_b("atomicscience:hazmatBoots");

        itemCell = contentRegistry.createItem("cellEmpty", Item.class);
        itemFissileFuel = contentRegistry.createItem("rodFissileFuel", ItemFissileFuel.class);
        itemDeuteriumCell = contentRegistry.createItem("cellDeuterium", ItemCell.class);
        itemTritiumCell = contentRegistry.createItem("cellTritium", ItemCell.class);
        itemWaterCell = contentRegistry.createItem("cellWater", ItemCell.class);
        itemDarkMatter = contentRegistry.createItem("darkMatter", ItemDarkMatter.class);
        itemAntimatter = contentRegistry.createItem("antimatter", ItemAntimatter.class);
        itemBreedingRod = contentRegistry.createItem("rodBreederFuel", ItemBreederFuel.class);

        itemYellowCake = contentRegistry.createItem("yellowcake", ItemRadioactive.class);
        itemUranium = contentRegistry.createItem(ItemUranium.class);

        FLUID_PLASMA.setBlockID(blockPlasma);

        int bucketID = Settings.getNextItemID();
        itemBucketToxic = new ItemBucket(Settings.CONFIGURATION.getItem("Toxic Waste Bucket", bucketID).getInt(bucketID), blockToxicWaste.field_71990_ca).func_77637_a(TabAS.INSTANCE).func_77655_b("atomicscience:bucketToxicWaste").func_77642_a(Item.field_77788_aw).func_111206_d("atomicscience:bucketToxicWaste");

        FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluid("toxicwaste"), new ItemStack(itemBucketToxic), new ItemStack(Item.field_77788_aw));
        FluidContainerRegistry.registerFluidContainer(FluidRegistry.WATER, new ItemStack(itemWaterCell), new ItemStack(itemCell));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("deuterium"), 200), new ItemStack(itemDeuteriumCell), new ItemStack(itemCell));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("tritium"), 200), new ItemStack(itemTritiumCell), new ItemStack(itemCell));
        if ((OreDictionary.getOres("oreUranium").size() > 1) && (Settings.CONFIGURATION.get("general", "Auto Disable Uranium If Exist", false).getBoolean(false)))
        {
            LOGGER.fine("Disabled Uranium Generation. Detected another uranium being generated: " + OreDictionary.getOres("oreUranium").size());
        }
        else
        {
            uraniumOreGeneration = new OreGenReplaceStone("Uranium Ore", "oreUranium", new ItemStack(blockUraniumOre), 0, 25, 9, 3, "pickaxe", 2);
            uraniumOreGeneration.enable(Settings.CONFIGURATION);
            OreGenerator.addOre(uraniumOreGeneration);
            LOGGER.fine("Added Atomic Science uranium to ore generator.");
        }
        Settings.CONFIGURATION.save();

        MinecraftForge.EVENT_BUS.register(itemAntimatter);
        MinecraftForge.EVENT_BUS.register(FulminationHandler.INSTANCE);
        if (Settings.allowOreDictionaryCompatibility)
        {
            OreDictionary.registerOre("ingotUranium", itemUranium);
            OreDictionary.registerOre("dustUranium", itemYellowCake);
        }
        OreDictionary.registerOre("breederUranium", new ItemStack(itemUranium, 1, 1));
        OreDictionary.registerOre("blockRadioactive", blockRadioactive);

        OreDictionary.registerOre("cellEmpty", itemCell);
        OreDictionary.registerOre("cellUranium", itemFissileFuel);
        OreDictionary.registerOre("cellTritium", itemTritiumCell);
        OreDictionary.registerOre("cellDeuterium", itemDeuteriumCell);
        OreDictionary.registerOre("cellWater", itemWaterCell);
        OreDictionary.registerOre("strangeMatter", itemDarkMatter);
        OreDictionary.registerOre("antimatterMilligram", new ItemStack(itemAntimatter, 1, 0));
        OreDictionary.registerOre("antimatterGram", new ItemStack(itemAntimatter, 1, 1));

        ForgeChunkManager.setForcedChunkLoadingCallback(this, new ForgeChunkManager.LoadingCallback()
        {
            public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world)
            {
                for (ForgeChunkManager.Ticket ticket : tickets) {
                    if (ticket.getType() == ForgeChunkManager.Type.ENTITY) {
                        if (ticket.getEntity() != null) {
                            if ((ticket.getEntity() instanceof EntityParticle)) {
                                ((EntityParticle)ticket.getEntity()).youPiao = ticket;
                            }
                        }
                    }
                }
            }
        });
        proxy.preInit();
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent evt)
    {
        metadata.modId = "AtomicScience";
        metadata.name = "Atomic Science";
        metadata.description = "Electricity generation is always a burden. That's why we are here to bring in high tech nuclear power to solve all your electricity-lack problems. With our fission reactors, fusion reactors, and antimatter generators, you won't be lacking electricity ever again!";

        metadata.url = "http://www.calclavia.com/atomic-science";

        metadata.logoFile = "as_logo.png";
        metadata.version = "1.2.0.83";
        metadata.authorList = Arrays.asList(new String[] { "Calclavia" });
        metadata.credits = "Please visit the website.";
        metadata.autogenerated = false;
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        if ((Loader.isModLoaded("IC2")) && (Settings.allowAlternateRecipes))
        {
            OreDictionary.registerOre("cellEmpty", Items.getItem("cell"));

            String cellEmptyName = OreDictionary.getOreName(OreDictionary.getOreID("cellEmpty"));
            if (cellEmptyName == "Unknown") {
                LOGGER.info("Unable to register cellEmpty in OreDictionary!");
            }
            GameRegistry.addRecipe(new ShapelessOreRecipe(itemYellowCake, new Object[] { Items.getItem("reactorUraniumSimple") }));
            GameRegistry.addRecipe(new ShapelessOreRecipe(Items.getItem("cell"), new Object[] { itemCell }));
            GameRegistry.addRecipe(new ShapelessOreRecipe(itemCell, new Object[] { "cellEmpty" }));
        }
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(itemAntimatter, 1, 1), new Object[] { itemAntimatter, itemAntimatter, itemAntimatter, itemAntimatter, itemAntimatter, itemAntimatter, itemAntimatter, itemAntimatter }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(itemAntimatter, 8, 0), new Object[] { new ItemStack(itemAntimatter, 1, 1) }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockSteamFunnel, 2), new Object[] { " B ", "B B", "B B", Character.valueOf('B'), UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes) }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockSteamFunnel, 2), new Object[] { " B ", "B B", "B B", Character.valueOf('B'), "ingotIron" }));

        GameRegistry.addRecipe(new ShapedOreRecipe(blockQuantumAssembler, new Object[] { "CCC", "SXS", "SSS", Character.valueOf('X'), blockCentrifuge, Character.valueOf('C'), UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes), Character.valueOf('S'), UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes) }));

        GameRegistry.addRecipe(new ShapedOreRecipe(blockFulmination, new Object[] { "OSO", "SCS", "OSO", Character.valueOf('O'), Block.field_72089_ap, Character.valueOf('C'), UniversalRecipe.CIRCUIT_T2.get(Settings.allowAlternateRecipes), Character.valueOf('S'), UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes) }));

        GameRegistry.addRecipe(new ShapedOreRecipe(blockAccelerator, new Object[] { "SCS", "CMC", "SCS", Character.valueOf('M'), UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes), Character.valueOf('C'), UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes), Character.valueOf('S'), UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes) }));

        GameRegistry.addRecipe(new ShapedOreRecipe(blockCentrifuge, new Object[] { "BSB", "MCM", "BSB", Character.valueOf('C'), UniversalRecipe.CIRCUIT_T2.get(Settings.allowAlternateRecipes), Character.valueOf('S'), UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), Character.valueOf('B'), UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes), Character.valueOf('M'), UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes) }));

        GameRegistry.addRecipe(new ShapedOreRecipe(blockNuclearBoiler, new Object[] { "S S", "FBF", "SMS", Character.valueOf('F'), Block.field_72051_aB, Character.valueOf('S'), UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), Character.valueOf('B'), Item.field_77788_aw, Character.valueOf('M'), UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes) }));

        GameRegistry.addRecipe(new ShapedOreRecipe(blockChemicalExtractor, new Object[] { "BSB", "MCM", "BSB", Character.valueOf('C'), UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes), Character.valueOf('S'), UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), Character.valueOf('B'), UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes), Character.valueOf('M'), UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes) }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockSiren, 2), new Object[] { "NPN", Character.valueOf('N'), Block.field_71960_R, Character.valueOf('P'), UniversalRecipe.SECONDARY_PLATE.get(Settings.allowAlternateRecipes) }));

        GameRegistry.addRecipe(new ShapedOreRecipe(blockReactorCell, new Object[] { "SCS", "MEM", "SCS", Character.valueOf('E'), "cellEmpty", Character.valueOf('C'), UniversalRecipe.CIRCUIT_T2.get(Settings.allowAlternateRecipes), Character.valueOf('S'), UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), Character.valueOf('M'), UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes) }));

        GameRegistry.addRecipe(new ShapedOreRecipe(blockFusionCore, new Object[] { "CPC", "PFP", "CPC", Character.valueOf('P'), UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), Character.valueOf('F'), blockReactorCell, Character.valueOf('C'), UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes) }));

        GameRegistry.addRecipe(new ShapedOreRecipe(blockElectricTurbine, new Object[] { " B ", "BMB", " B ", Character.valueOf('B'), UniversalRecipe.SECONDARY_PLATE.get(Settings.allowAlternateRecipes), Character.valueOf('M'), UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes) }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemCell, 16), new Object[] { " T ", "TGT", " T ", Character.valueOf('T'), "ingotTin", Character.valueOf('G'), Block.field_71946_M }));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(itemWaterCell), new Object[] { "cellEmpty", Item.field_77786_ax }));

        GameRegistry.addRecipe(new ShapedOreRecipe(blockThermometer, new Object[] { "SSS", "GCG", "GSG", Character.valueOf('S'), UniversalRecipe.PRIMARY_METAL.get(Settings.allowAlternateRecipes), Character.valueOf('G'), Block.field_71946_M, Character.valueOf('C'), UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes) }));

        GameRegistry.addRecipe(new ShapedOreRecipe(blockControlRod, new Object[] { "I", "I", "I", Character.valueOf('I'), Item.field_77703_o }));

        GameRegistry.addRecipe(new ShapedOreRecipe(itemFissileFuel, new Object[] { "CUC", "CUC", "CUC", Character.valueOf('U'), "ingotUranium", Character.valueOf('C'), "cellEmpty" }));

        GameRegistry.addRecipe(new ShapedOreRecipe(itemBreedingRod, new Object[] { "CUC", "CUC", "CUC", Character.valueOf('U'), "breederUranium", Character.valueOf('C'), "cellEmpty" }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockElectromagnet, 2, 0), new Object[] { "BBB", "BMB", "BBB", Character.valueOf('B'), UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes), Character.valueOf('M'), UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes) }));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockElectromagnet, 1, 1), new Object[] { blockElectromagnet, Block.field_71946_M }));

        GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatTop, new Object[] { "SSS", "BAB", "SCS", Character.valueOf('A'), Item.field_77687_V, Character.valueOf('C'), UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), Character.valueOf('S'), Block.field_72101_ab }));
        GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatBody, new Object[] { "SSS", "BAB", "SCS", Character.valueOf('A'), Item.field_77686_W, Character.valueOf('C'), UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), Character.valueOf('S'), Block.field_72101_ab }));
        GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatLeggings, new Object[] { "SSS", "BAB", "SCS", Character.valueOf('A'), Item.field_77693_X, Character.valueOf('C'), UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), Character.valueOf('S'), Block.field_72101_ab }));
        GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatBoots, new Object[] { "SSS", "BAB", "SCS", Character.valueOf('A'), Item.field_77692_Y, Character.valueOf('C'), UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), Character.valueOf('S'), Block.field_72101_ab }));

        EntityRegistry.registerGlobalEntityID(EntityParticle.class, "ASParticle", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityParticle.class, "ASParticle", 49, this, 80, 3, true);

        proxy.init();

        Settings.CONFIGURATION.load();
        if ((Loader.isModLoaded("IC2")) && (Settings.allowAlternateRecipes)) {
            if (Settings.allowIC2UraniumCompression) {
                try
                {
                    if (Recipes.compressor != null)
                    {
                        Map<IRecipeInput, RecipeOutput> compressorRecipes = Recipes.compressor.getRecipes();
                        Iterator<Map.Entry<IRecipeInput, RecipeOutput>> it = compressorRecipes.entrySet().iterator();
                        int i = 0;
                        while (it.hasNext())
                        {
                            Map.Entry<IRecipeInput, RecipeOutput> entry = (Map.Entry)it.next();
                            for (ItemStack checkStack : ((IRecipeInput)entry.getKey()).getInputs()) {
                                if (isItemStackUraniumOre(checkStack))
                                {
                                    i++;
                                    it.remove();
                                }
                            }
                        }
                        LOGGER.fine("Removed " + i + " IC2 uranium compression recipe, use centrifuge instead.");
                    }
                }
                catch (Exception e)
                {
                    LOGGER.fine("Failed to remove IC2 compressor recipes.");
                    e.printStackTrace();
                }
            }
        }
        if (Settings.quantumAssemblerGenerateMode > 0)
        {
            for (Item item : Item.field_77698_e) {
                if (item != null) {
                    if ((item.field_77779_bT > 256) || (Settings.quantumAssemblerGenerateMode == 2))
                    {
                        ItemStack itemStack = new ItemStack(item);
                        QuantumAssemblerRecipes.addRecipe(itemStack);
                    }
                }
            }
            if (Settings.quantumAssemblerGenerateMode == 2) {
                for (Block block : Block.field_71973_m) {
                    if (block != null)
                    {
                        ItemStack itemStack = new ItemStack(block);
                        QuantumAssemblerRecipes.addRecipe(itemStack);
                    }
                }
            }
            for (String oreName : OreDictionary.getOreNames()) {
                if (oreName.startsWith("ingot")) {
                    for (ItemStack itemStack : OreDictionary.getOres(oreName)) {
                        QuantumAssemblerRecipes.addRecipe(itemStack);
                    }
                }
            }
        }
        Settings.CONFIGURATION.save();
    }

    @ForgeSubscribe
    public void thermalEventHandler(EventThermal.EventThermalUpdate evt)
    {
        VectorWorld pos = evt.position;
        Block block = Block.field_71973_m[pos.getBlockID()];
        if (block == blockElectromagnet) {
            evt.heatLoss = (evt.deltaTemperature * 0.6F);
        }
    }

    @ForgeSubscribe
    public void plasmaEvent(PlasmaEvent.SpawnPlasmaEvent evt)
    {
        World world = evt.world;
        Vector3 position = new Vector3(evt.x, evt.y, evt.z);
        int blockID = position.getBlockID(world);

        Block block = Block.field_71973_m[blockID];
        if (block != null)
        {
            if ((block.field_71990_ca == Block.field_71986_z.field_71990_ca) || (block.field_71990_ca == Block.field_72083_ai.field_71990_ca)) {
                return;
            }
            TileEntity tile = position.getTileEntity(world);
            if ((tile instanceof TilePlasma))
            {
                ((TilePlasma)tile).setTemperature(evt.temperature);
                return;
            }
            if ((tile instanceof IElectromagnet)) {
                return;
            }
        }
        position.setBlock(world, blockPlasma.field_71990_ca);

        TileEntity tile = position.getTileEntity(world);
        if ((tile instanceof TilePlasma)) {
            ((TilePlasma)tile).setTemperature(evt.temperature);
        }
    }

    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void preTextureHook(TextureStitchEvent.Pre event)
    {
        if (event.map.field_94255_a == 0)
        {
            RenderUtility.registerIcon("atomicscience:uraniumHexafluoride", event.map);
            RenderUtility.registerIcon("atomicscience:steam", event.map);
            RenderUtility.registerIcon("atomicscience:deuterium", event.map);
            RenderUtility.registerIcon("atomicscience:tritium", event.map);
            RenderUtility.registerIcon("atomicscience:atomic_edge", event.map);
            RenderUtility.registerIcon("atomicscience:funnel_edge", event.map);
            RenderUtility.registerIcon("atomicscience:glass", event.map);
        }
    }

    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void postTextureHook(TextureStitchEvent.Post event)
    {
        FLUID_URANIUM_HEXAFLOURIDE.setIcons((Icon)RenderUtility.loadedIconMap.get("atomicscience:uraniumHexafluoride"));
        FLUID_STEAM.setIcons((Icon)RenderUtility.loadedIconMap.get("atomicscience:steam"));
        FLUID_DEUTERIUM.setIcons((Icon)RenderUtility.loadedIconMap.get("atomicscience:deuterium"));
        FLUID_TRITIUM.setIcons((Icon)RenderUtility.loadedIconMap.get("atomicscience:tritium"));
        FLUID_TOXIC_WASTE.setIcons(blockToxicWaste.func_71858_a(0, 0));
        FLUID_PLASMA.setIcons(blockPlasma.func_71858_a(0, 0));
    }

    @ForgeSubscribe
    public void worldSave(WorldEvent.Save evt)
    {
        if (!evt.world.field_72995_K) {
            if (FlagRegistry.getModFlag("ModFlags") != null) {
                NBTUtility.saveData("ModFlags", FlagRegistry.getModFlag("ModFlags").getNBT());
            }
        }
    }

    @ForgeSubscribe
    public void fillBucketEvent(FillBucketEvent evt)
    {
        if ((!evt.world.field_72995_K) && (evt.target != null) && (evt.target.field_72313_a == EnumMovingObjectType.TILE))
        {
            Vector3 blockPos = new Vector3(evt.target);
            int blockID = blockPos.getBlockID(evt.world);
            if (blockID == blockToxicWaste.field_71990_ca)
            {
                blockPos.setBlock(evt.world, 0);
                evt.result = new ItemStack(itemBucketToxic);
                evt.setResult(Event.Result.ALLOW);
            }
        }
    }

    public static enum RecipeType
    {
        CHEMICAL_EXTRACTOR;

        private RecipeType() {}
    }
}
