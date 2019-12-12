package org.halvors.nuclearphysics.common.item.group;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.NuclearPhysics;
import org.halvors.nuclearphysics.common.init.ModBlocks;

public class ItemGroupMain extends ItemGroup {
    public ItemGroupMain() {
        super(NuclearPhysics.ID);
    }

    @Override
    public ItemStack createIcon() {
        //return new ItemStack(RSBlocks.CREATIVE_CONTROLLER);
        return new ItemStack(ModBlocks.blockReactorCell);
    }
}