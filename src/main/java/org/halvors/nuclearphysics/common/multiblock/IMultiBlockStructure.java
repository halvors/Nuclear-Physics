package org.halvors.nuclearphysics.common.multiblock;

import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.api.BlockPos;

public interface IMultiBlockStructure<W extends IMultiBlockStructure> extends IMultiBlock {
    IBlockAccess getWorldObject();

    void onMultiBlockChanged();

    BlockPos getPosition();

    MultiBlockHandler<W> getMultiBlock();
}
