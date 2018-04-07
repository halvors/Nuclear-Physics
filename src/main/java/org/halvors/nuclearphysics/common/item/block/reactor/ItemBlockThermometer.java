package org.halvors.nuclearphysics.common.item.block.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.item.block.ItemBlockTooltip;
import org.halvors.nuclearphysics.common.type.Color;
import org.halvors.nuclearphysics.common.type.Position;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.List;

public class ItemBlockThermometer extends ItemBlockTooltip {
    public static final int energy = 1000;

    public ItemBlockThermometer(Block block) {
        super(block);

        setMaxStackSize(1);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        Position position = getSavedCoordinate(itemStack);

        if (position != null) {
            list.add(LanguageUtility.transelate("tooltip.trackingCoordinate") + ": ");
            list.add(Color.DARK_GREEN + "X: " + position.getIntX() + ", Y: " + position.getIntY() + ", Z: " + position.getIntZ());
        } else {
            list.add(Color.DARK_RED + LanguageUtility.transelate("tooltip.notTrackingTemperature"));
        }

        super.addInformation(itemStack, player, list, flag);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        final TileEntity tile = world.getTileEntity(x, y, z);
        final ItemStack itemStack = player.getHeldItem();

        if (!world.isRemote && tile != null) {
            // Inject essential tile data.
            NBTTagCompound essentialNBT = new NBTTagCompound();
            tile.writeToNBT(essentialNBT);

            NBTTagCompound setNbt = InventoryUtility.getNBTTagCompound(itemStack);

            if (essentialNBT.hasKey("trackCoordinate")) {
                setNbt.setTag("trackCoordinate", essentialNBT.getCompoundTag("trackCoordinate"));
            }

            tile.readFromNBT(setNbt);
        }

        return super.placeBlockAt(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            setSavedCoordinate(itemStack, null);
            player.addChatMessage(new ChatComponentText(Color.DARK_BLUE + "[" + Reference.NAME + "] " + Color.GREY + LanguageUtility.transelate("tooltip.clearedTrackingCoordinate") + "."));


            return itemStack;
        }

        return super.onItemRightClick(itemStack, world, player);
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        return true;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int facing, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            if (!world.isRemote) {
                setSavedCoordinate(itemStack, new Position(x, y, z));

                player.addChatMessage(new ChatComponentText(Color.DARK_BLUE + "[" + Reference.NAME + "] " + Color.GREY + LanguageUtility.transelate("tooltip.trackingCoordinate") + ": " + x + ", " + y + ", " + z));
            }

            return true;
        }

        return super.onItemUse(itemStack, player, world, x, y, z, facing, hitX, hitY, hitZ);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Position getSavedCoordinate(ItemStack itemStack) {
        NBTTagCompound tag = InventoryUtility.getNBTTagCompound(itemStack);

        if (tag.hasKey("trackCoordinate")) {
            return new Position(tag.getCompoundTag("trackCoordinate"));
        }

        return null;
    }

    public void setSavedCoordinate(ItemStack itemStack, Position position) {
        NBTTagCompound tag = InventoryUtility.getNBTTagCompound(itemStack);

        if (position != null) {
            tag.setTag("trackCoordinate", position.writeToNBT(new NBTTagCompound()));
        } else {
            tag.removeTag("trackCoordinate");
        }
    }
}
