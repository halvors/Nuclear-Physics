package org.halvors.quantum;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.halvors.quantum.common.CommonProxy;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.ConfigurationManager.Integration;
import org.halvors.quantum.common.QuantumCreativeTab;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.base.IUpdatableMod;
import org.halvors.quantum.common.block.Block;
import org.halvors.quantum.common.block.BlockMachine;
import org.halvors.quantum.common.block.BlockToxicWaste;
import org.halvors.quantum.common.event.PlayerEventHandler;
import org.halvors.quantum.common.item.*;
import org.halvors.quantum.common.item.armor.ItemArmorHazmat;
import org.halvors.quantum.common.tile.machine.TileEntityElectricityMeter;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.common.transform.vector.VectorWorld;
import org.halvors.quantum.common.updater.UpdateManager;

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
	@Instance(value = Reference.ID)
	public static Quantum instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "org.halvors." + Reference.ID + ".client.ClientProxy", serverSide = "org.halvors." + Reference.ID + ".common.CommonProxy")
	public static CommonProxy proxy;

	// Logger
	private static final Logger logger = LogManager.getLogger(Reference.ID);

	// ConfigurationManager
	private static Configuration configuration;

	// Creative Tab
	private static final QuantumCreativeTab creativeTab = new QuantumCreativeTab();

	// Fluids
	public static final Fluid fluidDeuterium = new Fluid("deuterium").setGaseous(true);
	public static final Fluid fluidUraniumHexaflouride = new Fluid("uraniumhexafluoride").setGaseous(true);
	public static final Fluid fluidPlasma = new Fluid("plasma").setGaseous(true);
	public static final Fluid fluidSteam = new Fluid("steam").setGaseous(true);
	public static final Fluid fluidTritium = new Fluid("tritium").setGaseous(true);
	public static final Fluid fluidToxicWaste = new Fluid("toxicwaste");

	public static FluidStack fluidStackDeuterium;
	public static FluidStack fluidStackUraniumHexaflouride;
	public static FluidStack fluidStackSteam;
	public static FluidStack fluidStackTritium;
	public static FluidStack fluidStackToxicWaste;
	public static FluidStack fluidStackWater;

	// Blocks
	public static final Block blockMachine = new BlockMachine();

	/*
	blockRadioactive = contentRegistry.createBlock(BlockRadioactive.class).func_71864_b("resonantinduction:radioactive").func_111022_d("resonantinduction:radioactive").func_71849_a(TabRI.DEFAULT);
	blockUraniumOre = contentRegistry.createBlock(BlockUraniumOre.class);
	blockElectricTurbine = contentRegistry.createTile(BlockElectricTurbine.class, TileElectricTurbine.class);
	blockCentrifuge = contentRegistry.createTile(BlockCentrifuge.class, TileCentrifuge.class);
	blockReactorCell = contentRegistry.newBlock(TileReactorCell.class);
	blockNuclearBoiler = contentRegistry.createTile(BlockNuclearBoiler.class, TileNuclearBoiler.class);
	blockChemicalExtractor = contentRegistry.createTile(BlockChemicalExtractor.class, TileChemicalExtractor.class);
	blockFusionCore = contentRegistry.createTile(BlockPlasmaHeater.class, TilePlasmaHeater.class);
	blockControlRod = contentRegistry.newBlock(TileControlRod.class);
	blockThermometer = contentRegistry.newBlock(TileThermometer.class);
	*/

	//public static final Block blockPlasma = new TilePlasma();

	/*
	blockPlasma = contentRegistry.newBlock(TilePlasma.class);
	blockElectromagnet = contentRegistry.newBlock(TileElectromagnet.class);
	blockSiren = contentRegistry.newBlock(TileSiren.class);
	blockSteamFunnel = contentRegistry.newBlock(TileFunnel.class);
	blockAccelerator = contentRegistry.createTile(BlockAccelerator.class, TileAccelerator.class);
	blockFulmination = contentRegistry.newBlock(TileFulmination.class);
	blockQuantumAssembler = contentRegistry.newBlock(TileQuantumAssembler.class);

	*/

	public static BlockFluidClassic blockToxicWaste;

	// Items
	//public static final ItemQuantum itemMultimeter = new ItemMultimeter();

	// Cells
	public static final Item itemAntimatter = new ItemAntimatter();
	public static final Item itemBreedingRod = new ItemBreederFuel();
	public static final Item itemCell = new ItemCell("cellEmpty");
	public static final Item itemDarkMatter = new ItemCell("darkMatter");
	public static final Item itemDeuteriumCell = new ItemCell("cellDeuterium");
	public static final Item itemFissileFuel = new ItemFissileFuel();
	public static final Item itemTritiumCell = new ItemCell("cellTritium");
	public static final Item itemWaterCell = new ItemCell("cellWater");

	// Buckets
	public static Item itemBucketToxicWaste;

	// Uranium
	public static final Item itemUranium = new ItemUranium();
	public static final Item itemYellowCake = new ItemRadioactive("yellowcake");

	// Hazmat
	public static final Item itemHazmatMask = new ItemArmorHazmat("hazmatMask", 0);
	public static final Item itemHazmatBody = new ItemArmorHazmat("hazmatBody", 1);
	public static final Item itemHazmatLeggings = new ItemArmorHazmat("hazmatLeggings", 2);
	public static final Item itemHazmatBoots = new ItemArmorHazmat("hazmatBoots", 3);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Initialize configuration.
        configuration = new Configuration(event.getSuggestedConfigurationFile());

		// Load the configuration.
		ConfigurationManager.loadConfiguration(configuration);

		// Check for updates.
		FMLCommonHandler.instance().bus().register(new UpdateManager(this, Reference.RELEASE_URL, Reference.DOWNLOAD_URL));

		// Mod integration.
		logger.log(Level.INFO, "CoFHCore integration is " + (Integration.isCoFHCoreEnabled ? "enabled" : "disabled") + ".");
		logger.log(Level.INFO, "Mekanism integration is " + (Integration.isMekanismEnabled ? "enabled" : "disabled") + ".");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Register the our EventHandler.
		FMLCommonHandler.instance().bus().register(new PlayerEventHandler());

		// Register the proxy as our GuiHandler to NetworkRegistry.
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		// Register event bus.
		MinecraftForge.EVENT_BUS.register(this);

		// Call functions for adding blocks, items, etc.
		registerFluids();
		registerBlocks();
		registerItems();
		registerFluidContainers();
		registerTileEntities();
		registerRecipes();
	}

	private void registerFluids() {
		// Register fluids.
		FluidRegistry.registerFluid(fluidDeuterium);
		FluidRegistry.registerFluid(fluidUraniumHexaflouride);
		FluidRegistry.registerFluid(fluidPlasma);
		FluidRegistry.registerFluid(fluidSteam);
		FluidRegistry.registerFluid(fluidTritium);
		FluidRegistry.registerFluid(fluidToxicWaste);

		fluidStackDeuterium = new FluidStack(FluidRegistry.getFluid("deuterium"), 0);
		fluidStackUraniumHexaflouride = new FluidStack(fluidUraniumHexaflouride, 0);
		fluidStackSteam = new FluidStack(FluidRegistry.getFluid("steam"), 0);
		fluidStackTritium = new FluidStack(FluidRegistry.getFluid("tritium"), 0);
		fluidStackToxicWaste = new FluidStack(FluidRegistry.getFluid("toxicwaste"), 0);
		fluidStackWater = new FluidStack(FluidRegistry.WATER, 0);
		fluidStackWater = new FluidStack(FluidRegistry.WATER, 0);
	}

	private void registerBlocks() {
		// Register blocks.
		GameRegistry.registerBlock(blockMachine, ItemBlockMachine.class, "blockMachine");

		blockToxicWaste = new BlockToxicWaste();

		GameRegistry.registerBlock(blockToxicWaste, "blockToxicWaste");
	}

	private void registerItems() {
		// Register items.
		// Cells
		GameRegistry.registerItem(itemAntimatter, "antimatter");
		GameRegistry.registerItem(itemBreedingRod, "rodBreedingFuel");
		GameRegistry.registerItem(itemCell, "cellEmpty");
		GameRegistry.registerItem(itemDarkMatter, "darkMatter");
		GameRegistry.registerItem(itemDeuteriumCell, "cellDeuterium");
		GameRegistry.registerItem(itemFissileFuel, "rodFissileFuel");
		GameRegistry.registerItem(itemTritiumCell, "cellTritium");
		GameRegistry.registerItem(itemWaterCell, "cellWater");

		// Buckets
		itemBucketToxicWaste = new ItemBucketToxicWaste();

		GameRegistry.registerItem(itemBucketToxicWaste, "bucketToxicWaste");

		// Uranium
		GameRegistry.registerItem(itemUranium, "uranium");
		GameRegistry.registerItem(itemYellowCake, "yellowcake");

		// Hazmat
		GameRegistry.registerItem(itemHazmatMask, "itemHazmatMask");
		GameRegistry.registerItem(itemHazmatBody, "itemHazmatBody");
		GameRegistry.registerItem(itemHazmatLeggings, "itemHazmatLeggings");
		GameRegistry.registerItem(itemHazmatBoots, "itemHazmatBoots");
	}

	private void registerFluidContainers() {
		// Register fluid containers.
		//fluidPlasma.setBlock(blockPlasma);
		FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("deuterium"), 200), new ItemStack(itemDeuteriumCell), new ItemStack(itemCell));
		FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("tritium"), 200), new ItemStack(itemTritiumCell), new ItemStack(itemCell));
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluid("toxicwaste"), new ItemStack(itemBucketToxicWaste), new ItemStack(Items.bucket));
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.WATER, new ItemStack(itemWaterCell), new ItemStack(itemCell));
	}

	private void registerTileEntities() {
		// Register tile entities.
		GameRegistry.registerTileEntity(TileEntityElectricityMeter.class, "tileElectricityMeter");
	}

	private void registerRecipes() {

	}

	@SubscribeEvent
	public void fillBucketEvent(FillBucketEvent event) {
		if (!event.world.isRemote && event.target != null && event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			VectorWorld pos = new VectorWorld(event.world, event.target);

			if (pos.getBlock() == blockToxicWaste) {
				pos.setBlock(Blocks.air);

				event.result = new ItemStack(itemBucketToxicWaste);
				event.setResult(Event.Result.ALLOW);
			}
		}
	}

	public static Quantum getInstance() {
		return instance;
	}

	public static CommonProxy getProxy() {
		return proxy;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static QuantumCreativeTab getCreativeTab() {
		return creativeTab;
	}

	public static Configuration getConfiguration() {
		return configuration;
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
}