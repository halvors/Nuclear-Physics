package org.halvors.nuclearphysics.setup;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonSetup {
    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent e) {
        //RS.NETWORK_HANDLER.register();

        //NetworkNodeProxyCapability.register();

        //MinecraftForge.EVENT_BUS.register(new NetworkNodeListener());
        //MinecraftForge.EVENT_BUS.register(new NetworkListener());
        //MinecraftForge.EVENT_BUS.register(new BlockListener());
    }

    @SubscribeEvent
    public void onRegisterRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> e) {
        //e.getRegistry().register(new UpgradeWithEnchantedBookRecipeSerializer().setRegistryName(RS.ID, "upgrade_with_enchanted_book"));
    }

    @SubscribeEvent
    public void onRegisterBlocks(RegistryEvent.Register<Block> e) {
        /*
        e.getRegistry().register(new QuartzEnrichedIronBlock());
        e.getRegistry().register(new ControllerBlock(NetworkType.NORMAL));
        e.getRegistry().register(new ControllerBlock(NetworkType.CREATIVE));
        e.getRegistry().register(new MachineCasingBlock());
        e.getRegistry().register(new CableBlock());
        e.getRegistry().register(new DiskDriveBlock());
        e.getRegistry().register(new GridBlock(GridType.NORMAL));
        e.getRegistry().register(new GridBlock(GridType.CRAFTING));
        e.getRegistry().register(new GridBlock(GridType.PATTERN));
        e.getRegistry().register(new GridBlock(GridType.FLUID));

        for (ItemStorageType type : ItemStorageType.values()) {
            e.getRegistry().register(new StorageBlock(type));
        }

        for (FluidStorageType type : FluidStorageType.values()) {
            e.getRegistry().register(new FluidStorageBlock(type));
        }

        e.getRegistry().register(new ExternalStorageBlock());
        e.getRegistry().register(new ImporterBlock());
        e.getRegistry().register(new ExporterBlock());
        e.getRegistry().register(new NetworkReceiverBlock());
        e.getRegistry().register(new NetworkTransmitterBlock());
        e.getRegistry().register(new RelayBlock());
        e.getRegistry().register(new DetectorBlock());
        e.getRegistry().register(new SecurityManagerBlock());
        e.getRegistry().register(new InterfaceBlock());
        e.getRegistry().register(new FluidInterfaceBlock());
        e.getRegistry().register(new WirelessTransmitterBlock());
        e.getRegistry().register(new StorageMonitorBlock());
        e.getRegistry().register(new ConstructorBlock());
        e.getRegistry().register(new DestructorBlock());
        e.getRegistry().register(new DiskManipulatorBlock());
        e.getRegistry().register(new PortableGridBlock(PortableGridBlockItem.Type.NORMAL));
        e.getRegistry().register(new PortableGridBlock(PortableGridBlockItem.Type.CREATIVE));
        e.getRegistry().register(new CrafterBlock());
        e.getRegistry().register(new CrafterManagerBlock());
        e.getRegistry().register(new CraftingMonitorBlock());
        */
    }

    @SubscribeEvent
    public void onRegisterTiles(RegistryEvent.Register<TileEntityType<?>> e) {
        //e.getRegistry().register(registerTileDataParameters(TileEntityType.Builder.create(() -> new ControllerTile(NetworkType.NORMAL), RSBlocks.CONTROLLER).build(null).setRegistryName(RS.ID, "controller")));
        //e.getRegistry().register(registerTileDataParameters(TileEntityType.Builder.create(() -> new ControllerTile(NetworkType.CREATIVE), RSBlocks.CREATIVE_CONTROLLER).build(null).setRegistryName(RS.ID, "creative_controller")));
    }

    /*
    private <T extends TileEntity> TileEntityType<T> registerTileDataParameters(TileEntityType<T> t) {
        BaseTile tile = (BaseTile) t.create();

        tile.getDataManager().getParameters().forEach(TileDataManager::registerParameter);

        return t;
    }
    */

    @SubscribeEvent
    public void onRegisterContainers(RegistryEvent.Register<ContainerType<?>> e) {
        /*
        e.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> new FilterContainer(inv.player, inv.getCurrentItem(), windowId)).setRegistryName(RS.ID, "filter"));
        e.getRegistry().register(IForgeContainerType.create(((windowId, inv, data) -> new ControllerContainer(null, inv.player, windowId))).setRegistryName(RS.ID, "controller"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<DiskDriveContainer, DiskDriveTile>((windowId, inv, tile) -> new DiskDriveContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "disk_drive"));
        e.getRegistry().register(IForgeContainerType.create(new GridContainerFactory()).setRegistryName(RS.ID, "grid"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<StorageContainer, StorageTile>((windowId, inv, tile) -> new StorageContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "storage_block"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<FluidStorageContainer, FluidStorageTile>((windowId, inv, tile) -> new FluidStorageContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "fluid_storage_block"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<ExternalStorageContainer, ExternalStorageTile>((windowId, inv, tile) -> new ExternalStorageContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "external_storage"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<ImporterContainer, ImporterTile>((windowId, inv, tile) -> new ImporterContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "importer"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<ExporterContainer, ExporterTile>((windowId, inv, tile) -> new ExporterContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "exporter"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<NetworkTransmitterContainer, NetworkTransmitterTile>((windowId, inv, tile) -> new NetworkTransmitterContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "network_transmitter"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<RelayContainer, RelayTile>((windowId, inv, tile) -> new RelayContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "relay"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<DetectorContainer, DetectorTile>((windowId, inv, tile) -> new DetectorContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "detector"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<SecurityManagerContainer, SecurityManagerTile>((windowId, inv, tile) -> new SecurityManagerContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "security_manager"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<InterfaceContainer, InterfaceTile>((windowId, inv, tile) -> new InterfaceContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "interface"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<FluidInterfaceContainer, FluidInterfaceTile>((windowId, inv, tile) -> new FluidInterfaceContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "fluid_interface"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<WirelessTransmitterContainer, WirelessTransmitterTile>((windowId, inv, tile) -> new WirelessTransmitterContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "wireless_transmitter"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<StorageMonitorContainer, StorageMonitorTile>((windowId, inv, tile) -> new StorageMonitorContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "storage_monitor"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<ConstructorContainer, ConstructorTile>((windowId, inv, tile) -> new ConstructorContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "constructor"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<DestructorContainer, DestructorTile>((windowId, inv, tile) -> new DestructorContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "destructor"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<DiskManipulatorContainer, DiskManipulatorTile>((windowId, inv, tile) -> new DiskManipulatorContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "disk_manipulator"));
        e.getRegistry().register(IForgeContainerType.create(new PositionalTileContainerFactory<CrafterContainer, CrafterTile>((windowId, inv, tile) -> new CrafterContainer(tile, inv.player, windowId))).setRegistryName(RS.ID, "crafter"));
        e.getRegistry().register(IForgeContainerType.create(new CrafterManagerContainerFactory()).setRegistryName(RS.ID, "crafter_manager"));
        e.getRegistry().register(IForgeContainerType.create(new CraftingMonitorContainerFactory()).setRegistryName(RS.ID, "crafting_monitor"));
        e.getRegistry().register(IForgeContainerType.create(new WirelessCraftingMonitorContainerFactory()).setRegistryName(RS.ID, "wireless_crafting_monitor"));
        */
    }

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> e) {
        /*
        e.getRegistry().register(new CoreItem(CoreItem.Type.CONSTRUCTION));
        e.getRegistry().register(new CoreItem(CoreItem.Type.DESTRUCTION));
        e.getRegistry().register(new QuartzEnrichedIronItem());
        e.getRegistry().register(new ProcessorBindingItem());

        for (ProcessorItem.Type type : ProcessorItem.Type.values()) {
            e.getRegistry().register(new ProcessorItem(type));
        }

        e.getRegistry().register(new SiliconItem());

        e.getRegistry().register(new SecurityCardItem());
        e.getRegistry().register(new NetworkCardItem());

        for (ItemStorageType type : ItemStorageType.values()) {
            if (type != ItemStorageType.CREATIVE) {
                e.getRegistry().register(new StoragePartItem(type));
            }

            e.getRegistry().register(new StorageDiskItem(type));
        }

        for (FluidStorageType type : FluidStorageType.values()) {
            if (type != FluidStorageType.CREATIVE) {
                e.getRegistry().register(new FluidStoragePartItem(type));
            }

            e.getRegistry().register(new FluidStorageDiskItem(type));
        }

        e.getRegistry().register(new StorageHousingItem());

        for (UpgradeItem.Type type : UpgradeItem.Type.values()) {
            e.getRegistry().register(new UpgradeItem(type));
        }

        e.getRegistry().register(new WrenchItem());
        e.getRegistry().register(new PatternItem());
        e.getRegistry().register(new FilterItem());
        e.getRegistry().register(new PortableGridBlockItem(PortableGridBlockItem.Type.NORMAL));
        e.getRegistry().register(new PortableGridBlockItem(PortableGridBlockItem.Type.CREATIVE));

        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.QUARTZ_ENRICHED_IRON));
        e.getRegistry().register(new ControllerBlockItem(RSBlocks.CONTROLLER));
        e.getRegistry().register(new ControllerBlockItem(RSBlocks.CREATIVE_CONTROLLER));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.MACHINE_CASING));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.CABLE));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.DISK_DRIVE));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.GRID));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.CRAFTING_GRID));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.PATTERN_GRID));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.FLUID_GRID));

        e.getRegistry().register(new StorageBlockItem(RSBlocks.ONE_K_STORAGE_BLOCK));
        e.getRegistry().register(new StorageBlockItem(RSBlocks.FOUR_K_STORAGE_BLOCK));
        e.getRegistry().register(new StorageBlockItem(RSBlocks.SIXTEEN_K_STORAGE_BLOCK));
        e.getRegistry().register(new StorageBlockItem(RSBlocks.SIXTY_FOUR_K_STORAGE_BLOCK));
        e.getRegistry().register(new StorageBlockItem(RSBlocks.CREATIVE_STORAGE_BLOCK));

        e.getRegistry().register(new FluidStorageBlockItem(RSBlocks.SIXTY_FOUR_K_FLUID_STORAGE_BLOCK));
        e.getRegistry().register(new FluidStorageBlockItem(RSBlocks.TWO_HUNDRED_FIFTY_SIX_K_FLUID_STORAGE_BLOCK));
        e.getRegistry().register(new FluidStorageBlockItem(RSBlocks.THOUSAND_TWENTY_FOUR_K_FLUID_STORAGE_BLOCK));
        e.getRegistry().register(new FluidStorageBlockItem(RSBlocks.FOUR_THOUSAND_NINETY_SIX_K_FLUID_STORAGE_BLOCK));
        e.getRegistry().register(new FluidStorageBlockItem(RSBlocks.CREATIVE_FLUID_STORAGE_BLOCK));

        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.EXTERNAL_STORAGE));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.IMPORTER));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.EXPORTER));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.NETWORK_RECEIVER));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.NETWORK_TRANSMITTER));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.RELAY));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.DETECTOR));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.SECURITY_MANAGER));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.INTERFACE));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.FLUID_INTERFACE));
        e.getRegistry().register(new WirelessTransmitterBlockItem());
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.STORAGE_MONITOR));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.CONSTRUCTOR));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.DESTRUCTOR));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.DISK_MANIPULATOR));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.CRAFTER));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.CRAFTER_MANAGER));
        e.getRegistry().register(BlockUtils.createBlockItemFor(RSBlocks.CRAFTING_MONITOR));

        e.getRegistry().register(new WirelessGridItem(WirelessGridItem.Type.NORMAL));
        e.getRegistry().register(new WirelessGridItem(WirelessGridItem.Type.CREATIVE));
        e.getRegistry().register(new WirelessFluidGridItem(WirelessFluidGridItem.Type.NORMAL));
        e.getRegistry().register(new WirelessFluidGridItem(WirelessFluidGridItem.Type.CREATIVE));
        e.getRegistry().register(new WirelessCraftingMonitorItem(WirelessCraftingMonitorItem.Type.NORMAL));
        e.getRegistry().register(new WirelessCraftingMonitorItem(WirelessCraftingMonitorItem.Type.CREATIVE));
         */
    }
}
