package org.halvors.quantum.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.halvors.quantum.common.Reference;

import java.util.List;

public class ItemAntimatter extends ItemCell {
    @SideOnly(Side.CLIENT)
    private IIcon iconGram;

    public ItemAntimatter() {
        super("antimatter");

        setMaxDurability(0);
        setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(Reference.PREFIX + "antimatter_milligram");
        iconGram = iconRegister.registerIcon(Reference.PREFIX + "antimatter_gram");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int metadata) {
        return metadata >= 1 ? iconGram : itemIcon;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return 160;
    }
}