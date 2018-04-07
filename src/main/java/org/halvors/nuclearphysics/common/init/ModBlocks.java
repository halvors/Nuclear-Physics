package org.halvors.nuclearphysics.common.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.block.BlockRadioactiveGrass;
import org.halvors.nuclearphysics.common.block.BlockUraniumOre;
import org.halvors.nuclearphysics.common.block.debug.BlockCreativeBuilder;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.block.particle.BlockFulminationGenerator;
import org.halvors.nuclearphysics.common.block.reactor.*;
import org.halvors.nuclearphysics.common.block.reactor.fission.BlockControlRod;
import org.halvors.nuclearphysics.common.block.reactor.fusion.BlockElectromagnet;
import org.halvors.nuclearphysics.common.item.block.ItemBlockMetadata;
import org.halvors.nuclearphysics.common.item.block.ItemBlockTooltip;
import org.halvors.nuclearphysics.common.item.block.reactor.ItemBlockThermometer;
import org.halvors.nuclearphysics.common.tile.particle.TileFulminationGenerator;
import org.halvors.nuclearphysics.common.tile.reactor.*;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TileElectromagnet;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TilePlasma;

import java.util.HashSet;
import java.util.Set;

public class ModBlocks {
    public static final Set<ItemBlock> itemBlocks = new HashSet<>();

    public static Block blockControlRod = new BlockControlRod();
    public static Block blockElectricTurbine = new BlockElectricTurbine();
    public static Block blockElectromagnet = new BlockElectromagnet();
    public static Block blockFulmination = new BlockFulminationGenerator();
    public static Block blockGasFunnel = new BlockGasFunnel();
    public static Block blockMachine = new BlockMachine();
    public static Block blockSiren = new BlockSiren();
    public static Block blockThermometer = new BlockThermometer();
    public static Block blockUraniumOre = new BlockUraniumOre();
    public static Block blockRadioactiveGrass = new BlockRadioactiveGrass();
    public static Block blockReactorCell = new BlockReactorCell();
    public static Block blockCreativeBuilder = new BlockCreativeBuilder();

    public static void registerBlocks() {
        GameRegistry.registerBlock(blockControlRod, ItemBlockTooltip.class, "blockControlRod");
        GameRegistry.registerBlock(blockElectricTurbine, ItemBlockTooltip.class, "blockElectricTurbine");
        GameRegistry.registerBlock(blockElectromagnet, ItemBlockMetadata.class, "blockElectromagnet,");
        GameRegistry.registerBlock(blockFulmination, ItemBlockTooltip.class, "blockFulmination");
        GameRegistry.registerBlock(blockGasFunnel, ItemBlockTooltip.class, "blockGasFunnel");
        GameRegistry.registerBlock(blockMachine, ItemBlockMetadata.class, "blockMachine");
        GameRegistry.registerBlock(blockSiren, ItemBlockTooltip.class, "blockSiren");
        GameRegistry.registerBlock(blockThermometer, ItemBlockThermometer.class, "blockThermometer");
        GameRegistry.registerBlock(blockUraniumOre, ItemBlockTooltip.class, "blockUraniumOre");
        GameRegistry.registerBlock(blockRadioactiveGrass, ItemBlockTooltip.class, "blockRadioactiveGrass");
        GameRegistry.registerBlock(blockReactorCell, ItemBlockTooltip.class, "blockReactorCell");
        GameRegistry.registerBlock(blockCreativeBuilder, ItemBlockTooltip.class, "blockCreativeBuilder");

        OreDictionary.registerOre("oreUranium", blockUraniumOre);
        OreDictionary.registerOre("blockRadioactiveGrass", blockRadioactiveGrass);
    }

    private static void registerTileEntities() {
        for (EnumMachine type : EnumMachine.values()) {
            registerTile(type.getTileClass());
        }

        registerTile(TileElectricTurbine.class);
        registerTile(TileElectromagnet.class);
        registerTile(TileFulminationGenerator.class);
        registerTile(TileGasFunnel.class);
        registerTile(TileSiren.class);
        registerTile(TileThermometer.class);
        registerTile(TilePlasma.class);
        registerTile(TileReactorCell.class);
    }

    private static void registerTile(Class<? extends TileEntity> tileClass) {
        String name = tileClass.getSimpleName().replaceAll("(.)(\\p{Lu})", "$1_$2").toLowerCase();

        GameRegistry.registerTileEntity(tileClass, Reference.PREFIX + name);
    }
}
