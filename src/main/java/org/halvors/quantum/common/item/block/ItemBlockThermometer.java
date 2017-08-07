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
import org.halvors.quantum.common.utility.transform.vector.Vector3;
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
        super.addInformation(itemStack, player, list, flag);

        Vector3 coord = getSavedCoord(itemStack);

        if (coord != null) {
            list.add(LanguageUtility.transelate("tooltip.trackingTemperature"));
            list.add("X: " + coord.intX() + ", Y: " + coord.intY() + ", Z: " + coord.intZ());
            // TODO: Add client side temperature.
        } else {
            list.add(Color.DARK_RED + LanguageUtility.transelate("tooltip.notTrackingTemperature"));
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
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack itemStack = player.getHeldItemMainhand();

        if (!world.isRemote) {
            setSavedCoords(itemStack, null);
            player.sendMessage(new TextComponentString("Cleared tracking coordinate."));

            return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
        }

        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (player.isSneaking()) {
            ItemStack itemStack = player.getHeldItemMainhand();
            setSavedCoords(itemStack, new Vector3(pos.getX(), pos.getY(), pos.getZ()));
            player.sendMessage(new TextComponentString("Tracking coordinate: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()));

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }
}
