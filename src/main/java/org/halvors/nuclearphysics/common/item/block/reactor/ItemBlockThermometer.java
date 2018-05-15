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
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.item.block.ItemBlockTooltip;
import org.halvors.nuclearphysics.common.type.EnumColor;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.VectorUtility;

import java.util.List;

public class ItemBlockThermometer extends ItemBlockTooltip {
    private static final String NBT_TRACK_COORDINATE = "trackCoordinate";

    public static final int energy = 1000;

    public ItemBlockThermometer(final Block block) {
        super(block);

        setMaxStackSize(1);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemStack, final EntityPlayer player, final List list, final boolean flag) {
        final BlockPos pos = getSavedCoordinate(itemStack);

        if (pos != null) {
            list.add(LanguageUtility.transelate("tooltip.trackingCoordinate") + ": ");
            list.add(EnumColor.DARK_GREEN + "X: " + pos.getX() + ", Y: " + pos.getY() + ", Z: " + pos.getZ());
        } else {
            list.add(EnumColor.DARK_RED + LanguageUtility.transelate("tooltip.notTrackingTemperature"));
        }

        super.addInformation(itemStack, player, list, flag);
    }

    @Override
    public boolean placeBlockAt(final ItemStack stack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ, final int metadata) {
        final BlockPos pos = new BlockPos(x, y, z);
        final TileEntity tile = pos.getTileEntity(world);
        final ItemStack itemStack = player.getHeldItem();

        if (!world.isRemote && tile != null) {
            // Inject essential tile data.
            final NBTTagCompound essentialNBT = new NBTTagCompound();
            tile.writeToNBT(essentialNBT);

            final NBTTagCompound setNbt = InventoryUtility.getNBTTagCompound(itemStack);

            if (essentialNBT.hasKey(NBT_TRACK_COORDINATE)) {
                setNbt.setTag(NBT_TRACK_COORDINATE, essentialNBT.getCompoundTag(NBT_TRACK_COORDINATE));
            }

            tile.readFromNBT(setNbt);
        }

        return super.placeBlockAt(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
    }

    @Override
    public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer player) {
        if (!world.isRemote) {
            setSavedCoordinate(itemStack, null);
            player.addChatMessage(new ChatComponentText(EnumColor.DARK_BLUE + "[" + Reference.NAME + "] " + EnumColor.GREY + LanguageUtility.transelate("tooltip.clearedTrackingCoordinate") + "."));

            return itemStack;
        }

        return super.onItemRightClick(itemStack, world, player);
    }

    @Override
    public boolean doesSneakBypassUse(final World world, final int x, final int y, final int z, final EntityPlayer player) {
        return true;
    }

    @Override
    public boolean onItemUse(final ItemStack itemStack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int facing, final float hitX, final float hitY, final float hitZ) {
        final BlockPos pos = new BlockPos(x, y, z);

        if (player.isSneaking()) {
            if (!world.isRemote) {
                setSavedCoordinate(itemStack, pos);

                player.addChatMessage(new ChatComponentText(EnumColor.DARK_BLUE + "[" + Reference.NAME + "] " + EnumColor.GREY + LanguageUtility.transelate("tooltip.trackingCoordinate") + ": " + x + ", " + y + ", " + z));
            }

            return true;
        }

        return super.onItemUse(itemStack, player, world, x, y, z, facing, hitX, hitY, hitZ);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public BlockPos getSavedCoordinate(final ItemStack itemStack) {
        final NBTTagCompound tag = InventoryUtility.getNBTTagCompound(itemStack);

        if (tag.hasKey(NBT_TRACK_COORDINATE)) {
            return VectorUtility.readFromNBT(tag.getCompoundTag(NBT_TRACK_COORDINATE));
        }

        return null;
    }

    public void setSavedCoordinate(final ItemStack itemStack, final BlockPos pos) {
        final NBTTagCompound tag = InventoryUtility.getNBTTagCompound(itemStack);

        if (pos != null) {
            tag.setTag(NBT_TRACK_COORDINATE, VectorUtility.writeToNBT(pos, new NBTTagCompound()));
        } else {
            tag.removeTag(NBT_TRACK_COORDINATE);
        }
    }
}
