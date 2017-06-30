package org.halvors.quantum.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.utility.LanguageUtility;
import org.halvors.quantum.common.utility.NBTUtility;
import org.halvors.quantum.common.utility.transform.vector.Vector3;
import org.halvors.quantum.common.utility.type.Color;

import java.util.List;

public class ItemBlockThermometer extends ItemBlockSaved {
    public static final int energy = 1000;

    public ItemBlockThermometer(Block block) {
        super(block);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(itemStack, player, list, flag);

        Vector3 coord = getSavedCoord(itemStack);

        if (coord != null) {
            list.add(LanguageUtility.localize("tooltip.trackingTemperature"));
            list.add("X: " + coord.intX() + ", Y: " + coord.intY() + ", Z: " + coord.intZ());
            // TODO: Add client side temperature.
        } else {
            list.add(Color.DARK_RED + LanguageUtility.localize("tooltip.notTrackingTemperature"));
        }
    }

    public void setSavedCoords(ItemStack itemStack, Vector3 position) {
        NBTTagCompound tagCompound = NBTUtility.getNBTTagCompound(itemStack);

        if (position != null) {
            tagCompound.setTag("trackCoordinate", position.writeToNBT(new NBTTagCompound()));
        } else {
            tagCompound.removeTag("trackCoordinate");
        }
    }

    public Vector3 getSavedCoord(ItemStack itemStack) {
        NBTTagCompound tagCompound = NBTUtility.getNBTTagCompound(itemStack);

        if (tagCompound.hasKey("trackCoordinate")) {
            return new Vector3(tagCompound.getCompoundTag("trackCoordinate"));
        }

        return null;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            setSavedCoords(itemStack, null);
            player.sendMessage(new TextComponentString("Cleared tracking coordinate."));
        }

        return itemStack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            setSavedCoords(itemStack, new Vector3(x, y, z));
            player.sendMessage(new TextComponentString("Tracking coordinate: " + x + ", " + y + ", " + z));

            return true;
        }

        return super.onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }
}
