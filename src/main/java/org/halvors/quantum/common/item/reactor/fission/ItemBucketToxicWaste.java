package org.halvors.quantum.common.item.reactor.fission;

import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import org.halvors.quantum.Quantum;

public class ItemBucketToxicWaste extends ItemBucket {
    public ItemBucketToxicWaste() {
        super(Quantum.blockToxicWaste);

        setUnlocalizedName("bucketToxicWaste");
        //setTextureName(Reference.PREFIX + "bucketToxicWaste");
        setContainerItem(Items.BUCKET);
        setCreativeTab(Quantum.getCreativeTab());
    }
}
