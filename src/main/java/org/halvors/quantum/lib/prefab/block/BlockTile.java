package org.halvors.quantum.lib.prefab.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BlockTile extends BlockAdvanced implements ITileEntityProvider {
    public BlockTile(Material material) {
        super(material);

        this.isBlockContainer = true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        dropEntireInventory(world, x, y, z, block, metadata);

        super.breakBlock(world, x, y, z, block, metadata);

        world.removeTileEntity(x, y, z);
    }

    @Override
    public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
        super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
        TileEntity tileentity = par1World.getTileEntity(par2, par3, par4);

        return tileentity != null && tileentity.receiveClientEvent(par5, par6);
    }

    public void dropEntireInventory(World world, int x, int y, int z, Block block, int par6) {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null) {
            if (tile instanceof IInventory) {
                IInventory inventory = (IInventory) tile;

                for (int i = 0; i < inventory.getSizeInventory(); i++) {
                    ItemStack itemStack = inventory.getStackInSlot(i);

                    if (itemStack != null) {
                        Random random = new Random();
                        float var8 = random.nextFloat() * 0.8F + 0.1F;
                        float var9 = random.nextFloat() * 0.8F + 0.1F;
                        float var10 = random.nextFloat() * 0.8F + 0.1F;

                        while (itemStack.stackSize > 0) {
                            int var11 = random.nextInt(21) + 10;

                            if (var11 > itemStack.stackSize) {
                                var11 = itemStack.stackSize;
                            }

                            itemStack.stackSize -= var11;

                            EntityItem entityItem = new EntityItem(world, x + var8, y + var9, z + var10, new ItemStack(itemStack.getItem(), var11, itemStack.getMetadata()));

                            if (itemStack.hasTagCompound()) {
                                entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                            }

                            float var13 = 0.05F;
                            entityItem.motionX = random.nextGaussian() * var13;
                            entityItem.motionY = (random.nextGaussian() * var13) + 0.2F;
                            entityItem.motionZ = random.nextGaussian() * var13;
                            world.spawnEntityInWorld(entityItem);

                            if (itemStack.stackSize <= 0) {
                                inventory.setInventorySlotContents(i, null);
                            }
                        }
                    }
                }
            }
        }
    }
}
