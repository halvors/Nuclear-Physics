package org.halvors.nuclearphysics.common.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IMultiBlockStructure<W extends IMultiBlockStructure> extends IMultiBlock {
    IBlockAccess getWorldObject();

    void onMultiBlockChanged();

    BlockPos getPosition();

    MultiBlockHandler<W> getMultiBlock();
}
