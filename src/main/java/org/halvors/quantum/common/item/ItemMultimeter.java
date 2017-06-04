package org.halvors.quantum.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import org.halvors.quantum.common.tile.TileEntity;
import org.halvors.quantum.common.utility.render.Color;

public class ItemMultimeter extends ItemTextured {
	public ItemMultimeter() {
		super("Multimeter");
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntity tileEntity = TileEntity.getTileEntity(world, x, y, z);

			if (tileEntity != null) {
				player.addChatMessage(new ChatComponentText(Color.RED + "Not implemented yet."));
			}
		}

		return false;
	}
}
