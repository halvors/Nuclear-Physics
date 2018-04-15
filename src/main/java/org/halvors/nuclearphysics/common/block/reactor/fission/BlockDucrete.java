package org.halvors.nuclearphysics.common.block.reactor.fission;

import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.api.block.IAntiRadiationBlock;
import org.halvors.nuclearphysics.common.block.BlockBase;

public class BlockDucrete extends BlockBase implements IAntiRadiationBlock {
    public BlockDucrete() {
        super("ducrete", Material.GRASS);

        setHardness(10);
    }

    @Override
    public boolean isPoisonPrevention(IBlockAccess world, int x, int y, int z, String name) {
        return true;
    }
}
