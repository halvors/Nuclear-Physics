package org.halvors.quantum.common.item.block;

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
import org.halvors.quantum.common.utility.InventoryUtility;
import org.halvors.quantum.common.utility.LanguageUtility;
import org.halvors.quantum.common.utility.position.Position;
import org.halvors.quantum.common.utility.type.Color;

import javax.annotation.Nonnull;
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
    public void addInformation(@Nonnull ItemStack itemStack, @Nonnull EntityPlayer player, @Nonnull List<String> list, boolean flag) {
        Position position = getSavedCoordinate(itemStack);

        if (position != null) {
            list.add(LanguageUtility.transelate("tooltip.trackingTemperature"));
            list.add(Color.DARK_GREEN + "X: " + position.getIntX() + ", Y: " + position.getIntY() + ", Z: " + position.getIntZ());
        } else {
            list.add(Color.DARK_RED + LanguageUtility.transelate("tooltip.notTrackingTemperature"));
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

            if (essentialNBT.hasKey("trackCoordinate")) {
                setNbt.setTag("trackCoordinate", essentialNBT.getCompoundTag("trackCoordinate"));
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
            player.sendMessage(new TextComponentString("Cleared tracking coordinate."));

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
                setSavedCoordinate(itemStack, new Position(pos));
                player.sendMessage(new TextComponentString("Tracking coordinate: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()));
            }

            return EnumActionResult.SUCCESS;
        }

        return super.onItemUse(itemStack, player, world, pos, hand, facing, hitX, hitY, hitZ);
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
