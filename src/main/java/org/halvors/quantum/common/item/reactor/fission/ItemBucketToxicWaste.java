package org.halvors.quantum.common.item.reactor.fission;

import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.QuantumFluids;
import org.halvors.quantum.common.Reference;

public class ItemBucketToxicWaste extends ItemBucket {
    public ItemBucketToxicWaste() {
        super(QuantumFluids.fluidToxicWaste.getBlock());

        setUnlocalizedName("bucketToxicWaste");
        setRegistryName(Reference.ID, "bucketToxicWaste");
        setContainerItem(Items.BUCKET);
        setCreativeTab(Quantum.getCreativeTab());
    }
}