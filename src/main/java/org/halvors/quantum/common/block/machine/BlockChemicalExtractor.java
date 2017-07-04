package org.halvors.quantum.common.block.machine;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;

import javax.annotation.Nonnull;

public class BlockChemicalExtractor extends BlockMachine {
    public BlockChemicalExtractor() {
        super("chemical_extractor");
    }

    @Override
    @Nonnull
    public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
        return new TileChemicalExtractor();
    }
}
