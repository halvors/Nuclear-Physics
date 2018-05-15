package org.halvors.nuclearphysics.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.common.tile.ITileRotatable;

public class BlockRotatable extends BlockContainerBase {
    protected byte rotationMask = Byte.parseByte("111100", 2);
    protected boolean isFlipPlacement = false;

    protected BlockRotatable(final String name, final Material material) {
        super(name, material);
    }

    @Override
    public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase entity, final ItemStack item) {
        final BlockPos pos = new BlockPos(x, y, z);
        final TileEntity tile = pos.getTileEntity(world);

        if (tile instanceof ITileRotatable) {
            final ITileRotatable tileRotatable = (ITileRotatable) tile;

            tileRotatable.setFacing(ForgeDirection.VALID_DIRECTIONS[determineOrientation(world, x, y, z, entity)]);
        }
    }

    @Override
    public ForgeDirection[] getValidRotations(final World world, final int x, final int y, final int z) {
        final BlockPos pos = new BlockPos(x, y, z);
        final TileEntity tile = pos.getTileEntity(world);
        final ForgeDirection[] valid = new ForgeDirection[6];

        if (tile instanceof ITileRotatable) {
            final ITileRotatable tileRotatable = (ITileRotatable) tile;

            for (final ForgeDirection facing : ForgeDirection.VALID_DIRECTIONS) {
                if (tileRotatable.canSetFacing(facing)) {
                    valid[facing.ordinal()] = facing;
                }
            }
        }

        return valid;
    }

    @Override
    public boolean rotateBlock(final World world, final int x, final int y, final int z, final ForgeDirection side) {
        final BlockPos pos = new BlockPos(x, y, z);
        final TileEntity tile = pos.getTileEntity(world);

        if (tile instanceof ITileRotatable) {
            final ITileRotatable tileRotatable = (ITileRotatable) tile;

            if (tileRotatable.canSetFacing(side)) {
                tileRotatable.setFacing(side);
            }

            return true;
        }

        return false;
    }

    public boolean canRotate(final int ordinal) {
        return (rotationMask & 1 << ordinal) != 0;
    }

    public int determineOrientation(final World world, final int x, final int y, final int z, EntityLivingBase entity) {
        if (MathHelper.abs((float) entity.posX - x) < 2 && MathHelper.abs((float) entity.posZ - z) < 2) {
            double d0 = entity.posY + 1.82D - entity.yOffset;

            if (canRotate(1) && (d0 - y > 2)) {
                return 1;
            }

            if (canRotate(0) && (y - d0 > 0)) {
                return 0;
            }
        }

        int playerSide = MathHelper.floor_double(entity.rotationYaw * 4 / 360 + 0.5) & 0x3;
        int returnSide = playerSide == 3 && canRotate(4) ? 4 : playerSide == 2 && canRotate(3) ? 3 : playerSide == 1 && canRotate(5) ? 5 : playerSide == 0 && canRotate(2) ? 2 : 0;

        if (isFlipPlacement) {
            return ForgeDirection.getOrientation(returnSide).getOpposite().ordinal();
        }

        return returnSide;
    }
}