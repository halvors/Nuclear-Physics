package org.halvors.quantum.atomic.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.Random;

public abstract class BlockInventory extends BlockRotatable {
    public BlockInventory(String name, Material material) {
        super(name, material);
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        dropEntireInventory(world, pos, state);

        super.breakBlock(world, pos, state);
    }

    public void dropEntireInventory(World world, BlockPos pos, IBlockState state) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile != null) {
            if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                final IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

                if (itemHandler instanceof IItemHandlerModifiable) {
                    final IItemHandlerModifiable inventory = (IItemHandlerModifiable) itemHandler;

                    for (int i = 0; i < inventory.getSlots(); i++) {
                        final ItemStack itemStack = inventory.getStackInSlot(i);

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

                                EntityItem entityItem = new EntityItem(world, pos.getX() + var8, pos.getY() + var9, pos.getZ() + var10, new ItemStack(itemStack.getItem(), var11, itemStack.getMetadata()));

                                if (itemStack.hasTagCompound()) {
                                    entityItem.getEntityItem().setTagCompound(itemStack.getTagCompound().copy());
                                }

                                float var13 = 0.05F;
                                entityItem.motionX = random.nextGaussian() * var13;
                                entityItem.motionY = (random.nextGaussian() * var13) + 0.2F;
                                entityItem.motionZ = random.nextGaussian() * var13;
                                world.spawnEntity(entityItem);

                                if (itemStack.stackSize <= 0) {
                                    inventory.setStackInSlot(i, null);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
