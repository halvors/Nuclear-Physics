package org.halvors.nuclearphysics.common.multiblock;

import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.utility.location.Position;

public interface IMultiBlockStructure<W extends IMultiBlockStructure> extends IMultiBlock {
    World getWorldObject();

    void onMultiBlockChanged();

    Position getPosition();

    MultiBlockHandler<W> getMultiBlock();
}
