package org.halvors.nuclearphysics.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;

public class BlockBase extends Block {
    protected String name;

    public BlockBase(String name, Material material) {
        super(material);

        this.name = name;

        setUnlocalizedName(Reference.ID + "." + name);
        setTextureName(Reference.PREFIX + name);
        setCreativeTab(NuclearPhysics.getCreativeTab());
    }
}
