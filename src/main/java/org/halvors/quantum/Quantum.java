package org.halvors.quantum;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
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
import org.halvors.quantum.common.event.PlayerEventHandler;
import org.halvors.quantum.common.item.*;
import org.halvors.quantum.common.item.armor.ItemArmorHazmat;
import org.halvors.quantum.common.tile.machine.TileEntityElectricityMeter;
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

	// Logger.
	private static final Logger logger = LogManager.getLogger(Reference.ID);

	// ConfigurationManager.
	private static Configuration configuration;

	// Creative Tab.
	private static final QuantumCreativeTab creativeTab = new QuantumCreativeTab();

	// Blocks.
	public static final Block blockMachine = new BlockMachine();

	// Items.
	//public static final ItemQuantum itemMultimeter = new ItemMultimeter();

	// Cells
	public static final Item itemAntimatter = new ItemAntimatter();
	//public static final Item itemBreedingRod = contentRegistry.createItem("rodBreederFuel", ItemBreederFuel.class);
	public static final Item itemCell = new ItemCell("cellEmpty");
	//public static final Item itemDarkMatter = contentRegistry.createItem("darkMatter", ItemDarkMatter.class);
	//public static final Item itemDeuteriumCell = contentRegistry.createItem("cellDeuterium", ItemCell.class);
	//public static final Item itemFissileFuel = contentRegistry.createItem("rodFissileFuel", ItemFissileFuel.class);
	//public static final Item itemTritiumCell = contentRegistry.createItem("cellTritium", ItemCell.class);
	public static final Item itemUranium = new ItemUranium();
	//public static final Item itemWaterCell = contentRegistry.createItem("cellWater", ItemCell.class);
	//public static final Item itemYellowCake = contentRegistry.createItem("yellowcake", ItemRadioactive.class);

	/*
	public static final Item itemCell = new ItemCell("cellEmpty");
	//var itemFissileFuel: ItemQuantum = new ItemFuelRod
	//var itemBreedingRod: ItemQuantum = new ItemBreederFuel
	//var itemDarkMatter: ItemQuantum = new ItemCell("darkMatter")
	public static final Item itemAntimatter = new ItemAntimatter();
	//var itemDeuteriumCell: ItemQuantum = new ItemCell("cellDeuterium")
	//var itemTritiumCell: ItemQuantum = new ItemCell("cellTritium")
	//var itemWaterCell: ItemQuantum = new ItemCell("cellWater")
	//var itemYellowCake: ItemQuantum = new ItemRadioactive().setTextureName(Reference.prefix + "yellowcake").setCreativeTab(EDXCreativeTab)
	//var itemUranium: ItemQuantum = new ItemUranium().setCreativeTab(EDXCreativeTab)
	*/

	// Buckets
	//var itemBucketToxic: ItemQuantum = null

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

		// Call functions for adding blocks, items, etc.
		registerBlocks();
		registerItems();
		registerTileEntities();
		registerRecipes();
	}

	private void registerBlocks() {
		// Register blocks.
		GameRegistry.registerBlock(blockMachine, ItemBlockMachine.class, "blockMachine");
	}

	private void registerItems() {
		// Register items.
		GameRegistry.registerItem(itemAntimatter, "itemAntimatter");
		//GameRegistry.registerItem(itemBreedingRod, "rodBreedingFuel");
		GameRegistry.registerItem(itemCell, "cellEmpty");
		//GameRegistry.registerItem(itemDarkMatter, "darkMatter");
		//GameRegistry.registerItem(itemDeuteriumCell, "cellDeuterium");
		//GameRegistry.registerItem(itemFissileFuel, "rodFissileFuel");
		//GameRegistry.registerItem(itemTritiumCell, "cellTritium");
		GameRegistry.registerItem(itemUranium, "itemUranium");
		//GameRegistry.registerItem(itemWaterCell, "cellWater");
		//GameRegistry.registerItem(itemYellowCake, "yellowcake");

		// Hazmat.
		GameRegistry.registerItem(itemHazmatMask, "itemHazmatMask");
		GameRegistry.registerItem(itemHazmatBody, "itemHazmatBody");
		GameRegistry.registerItem(itemHazmatLeggings, "itemHazmatLeggings");
		GameRegistry.registerItem(itemHazmatBoots, "itemHazmatBoots");
	}

	private void registerTileEntities() {
		// Register tile entities.
		GameRegistry.registerTileEntity(TileEntityElectricityMeter.class, "tileElectricityMeter");
	}

	private void registerRecipes() {

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