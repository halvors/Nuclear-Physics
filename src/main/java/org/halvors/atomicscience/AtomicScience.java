package org.halvors.atomicscience;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import mekanism.api.ItemRetriever;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.halvors.electrometrics.common.CommonProxy;
import org.halvors.electrometrics.common.ConfigurationManager;
import org.halvors.electrometrics.common.ConfigurationManager.Integration;
import org.halvors.electrometrics.common.CreativeTab;
import org.halvors.electrometrics.common.Reference;
import org.halvors.electrometrics.common.base.IUpdatableMod;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.base.Tier;
import org.halvors.electrometrics.common.block.Block;
import org.halvors.electrometrics.common.block.BlockMachine;
import org.halvors.electrometrics.common.event.PlayerEventHandler;
import org.halvors.electrometrics.common.item.ItemBlockMachine;
import org.halvors.electrometrics.common.item.ItemMultimeter;
import org.halvors.electrometrics.common.tile.machine.TileEntityElectricityMeter;
import org.halvors.electrometrics.common.updater.UpdateManager;

/**
 * This is the AtomicScience class, which is the main class of this mod.
 *
 * @author halvors
 */
@Mod(modid = Reference.ID,
     name = Reference.NAME,
     version = Reference.VERSION,
     dependencies = "after:Mekanism",
     guiFactory = "org.halvors." + Reference.ID + ".client.gui.configuration.GuiConfiguationFactory")
public class AtomicScience implements IUpdatableMod {
	// The instance of your mod that Forge uses.
	@Instance(value = Reference.ID)
	public static org.halvors.atomicscience.AtomicScience instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "org.halvors.atomicscience.client.ClientProxy", serverSide = "org.halvors.atomicscience.common.CommonProxy")
	public static CommonProxy proxy;

	// Logger instance.
	private static final Logger logger = LogManager.getLogger(Reference.ID);

	// Creative tab.
	private static final CreativeTab creativeTab = new CreativeTab();

	// Items.
	public static final Item itemMultimeter = new ItemMultimeter();

	// Blocks.
	public static final Block blockMachine = new BlockMachine();

	// ConfigurationManager.
	private static Configuration configuration;


	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Initialize configuration.
        configuration = new Configuration(event.getSuggestedConfigurationFile());

		// Load the configuration.
		ConfigurationManager.loadConfiguration(configuration);

		// Check for updates.
		FMLCommonHandler.instance().bus().register(new UpdateManager(this, Reference.RELEASE_URL, Reference.DOWNLOAD_URL));

		// Mod integration.
		logger.log(Level.INFO, "BuildCraft integration is " + (Integration.isBuildCraftEnabled ? "enabled" : "disabled") + ".");
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
		addItems();
		addBlocks();
		addTileEntities();
		addRecipes();
	}

	private void addItems() {
		// Register items.
		GameRegistry.registerItem(itemMultimeter, "itemMultimeter");
	}

	private void addBlocks() {
		// Register blocks.
		GameRegistry.registerBlock(blockMachine, ItemBlockMachine.class, "blockMachine");
	}

	private void addTileEntities() {
		// Register tile entities.
		GameRegistry.registerTileEntity(TileEntityElectricityMeter.class, "tileEntityElectricityMeter");
	}

	private void addRecipes() {
		// Register recipes.
        Item circuit = Items.repeater;
        ItemStack battery = new ItemStack(Items.diamond);
        ItemStack cable = new ItemStack(Items.gold_ingot);
        ItemStack casing = new ItemStack(Blocks.iron_block);

        if (Integration.isMekanismEnabled) {
            circuit = ItemRetriever.getItem("ControlCircuit").getItem(); // Basic control circuit.
            battery = new ItemStack(ItemRetriever.getItem("EnergyTablet").getItem(), 1, 100); // Uncharged battery.
            cable = new ItemStack(ItemRetriever.getItem("PartTransmitter").getItem(), 8); // Basic universal cable.
            casing = new ItemStack(ItemRetriever.getBlock("BasicBlock").getItem(), 1, 8); // Steel casing.
        }

        // Multimeter
        GameRegistry.addRecipe(new ShapedOreRecipe(itemMultimeter,
                "RGR",
                "IMI",
                "CBC",
                'R', Items.redstone,
                'G', "paneGlass",
                'I', OreDictionary.doesOreNameExist("ingotCopper") ? "ingotCopper" : Items.gold_ingot,
                'M', Items.clock,
                'C', circuit,
                'B', battery));

        // Electricity Meter
        for (Tier.Electric electricTier : Tier.Electric.values()) {
            if (Integration.isMekanismEnabled) {
                cable = new ItemStack(ItemRetriever.getItem("PartTransmitter").getItem(), 8, electricTier.ordinal()); // Tier matching universal cable.
            }

            MachineType machineType = electricTier.getMachineType();
            ItemStack itemStackMachine = machineType.getItemStack();
            ItemBlockMachine itemBlockMachine = (ItemBlockMachine) itemStackMachine.getItem();
            itemBlockMachine.setElectricTier(itemStackMachine, electricTier);

            GameRegistry.addRecipe(itemStackMachine,
                    "RMR",
                    "C#C",
                    "RBR",
                    'R', Items.redstone,
                    'M', itemMultimeter,
                    'C', cable,
                    '#', casing,
                    'B', battery);
        }
	}

	public static org.halvors.atomicscience.AtomicScience getInstance() {
		return instance;
	}

	public static CommonProxy getProxy() {
		return proxy;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static CreativeTab getCreativeTab() {
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