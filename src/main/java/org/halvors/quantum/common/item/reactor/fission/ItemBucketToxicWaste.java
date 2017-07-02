package org.halvors.quantum.common.item.reactor.fission;

import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.QuantumBlocks;
import org.halvors.quantum.common.Reference;

public class ItemBucketToxicWaste extends ItemBucket {
    public ItemBucketToxicWaste() {
        super(QuantumBlocks.blockToxicWaste);

        setUnlocalizedName("bucketToxicWaste");
        setRegistryName(Reference.ID, "bucketToxicWaste");
        //setTextureName(Reference.PREFIX + "bucketToxicWaste");
        setContainerItem(Items.BUCKET);
        setCreativeTab(Quantum.getCreativeTab());
    }
}
