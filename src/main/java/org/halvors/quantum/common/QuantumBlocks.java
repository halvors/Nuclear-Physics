package org.halvors.quantum.common;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.halvors.quantum.common.block.*;
import org.halvors.quantum.common.block.debug.BlockCreativeBuilder;
import org.halvors.quantum.common.block.machine.*;
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
import org.halvors.quantum.common.item.block.ItemBlockThermometer;

public class QuantumBlocks {
    public static Block blockAccelerator = new BlockAccelerator();
    public static Block blockChemicalExtractor = new BlockChemicalExtractor();
    public static Block blockControlRod = new BlockControlRod();
    public static Block blockElectricTurbine = new BlockElectricTurbine();
    public static Block blockElectromagnet = new BlockElectromagnet();
    public static Block blockFulmination = new BlockFulmination();
    public static Block blockGasCentrifuge = new BlockGasCentrifuge();
    public static Block blockGasFunnel = new BlockGasFunnel();
    public static Block blockNuclearBoiler = new BlockNuclearBoiler();
    public static Block blockSiren = new BlockSiren();
    public static Block blockThermometer = new BlockThermometer();
    public static Block blockUraniumOre = new BlockUraniumOre();
    public static Block blockPlasma = new BlockPlasma();
    //fluidPlasma.setBlock(blockPlasma);
    public static Block blockPlasmaHeater = new BlockPlasmaHeater();
    //public static Block blockQuantumAssembler = new BlockQuantumAssembler();
    public static Block blockRadioactiveGrass = new BlockRadioactiveGrass();
    public static Block blockReactorCell = new BlockReactorCell();
    public static BlockFluidClassic blockToxicWaste = new BlockToxicWaste();

    public static Block blockCreativeBuilder = new BlockCreativeBuilder();

    public static void register() {
        // Register blocks.
        register(blockAccelerator);
        register(blockChemicalExtractor);
        register(blockControlRod);
        //register(blockElectricTurbine);
        //register(blockElectromagnet, new ItemBlockMetadata(blockElectromagnet));
        register(blockFulmination);
        register(blockGasCentrifuge);
        register(blockGasFunnel);
        register(blockNuclearBoiler);
        register(blockSiren);
        register(blockThermometer, new ItemBlockThermometer(blockThermometer));
        register(blockUraniumOre);
        register(blockPlasma);
        register(blockPlasmaHeater);
        //register(blockQuantumAssembler);
        register(blockRadioactiveGrass);
        //register(blockReactorCell);
        //register(blockToxicWaste);

        register(blockCreativeBuilder);
    }

    private static <T extends Block> T register(T block, ItemBlock itemBlock) {
        GameRegistry.register(block);
        GameRegistry.register(itemBlock);

        if (block instanceof BlockQuantum) {
            ((BlockQuantum) block).registerItemModel(itemBlock);
        } else if (block instanceof BlockContainerQuantum) {
            ((BlockContainerQuantum) block).registerItemModel(itemBlock);
        }

        return block;
    }

    private static <T extends Block> T register(T block) {
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());

        return register(block, itemBlock);
    }
}
