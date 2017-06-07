package universalelectricity.api.item;

import net.minecraft.item.ItemStack;

public interface IEnergyItem {
    long recharge(ItemStack paramItemStack, long paramLong, boolean paramBoolean);

    long discharge(ItemStack paramItemStack, long paramLong, boolean paramBoolean);

    long getEnergy(ItemStack paramItemStack);

    long getEnergyCapacity(ItemStack paramItemStack);

    void setEnergy(ItemStack paramItemStack, long paramLong);
}
