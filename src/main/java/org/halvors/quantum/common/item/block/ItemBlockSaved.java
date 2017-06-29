package org.halvors.quantum.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.common.utility.transform.vector.Vector3;
import org.halvors.quantum.common.utility.InventoryUtility;
import org.halvors.quantum.common.utility.NBTUtility;

/**
 * An item that can store a block's tile data.
 */
public class ItemBlockSaved extends ItemBlockTooltip {
    public ItemBlockSaved(Block block) {
        super(block);

        setMaxDurability(0);
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null) {
            // Inject essential tile data.
            NBTTagCompound essentialNBT = new NBTTagCompound();
            tile.writeToNBT(essentialNBT);

            NBTTagCompound setNbt = NBTUtility.getNBTTagCompound(stack);

            if (essentialNBT.hasKey("id")) {
                setNbt.setString("id", essentialNBT.getString("id"));
                setNbt.setInteger("x", essentialNBT.getInteger("x"));
                setNbt.setInteger("y", essentialNBT.getInteger("y"));
                setNbt.setInteger("z", essentialNBT.getInteger("z"));
            }

            tile.readFromNBT(setNbt);
        }

        return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
    }

    public static ItemStack getItemStackWithNBT(World world, int x, int y, int z) {
        return getItemStackWithNBT(world.getBlock(x, y, z), world, x, y, z);
    }

    public static ItemStack getItemStackWithNBT(Block block, World world, int x, int y, int z) {
        if (block != null) {
            int metadata = world.getBlockMetadata(x, y, z);

            ItemStack dropStack = new ItemStack(block, block.quantityDropped(metadata, 0, world.rand), block.damageDropped(metadata));
            NBTTagCompound tag = new NBTTagCompound();

            TileEntity tile = world.getTileEntity(x, y, z);

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

    public static void dropBlockWithNBT(Block block, World world, int x, int y, int z) {
        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
            ItemStack itemStack = getItemStackWithNBT(block, world, x, y, z);

            if (itemStack != null) {
                InventoryUtility.dropItemStack(world, new Vector3(x, y, z), itemStack);
            }
        }
    }
}

