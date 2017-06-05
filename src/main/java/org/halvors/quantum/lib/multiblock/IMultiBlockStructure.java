package org.halvors.quantum.lib.multiblock;

import net.minecraft.world.World;
import org.halvors.quantum.common.transform.vector.Vector3;

public interface IMultiBlockStructure<W extends IMultiBlockStructure> extends IMultiBlock {
    World getWorld();

    void onMultiBlockChanged();

    Vector3 getPosition();

    MultiBlockHandler<W> getMultiBlock();
}
