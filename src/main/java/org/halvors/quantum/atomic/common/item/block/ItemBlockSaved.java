package org.halvors.quantum.atomic.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.halvors.quantum.atomic.common.utility.InventoryUtility;
import org.halvors.quantum.atomic.common.utility.NBTUtility;
import org.halvors.quantum.atomic.common.utility.transform.vector.Vector3;

/**
 * An item that can store a block's tile data.
 */
public class ItemBlockSaved extends ItemBlockTooltip {
    public ItemBlockSaved(Block block) {
        super(block);

        setMaxDamage(0);
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

    @Override
    public boolean placeBlockAt(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile != null) {
            // Inject essential tile data.
            NBTTagCompound essentialNBT = new NBTTagCompound();
            tile.writeToNBT(essentialNBT);

            NBTTagCompound setNbt = NBTUtility.getNBTTagCompound(itemStack);

            if (essentialNBT.hasKey("id")) {
                setNbt.setString("id", essentialNBT.getString("id"));
                setNbt.setInteger("x", essentialNBT.getInteger("x"));
                setNbt.setInteger("y", essentialNBT.getInteger("y"));
                setNbt.setInteger("z", essentialNBT.getInteger("z"));
            }

            tile.readFromNBT(setNbt);
        }

        return super.placeBlockAt(itemStack, player, world, pos, side, hitX, hitY, hitZ, state);
    }

    public static ItemStack getItemStackWithNBT(World world, BlockPos pos) {
        return getItemStackWithNBT(world.getBlockState(pos), world, pos);
    }

    public static ItemStack getItemStackWithNBT(IBlockState state, World world, BlockPos pos) {
        if (state != null) {
            ItemStack dropStack = new ItemStack(state.getBlock(), state.getBlock().quantityDropped(state, 0, world.rand), state.getBlock().damageDropped(state));
            NBTTagCompound tag = new NBTTagCompound();

            TileEntity tile = world.getTileEntity(pos);

            if (tile != null) {
                tile.writeToNBT(tag);
            }

            tag.removeTag("id");
            tag.removeTag("x");
            tag.removeTag("y");
            tag.removeTag("z");

            dropStack.setTagCompound(tag);

            return dropStack;
        }

        return null;
    }

    public static void dropBlockWithNBT(IBlockState state, World world, BlockPos pos) {
        if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops")) {
            ItemStack itemStack = getItemStackWithNBT(state, world, pos);

            if (itemStack != null) {
                InventoryUtility.dropItemStack(world, new Vector3(pos.getX(), pos.getY(), pos.getZ()), itemStack);
            }
        }
    }
}

