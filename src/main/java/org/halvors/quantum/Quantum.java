package org.halvors.quantum;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.halvors.quantum.common.*;
import org.halvors.quantum.common.ConfigurationManager.Integration;
import org.halvors.quantum.common.block.BlockQuantum;
import org.halvors.quantum.common.block.BlockRadioactiveGrass;
import org.halvors.quantum.common.block.BlockToxicWaste;
import org.halvors.quantum.common.block.BlockUraniumOre;
import org.halvors.quantum.common.block.debug.BlockCreativeBuilder;
import org.halvors.quantum.common.block.machine.BlockChemicalExtractor;
import org.halvors.quantum.common.block.machine.BlockGasCentrifuge;
import org.halvors.quantum.common.block.machine.BlockNuclearBoiler;
import org.halvors.quantum.common.block.machine.BlockQuantumAssembler;
import org.halvors.quantum.common.block.particle.BlockAccelerator;
import org.halvors.quantum.common.block.particle.BlockFulmination;
import org.halvors.quantum.common.block.reactor.BlockElectricTurbine;
import org.halvors.quantum.common.block.reactor.BlockGasFunnel;
import org.halvors.quantum.common.block.reactor.fission.BlockControlRod;
import org.halvors.quantum.common.block.reactor.fission.BlockReactorCell;
import org.halvors.quantum.common.block.reactor.fission.BlockSiren;
import org.halvors.quantum.common.block.reactor.fission.BlockThermometer;
import org.halvors.quantum.common.block.reactor.fusion.BlockElectromagnet;
import org.halvors.quantum.common.block.reactor.fusion.BlockPlasma;
import org.halvors.quantum.common.block.reactor.fusion.BlockPlasmaHeater;
import org.halvors.quantum.common.entity.particle.EntityParticle;
import org.halvors.quantum.common.event.ExplosionEventHandler;
import org.halvors.quantum.common.event.PlayerEventHandler;
import org.halvors.quantum.common.event.ThermalEventHandler;
import org.halvors.quantum.common.grid.UpdateTicker;
import org.halvors.quantum.common.item.ItemCell;
import org.halvors.quantum.common.item.ItemRadioactive;
import org.halvors.quantum.common.item.armor.ItemArmorHazmat;
import org.halvors.quantum.common.item.block.ItemBlockMetadata;
import org.halvors.quantum.common.item.block.ItemBlockThermometer;
import org.halvors.quantum.common.item.particle.ItemAntimatterCell;
import org.halvors.quantum.common.item.particle.ItemDarkmatterCell;
import org.halvors.quantum.common.item.reactor.fission.ItemBreederFuel;
import org.halvors.quantum.common.item.reactor.fission.ItemBucketToxicWaste;
import org.halvors.quantum.common.item.reactor.fission.ItemFissileFuel;
import org.halvors.quantum.common.item.reactor.fission.ItemUranium;
import org.halvors.quantum.common.network.PacketHandler;
import org.halvors.quantum.common.schematic.SchematicAccelerator;
import org.halvors.quantum.common.schematic.SchematicBreedingReactor;
import org.halvors.quantum.common.schematic.SchematicFissionReactor;
import org.halvors.quantum.common.schematic.SchematicFusionReactor;
import org.halvors.quantum.common.thermal.ThermalGrid;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;
import org.halvors.quantum.common.tile.machine.TileGasCentrifuge;
import org.halvors.quantum.common.tile.machine.TileNuclearBoiler;
import org.halvors.quantum.common.tile.machine.TileQuantumAssembler;
import org.halvors.quantum.common.tile.particle.FulminationHandler;
import org.halvors.quantum.common.tile.particle.TileAccelerator;
import org.halvors.quantum.common.tile.particle.TileFulmination;
import org.halvors.quantum.common.tile.reactor.TileElectricTurbine;
import org.halvors.quantum.common.tile.reactor.TileGasFunnel;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.common.tile.reactor.fission.TileSiren;
import org.halvors.quantum.common.tile.reactor.fission.TileThermometer;
import org.halvors.quantum.common.tile.reactor.fusion.TileElectromagnet;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasma;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasmaHeater;
import org.halvors.quantum.common.utility.ResourceUtility;
import org.halvors.quantum.common.utility.type.ResourceType;

import java.util.List;

/**
 * This is the Quantum class, which is the main class of this mod.
 *
 * @author halvors
 */
@Mod(modid = Reference.ID,
     name = Reference.NAME,
     version = Reference.VERSION,
     dependencies = "after:CoFHCore;" +
                    "after:Mekanism",
     guiFactory = "org.halvors." + Reference.ID + ".client.gui.configuration.GuiConfiguationFactory")
public class Quantum implements IUpdatableMod {
	// The instance of your mod that Forge uses.
	@Mod.Instance(value = Reference.ID)
	private static Quantum instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "org.halvors." + Reference.ID + ".client.ClientProxy", serverSide = "org.halvors." + Reference.ID + ".common.CommonProxy")
	private static CommonProxy proxy;

	// ConfigurationManager
	private static Configuration configuration;

	// Network
	private static final PacketHandler packetHandler = new PacketHandler();

	// Logger
	private static final Logger logger = LogManager.getLogger(Reference.ID);

	// Creative Tab
	private static final CreativeTabQuantum creativeTab = new CreativeTabQuantum();

	// Grids
	private static final ThermalGrid thermalGrid = new ThermalGrid();

	// Fluids
	public static final Fluid fluidDeuterium = new Fluid("deuterium", ResourceUtility.getResource(ResourceType.TEXTURE_BLOCKS, "deuterium"), ResourceUtility.getResource(ResourceType.TEXTURE_BLOCKS, "deuterium")).setGaseous(true);
	public static final Fluid fluidUraniumHexaflouride = new Fluid("uraniumHexafluoride", ResourceUtility.getResource(ResourceType.TEXTURE_BLOCKS, "uraniumHexafluoride"), ResourceUtility.getResource(ResourceType.TEXTURE_BLOCKS, "uraniumHexafluoride")).setGaseous(true);
	public static final Fluid fluidPlasma = new Fluid("plasma", ResourceUtility.getResource(ResourceType.TEXTURE_BLOCKS, "plasma"), ResourceUtility.getResource(ResourceType.TEXTURE_BLOCKS, "plasma")).setGaseous(true);
	public static final Fluid fluidSteam = new Fluid("steam", ResourceUtility.getResource(ResourceType.TEXTURE_BLOCKS, "steam"), ResourceUtility.getResource(ResourceType.TEXTURE_BLOCKS, "steam")).setGaseous(true);
	public static final Fluid fluidTritium = new Fluid("tritium", ResourceUtility.getResource(ResourceType.TEXTURE_BLOCKS, "tritium"), ResourceUtility.getResource(ResourceType.TEXTURE_BLOCKS, "tritium")).setGaseous(true);
	public static final Fluid fluidToxicWaste = new Fluid("toxicWaste", ResourceUtility.getResource(ResourceType.TEXTURE_BLOCKS, "toxicWaste"), ResourceUtility.getResource(ResourceType.TEXTURE_BLOCKS, "toxicWaste"));

	public static FluidStack fluidStackDeuterium;
	public static FluidStack fluidStackUraniumHexaflouride;
	public static FluidStack fluidStackSteam;
	public static FluidStack fluidStackTritium;
	public static FluidStack fluidStackToxicWaste;
	public static FluidStack fluidStackWater;

	// Blocks
	public static Block blockAccelerator;
	public static Block blockChemicalExtractor;
	public static Block blockControlRod;
	public static Block blockElectromagnet;
	public static Block blockFulmination;
	public static Block blockGasCentrifuge;
	public static Block blockGasFunnel;
	public static Block blockNuclearBoiler;
	public static Block blockSiren;
	public static Block blockThermometer;
	public static Block blockUraniumOre;
	public static Block blockPlasma;
    public static Block blockPlasmaHeater;
	public static Block blockQuantumAssembler;
	public static Block blockRadioactiveGrass;
	public static Block blockReactorCell;
	public static BlockFluidClassic blockToxicWaste;
	public static Block blockElectricTurbine;

	public static Block blockCreativeBuilder;

	// Items
	// Cells
	public static Item itemAntimatterCell;
	public static Item itemBreederFuel;
	public static Item itemCell;
	public static Item itemDarkMatterCell;
	public static Item itemDeuteriumCell;
	public static Item itemFissileFuel;
	public static Item itemTritiumCell;
	public static Item itemWaterCell;

	// Buckets
	public static Item itemBucketToxicWaste;

	// Uranium
	public static Item itemUranium;
	public static Item itemYellowCake;

	// Hazmat
	public static ItemArmor itemHazmatMask;
	public static ItemArmor itemHazmatBody;
	public static ItemArmor itemHazmatLeggings;
	public static ItemArmor itemHazmatBoots;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Initialize configuration.
        configuration = new Configuration(event.getSuggestedConfigurationFile());

		// Load the configuration.
		ConfigurationManager.loadConfiguration(configuration);

		// Check for updates.
		//FMLCommonHandler.instance().bus().register(new UpdateManager(this, Reference.RELEASE_URL, Reference.DOWNLOAD_URL));

		// Mod integration.
		logger.log(Level.INFO, "CoFHCore integration is " + (Integration.isCoFHCoreEnabled ? "enabled" : "disabled") + ".");
		logger.log(Level.INFO, "Mekanism integration is " + (Integration.isMekanismEnabled ? "enabled" : "disabled") + ".");

		// Call functions for adding blocks, items, etc.
		registerFluids();
		registerBlocks();
		registerTileEntities();
		registerItems();
		registerFluidContainers();
		registerEntities();
		registerRecipes();

		// Calling proxy handler.
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		// Register event handlers.
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(new PlayerEventHandler());
		MinecraftForge.EVENT_BUS.register(new ExplosionEventHandler());
		MinecraftForge.EVENT_BUS.register(new ThermalEventHandler());

		// Register the proxy as our GuiHandler to NetworkRegistry.
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		// Register event buses. TODO: Move this to a custom event handler?
		MinecraftForge.EVENT_BUS.register(FulminationHandler.INSTANCE);

		// Adde schematics to the creative builder.
		BlockCreativeBuilder.registerSchematic(new SchematicAccelerator());
		BlockCreativeBuilder.registerSchematic(new SchematicBreedingReactor());
		BlockCreativeBuilder.registerSchematic(new SchematicFissionReactor());
		BlockCreativeBuilder.registerSchematic(new SchematicFusionReactor());

		if (ConfigurationManager.General.allowOreDictionaryCompatibility) {
			OreDictionary.registerOre("ingotUranium", itemUranium);
			OreDictionary.registerOre("dustUranium", itemYellowCake);
		}

		OreDictionary.registerOre("oreUranium", new ItemStack(blockUraniumOre));
		OreDictionary.registerOre("breederUranium", new ItemStack(itemUranium, 1, 1));
		OreDictionary.registerOre("blockRadioactiveGrass", blockRadioactiveGrass);
		OreDictionary.registerOre("cellEmpty", itemCell);
		OreDictionary.registerOre("cellUranium", itemFissileFuel);
		OreDictionary.registerOre("cellTritium", itemTritiumCell);
		OreDictionary.registerOre("cellDeuterium", itemDeuteriumCell);
		OreDictionary.registerOre("cellWater.json", itemWaterCell);
		OreDictionary.registerOre("darkmatter", itemDarkMatterCell);
		OreDictionary.registerOre("antimatterMilligram", new ItemStack(itemAntimatterCell, 1, 0));
		OreDictionary.registerOre("antimatterGram", new ItemStack(itemAntimatterCell, 1, 1));

		ForgeChunkManager.setForcedChunkLoadingCallback(this, new ForgeChunkManager.LoadingCallback() {
			@Override
			public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world) {
				for (ForgeChunkManager.Ticket ticket : tickets) {
					if (ticket.getType() == ForgeChunkManager.Type.ENTITY) {
						if (ticket.getEntity() != null) {
							if (ticket.getEntity() instanceof EntityParticle) {
								((EntityParticle) ticket.getEntity()).updateTicket = ticket;
							}
						}
					}
				}
			}
		});

		// Register packets.
		packetHandler.init();

		// Calling proxy handler.
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (!UpdateTicker.getInstance().isAlive()) {
			UpdateTicker.getInstance().start();
		}

		// Register grids.
		UpdateTicker.addNetwork(thermalGrid);

		// Calling proxy handler.
		proxy.postInit(event);
	}

	private void registerFluids() {
		// Register fluids.
		FluidRegistry.registerFluid(fluidDeuterium);
		FluidRegistry.registerFluid(fluidUraniumHexaflouride);
		FluidRegistry.registerFluid(fluidPlasma);
		FluidRegistry.registerFluid(fluidSteam);
		FluidRegistry.registerFluid(fluidTritium);
		FluidRegistry.registerFluid(fluidToxicWaste);

		fluidStackDeuterium = new FluidStack(fluidDeuterium, 0);
		fluidStackUraniumHexaflouride = new FluidStack(fluidUraniumHexaflouride, 0);
		fluidStackSteam = new FluidStack(fluidSteam, 0);
		fluidStackTritium = new FluidStack(fluidTritium, 0);
		fluidStackToxicWaste = new FluidStack(fluidToxicWaste, 0);
		fluidStackWater = new FluidStack(FluidRegistry.WATER, 0);
	}

	private void registerBlocks() {
		// Register blocks.
		blockAccelerator = new BlockAccelerator();
		blockChemicalExtractor = new BlockChemicalExtractor();
		blockControlRod = new BlockControlRod();
		blockElectricTurbine = new BlockElectricTurbine();
		blockElectromagnet = new BlockElectromagnet();
		blockFulmination = new BlockFulmination();
		blockGasCentrifuge = new BlockGasCentrifuge();
		blockGasFunnel = new BlockGasFunnel();
		blockNuclearBoiler = new BlockNuclearBoiler();
		blockSiren = new BlockSiren();
		blockThermometer = new BlockThermometer();
		blockUraniumOre = new BlockUraniumOre();
		blockPlasma = new BlockPlasma();
		fluidPlasma.setBlock(blockPlasma);
		blockPlasmaHeater = new BlockPlasmaHeater();
		blockQuantumAssembler = new BlockQuantumAssembler();
		blockRadioactiveGrass = new BlockRadioactiveGrass();
		blockReactorCell = new BlockReactorCell();
		blockToxicWaste = new BlockToxicWaste();

		blockCreativeBuilder = new BlockCreativeBuilder();

		register(blockAccelerator);
		register(blockChemicalExtractor);
		register(blockControlRod);
		register(blockElectricTurbine);
		register(blockElectromagnet, new ItemBlockMetadata(blockElectromagnet));
		register(blockFulmination);
		register(blockGasCentrifuge);
		register(blockGasFunnel);
		register(blockNuclearBoiler);
		register(blockSiren);
		register(blockThermometer, new ItemBlockThermometer(blockThermometer));
		register(blockUraniumOre);
		register(blockPlasma);
		register(blockPlasmaHeater);
		register(blockQuantumAssembler);
		register(blockRadioactiveGrass);
		register(blockReactorCell);
		register(blockToxicWaste);

		register(blockCreativeBuilder);
	}

	private void registerTileEntities() {
		// Register tile entities.
		GameRegistry.registerTileEntity(TileAccelerator.class, "tileAccelerator");
		GameRegistry.registerTileEntity(TileChemicalExtractor.class, "tileChemicalExtractor");
		GameRegistry.registerTileEntity(TileElectricTurbine.class, "tileElectricTurbine");
		GameRegistry.registerTileEntity(TileElectromagnet.class, "tileElectromagnet");
		GameRegistry.registerTileEntity(TileFulmination.class, "tileFulmination");
		GameRegistry.registerTileEntity(TileGasCentrifuge.class, "tileGasCentrifuge");
		GameRegistry.registerTileEntity(TileGasFunnel.class, "tileGasFunnel");
		GameRegistry.registerTileEntity(TileNuclearBoiler.class, "tileNuclearBoiler");
		GameRegistry.registerTileEntity(TileSiren.class, "tileSiren");
		GameRegistry.registerTileEntity(TileThermometer.class, "tileThermometer");
		GameRegistry.registerTileEntity(TilePlasma.class, "tilePlasma");
        GameRegistry.registerTileEntity(TilePlasmaHeater.class, "tilePlasmaHeater");
		GameRegistry.registerTileEntity(TileQuantumAssembler.class, "tileQuantumAssembler");
		GameRegistry.registerTileEntity(TileReactorCell.class, "tileReactorCell");
	}

	private void registerItems() {
		// Register items.
		// Cells
		itemAntimatterCell = new ItemAntimatterCell();
		itemBreederFuel = new ItemBreederFuel();
		itemCell = new ItemCell("cellEmpty");
		itemDarkMatterCell = new ItemDarkmatterCell();
		itemDeuteriumCell = new ItemCell("cellDeuterium");
		itemFissileFuel = new ItemFissileFuel();
		itemTritiumCell = new ItemCell("cellTritium");
		itemWaterCell = new ItemCell("cellWater");

		// Buckets
		itemBucketToxicWaste = new ItemBucketToxicWaste();

		// Uranium
		itemUranium = new ItemUranium();
		itemYellowCake = new ItemRadioactive("yellowcake");

		// Hazmat
		itemHazmatMask = new ItemArmorHazmat("hazmatMask", EntityEquipmentSlot.HEAD);
		itemHazmatBody = new ItemArmorHazmat("hazmatBody", EntityEquipmentSlot.CHEST);
		itemHazmatLeggings = new ItemArmorHazmat("hazmatLeggings", EntityEquipmentSlot.LEGS);
		itemHazmatBoots = new ItemArmorHazmat("hazmatBoots", EntityEquipmentSlot.FEET);

		GameRegistry.register(itemAntimatterCell);
		GameRegistry.register(itemBreederFuel);
		GameRegistry.register(itemCell);
		GameRegistry.register(itemDarkMatterCell);
		GameRegistry.register(itemDeuteriumCell);
		GameRegistry.register(itemFissileFuel);
		GameRegistry.register(itemTritiumCell);
		GameRegistry.register(itemWaterCell);
		GameRegistry.register(itemBucketToxicWaste);

		GameRegistry.register(itemUranium);
		GameRegistry.register(itemYellowCake);

		GameRegistry.register(itemHazmatMask);
		GameRegistry.register(itemHazmatBody);
		GameRegistry.register(itemHazmatLeggings);
		GameRegistry.register(itemHazmatBoots);
	}

	private void registerFluidContainers() {
		// Register fluid containers.
		FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("deuterium"), 200), new ItemStack(itemDeuteriumCell), new ItemStack(itemCell));
		FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("tritium"), 200), new ItemStack(itemTritiumCell), new ItemStack(itemCell));
		FluidContainerRegistry.registerFluidContainer(fluidToxicWaste, new ItemStack(itemBucketToxicWaste), new ItemStack(Items.BUCKET));
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.WATER, new ItemStack(itemWaterCell), new ItemStack(itemCell));
	}

	private void registerEntities() {
		// Register entities.
		EntityRegistry.registerModEntity(EntityParticle.class, "Particle", 0, this, 80, 3, true);
	}

	private void registerRecipes() {
		// Register recipes.

		// Cells
		// Antimatter
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(itemAntimatterCell, 1, 1), itemAntimatterCell, itemAntimatterCell, itemAntimatterCell, itemAntimatterCell, itemAntimatterCell, itemAntimatterCell, itemAntimatterCell, itemAntimatterCell));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(itemAntimatterCell, 8, 0), new ItemStack(itemAntimatterCell, 1, 1)));

		// Breeder Fuel Rod
		GameRegistry.addRecipe(new ShapedOreRecipe(itemBreederFuel, "CUC", "CUC", "CUC", 'U', "breederUranium", 'C', "cellEmpty"));

		// Empty Cell
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemCell, 16), " T ", "TGT", " T ", 'T', "ingotTin", 'G', Blocks.GLASS));

		// Fissile Fuel
		GameRegistry.addRecipe(new ShapedOreRecipe(itemFissileFuel, "CUC", "CUC", "CUC", 'U', "ingotUranium", 'C', "cellEmpty"));

		// Water Cell
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(itemWaterCell), "cellEmpty", Items.WATER_BUCKET));

		// Hazmat
		//GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatMask, "SSS", "BAB", "SCS", 'A', Items.leather_helmet, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), 'S', Blocks.cloth }));
		//GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatBody, "SSS", "BAB", "SCS", 'A', Items.leather_plate, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), 'S', Block.cloth }));
		//GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatLeggings, "SSS", "BAB", "SCS", 'A', Items.leather_legs, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), 'S', Block.cloth }));
		//GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatBoots, "SSS", "BAB", "SCS", 'A', Items.leather_boots, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), 'S', Block.cloth }));

		// Particle Accelerator
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockAccelerator, "SCS", "CMC", "SCS", 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes), 'C', UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes)));

		// Chemical Extractor
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockChemicalExtractor, "BSB", "MCM", "BSB", 'C', UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'B', UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes), 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

		// Control Rod
		GameRegistry.addRecipe(new ShapedOreRecipe(blockControlRod, "I", "I", "I", 'I', Items.IRON_INGOT));

		// Turbine
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockElectricTurbine, " B ", "BMB", " B ", 'B', UniversalRecipe.SECONDARY_PLATE.get(Settings.allowAlternateRecipes), 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

		// Electromagnet
		//GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockElectromagnet, 2, 0), "BBB", "BMB", "BBB", 'B', UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes), 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

		// Electromagnet Glass
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockElectromagnet, 1, 1), blockElectromagnet, Blocks.GLASS));

		// Fulmination Generator
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockFulmination, "OSO", "SCS", "OSO", 'O', Blocks.obsidian, 'C', UniversalRecipe.CIRCUIT_T2.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes)));

		// Gas Centrifuge
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockGasCentrifuge, "BSB", "MCM", "BSB", 'C', UniversalRecipe.CIRCUIT_T2.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'B', UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes), 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

		// Gas Funnel
		//GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockGasFunnel, 2), " B ", "B B", "B B", 'B', UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockGasFunnel, 2), " B ", "B B", "B B", 'B', "ingotIron"));

		// Nuclear Boiler
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockNuclearBoiler, "S S", "FBF", "SMS", 'F', Block.furnaceIdle, 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'B', Item.bucketEmpty, 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

		// Siren
		//GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockSiren, 2), "NPN", 'N', Blocks.noteblock, 'P', UniversalRecipe.SECONDARY_PLATE.get(Settings.allowAlternateRecipes)));

		// Thermometer
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockThermometer, "SSS", "GCG", "GSG", 'S', UniversalRecipe.PRIMARY_METAL.get(Settings.allowAlternateRecipes), 'G', Blocks.glass, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes)));

		// Plasma Heater
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockPlasmaHeater, "CPC", "PFP", "CPC", 'P', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'F', blockReactorCell, 'C', UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes)));

		// Quantum Assembler
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockQuantumAssembler, "CCC", "SXS", "SSS", 'X', blockGasCentrifuge, 'C', UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes)));

		// Fission Reactor
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockReactorCell, "SCS", "MEM", "SCS", 'E', "cellEmpty", 'C', UniversalRecipe.CIRCUIT_T2.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

		/*
		// IC2 Recipes
		if (Loader.isModLoaded("IC2") && Settings.allowAlternateRecipes)
		{
			OreDictionary.registerOre("cellEmpty", Items.getItem("cell"));

			// Check to make sure we have actually registered the Ore, otherwise tell the user about
			// it.
			String cellEmptyName = OreDictionary.getOreName(OreDictionary.getOreID("cellEmpty"));
			if (cellEmptyName == "Unknown")
			{
				ResonantInduction.LOGGER.info("Unable to register cellEmpty in OreDictionary!");
			}

			// IC2 exchangeable recipes
			GameRegistry.addRecipe(new ShapelessOreRecipe(itemYellowCake, Items.getItem("reactorUraniumSimple")));
			GameRegistry.addRecipe(new ShapelessOreRecipe(Items.getItem("cell"), itemCell));
			GameRegistry.addRecipe(new ShapelessOreRecipe(itemCell, "cellEmpty"));
		}
		*/
	}

	/*
	@SubscribeEvent
	public void onFillBucketEvent(FillBucketEvent event) {
		if (!event.getWorld().isRemote && event.getTarget() != null && event.getWorld().typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			VectorWorld pos = new VectorWorld(event.getWorld(), event.getTarget());

			if (pos.getBlock() == blockToxicWaste) {
				pos.setBlock(Blocks.AIR);

				event.setFilledBucket(new ItemStack(itemBucketToxicWaste));
				event.setResult(Event.Result.ALLOW);
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void preTextureHook(TextureStitchEvent.Pre event) {
		if (event.getMap().getTextureType() == 0) {
			RenderUtility.registerIcon(Reference.PREFIX + "atomic_edge", event.getMap());
			RenderUtility.registerIcon(Reference.PREFIX + "gasFunnel_edge", event.getMap());

			RenderUtility.registerIcon(Reference.PREFIX + "deuterium", event.getMap());
			RenderUtility.registerIcon(Reference.PREFIX + "steam", event.getMap());
			RenderUtility.registerIcon(Reference.PREFIX + "tritium", event.getMap());
			RenderUtility.registerIcon(Reference.PREFIX + "uraniumHexafluoride", event.getMap());
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void postTextureHook(TextureStitchEvent.Post event) {
		fluidToxicWaste.setIcons(blockToxicWaste.getIcon(0, 0));
		fluidPlasma.setIcons(blockPlasma.getIcon(0, 0));
		fluidDeuterium.setIcons(RenderUtility.loadedIconMap.get(Reference.PREFIX + "deuterium"));
		fluidSteam.setIcons(RenderUtility.loadedIconMap.get(Reference.PREFIX + "steam"));
		fluidTritium.setIcons(RenderUtility.loadedIconMap.get(Reference.PREFIX + "tritium"));
		fluidUraniumHexaflouride.setIcons(RenderUtility.loadedIconMap.get(Reference.PREFIX + "uraniumHexafluoride"));
	}
	*/

	public static Quantum getInstance() {
		return instance;
	}

	public static CommonProxy getProxy() {
		return proxy;
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

	public static PacketHandler getPacketHandler() {
		return packetHandler;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static CreativeTabQuantum getCreativeTab() {
		return creativeTab;
	}

	@Override
	public String getModId() {
		return Reference.ID;
	}

	@Override
	public String getModName() {
		return Reference.NAME;
	}

	@Override
	public String getModVersion() {
		return Reference.VERSION;
	}

	private static <T extends Block> T register(T block, ItemBlock itemBlock) {
		GameRegistry.register(block);
		GameRegistry.register(itemBlock);

		if (block instanceof BlockQuantum) {
			((BlockQuantum) block).registerItemModel(itemBlock);
		}

		return block;
	}

	private static <T extends Block> T register(T block) {
		ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(block.getRegistryName());

		return register(block, itemBlock);
	}
}