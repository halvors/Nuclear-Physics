package org.halvors.quantum.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.utility.LanguageUtility;
import org.halvors.quantum.common.utility.NBTUtility;
import org.halvors.quantum.common.utility.position.Position;
import org.halvors.quantum.common.utility.type.Color;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemBlockThermometer extends ItemBlockSaved {
    public static final int energy = 1000;

    public ItemBlockThermometer(Block block) {
        super(block);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack itemStack, @Nonnull EntityPlayer player, @Nonnull List<String> list, boolean flag) {
        Position position = getSavedCoordinate(itemStack);

        if (position != null) {
            list.add(LanguageUtility.transelate("tooltip.trackingTemperature"));
            list.add("X: " + position.getX() + ", Y: " + position.getY() + ", Z: " + position.getZ());
            // TODO: Add client side temperature.
        } else {
            list.add(Color.DARK_RED + LanguageUtility.transelate("tooltip.notTrackingTemperature"));
        }

        super.addInformation(itemStack, player, list, flag);
    }

    public void setSavedCoordinate(ItemStack itemStack, Position position) {
        NBTTagCompound tagCompound = NBTUtility.getNBTTagCompound(itemStack);

        if (position != null) {
            tagCompound.setTag("trackCoordinate", position.writeToNBT(new NBTTagCompound()));
        } else {
            tagCompound.removeTag("trackCoordinate");
        }
    }

    public Position getSavedCoordinate(ItemStack itemStack) {
        NBTTagCompound tag = NBTUtility.getNBTTagCompound(itemStack);

        if (tag.hasKey("trackCoordinate")) {
            return new Position(tag.getCompoundTag("trackCoordinate"));
        }

        return null;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote) {
            setSavedCoordinate(itemStack, null);
            player.sendMessage(new TextComponentString("Cleared tracking coordinate."));

            return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
        }

        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (player.isSneaking()) {
            setSavedCoordinate(itemStack, new Position(pos));
            player.sendMessage(new TextComponentString("Tracking coordinate: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()));

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }
}
