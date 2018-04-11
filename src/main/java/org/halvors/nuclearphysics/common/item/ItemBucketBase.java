package org.halvors.nuclearphysics.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraftforge.fluids.Fluid;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;

public class ItemBucketBase extends ItemBucket {
	protected final String name;

	public ItemBucketBase(String name, Fluid fluid) {
		super(fluid.getBlock());

		this.name = name;

		setUnlocalizedName(Reference.ID + "." + name);
		setCreativeTab(NuclearPhysics.getCreativeTab());
		setContainerItem(Items.bucket);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Reference.PREFIX + name);
	}
}
