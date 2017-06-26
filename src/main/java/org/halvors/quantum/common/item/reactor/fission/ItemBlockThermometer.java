package org.halvors.quantum.common.item.reactor.fission;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.common.utility.LanguageUtility;
import org.halvors.quantum.lib.prefab.item.ItemBlockSaved;

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
            list.add("\uaa74" + LanguageUtility.localize("tooltip.trackingTemperature"));
            list.add("X: " + coord.intX() + ", Y: " + coord.intY() + ", Z: " + coord.intZ());
            // TODO: Add client side temperature.
        } else {
            list.add("\u00a74" + LanguageUtility.localize("tooltip.notTrackingTemperature"));
        }
    }

    public void setSavedCoords(ItemStack itemStack, Vector3 position) {
        NBTTagCompound nbt = itemStack.getTagCompound();

        if (position != null) {
            nbt.setTag("trackCoordinate", position.writeToNBT(new NBTTagCompound()));
        } else {
            nbt.removeTag("trackCoordinate");
        }
    }

    public Vector3 getSavedCoord(ItemStack itemStack) {
        NBTTagCompound nbt = itemStack.getTagCompound();

        if (nbt.hasKey("trackCoordinate")) {
            return new Vector3(nbt.getCompoundTag("trackCoordinate"));
        }

        return null;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        setSavedCoords(itemStack, null);

        if (!world.isRemote) {
            player.addChatMessage(new ChatComponentText("Cleared tracking coordinate."));
        }

        return itemStack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            if (!world.isRemote) {
                setSavedCoords(itemStack, new Vector3(x, y, z));
                player.addChatMessage(new ChatComponentText("Tracking coordinate: " + x + ", " + y + ", " + z));
            }

            return true;
        }

        return super.onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }
}
