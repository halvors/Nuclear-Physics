package org.halvors.quantum.common.block.machine;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.common.tile.machine.TileQuantumAssembler;

public class BlockQuantumAssembler extends BlockMachine {
    public BlockQuantumAssembler() {
        super("quantum_assembler");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileQuantumAssembler();
    }
}
