package org.halvors.nuclearphysics.common.item.block.reactor;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.item.block.ItemBlockTooltip;
import org.halvors.nuclearphysics.common.type.EnumColor;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.VectorUtility;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemBlockThermometer extends ItemBlockTooltip {
    private static final String NBT_TRACK_COORDINATE = "trackCoordinate";

    public static final int energy = 1000;

    public ItemBlockThermometer(Block block) {
        super(block);

        setMaxStackSize(1);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack itemStack, @Nonnull EntityPlayer player, @Nonnull List<String> list, boolean flag) {
        BlockPos pos = getSavedCoordinate(itemStack);

        if (pos != null) {
            list.add(LanguageUtility.transelate("tooltip.trackingCoordinate") + ": ");
            list.add(EnumColor.DARK_GREEN + "X: " + pos.getX() + ", Y: " + pos.getY() + ", Z: " + pos.getZ());
        } else {
            list.add(EnumColor.DARK_RED + LanguageUtility.transelate("tooltip.notTrackingTemperature"));
        }

        super.addInformation(itemStack, player, list, flag);
    }

    @Override
    public boolean placeBlockAt(@Nonnull ItemStack itemStack, @Nonnull EntityPlayer player, World world, @Nonnull BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);

        if (!world.isRemote && tile != null) {
            // Inject essential tile data.
            NBTTagCompound essentialNBT = new NBTTagCompound();
            tile.writeToNBT(essentialNBT);

            NBTTagCompound setNbt = InventoryUtility.getNBTTagCompound(itemStack);

            if (essentialNBT.hasKey(NBT_TRACK_COORDINATE)) {
                setNbt.setTag(NBT_TRACK_COORDINATE, essentialNBT.getCompoundTag(NBT_TRACK_COORDINATE));
            }

            tile.readFromNBT(setNbt);
        }

        return super.placeBlockAt(itemStack, player, world, pos, side, hitX, hitY, hitZ, state);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote) {
            setSavedCoordinate(itemStack, null);
            player.sendMessage(new TextComponentString(EnumColor.DARK_BLUE + "[" + Reference.NAME + "] " + EnumColor.GREY + LanguageUtility.transelate("tooltip.clearedTrackingCoordinate") + "."));

            return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
        }

        return super.onItemRightClick(itemStack, world, player, hand);
    }

    /*
     * This is a workaround for buggy onItemUseFirst() function in 1.10.
     * TODO: Review this for 1.11 and 1.12.
     */
    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(ItemStack itemStack, @Nonnull EntityPlayer player, World world, @Nonnull BlockPos pos, EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            if (!world.isRemote) {
                setSavedCoordinate(itemStack, pos);

                player.sendMessage(new TextComponentString(EnumColor.DARK_BLUE + "[" + Reference.NAME + "] " + EnumColor.GREY + LanguageUtility.transelate("tooltip.trackingCoordinate") + ": " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()));
            }

            return EnumActionResult.SUCCESS;
        }

        return super.onItemUse(itemStack, player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public BlockPos getSavedCoordinate(ItemStack itemStack) {
        NBTTagCompound tag = InventoryUtility.getNBTTagCompound(itemStack);

        if (tag.hasKey(NBT_TRACK_COORDINATE)) {
            return VectorUtility.readFromNBT(tag.getCompoundTag(NBT_TRACK_COORDINATE));
        }

        return null;
    }

    public void setSavedCoordinate(ItemStack itemStack, BlockPos pos) {
        NBTTagCompound tag = InventoryUtility.getNBTTagCompound(itemStack);

        if (pos != null) {
            tag.setTag(NBT_TRACK_COORDINATE, VectorUtility.writeToNBT(pos, new NBTTagCompound()));
        } else {
            tag.removeTag(NBT_TRACK_COORDINATE);
        }
    }
}
