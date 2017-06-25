package org.halvors.quantum.lib.prefab.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.lib.block.IRotatableBlock;

public abstract class BlockRotatable extends BlockTile implements IRotatableBlock {
    protected byte rotationMask = Byte.parseByte("111100", 2);
    protected boolean isFlipPlacement = false;

    public BlockRotatable(Material material) {
        super(material);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        world.setBlockMetadataWithNotify(x, y, z, determineOrientation(world, x, y, z, entityLiving), 3);
    }

    public int determineOrientation(World world, int x, int y, int z, EntityLivingBase entity) {
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

    public boolean canRotate(int ordinal) {
        return (rotationMask & 1 << ordinal) != 0;
    }

    @Override
    public boolean onSneakUseWrench(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ) {
        return doRotateBlock(world, x, y, z, ForgeDirection.getOrientation(side));
    }

    @Override
    public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ) {
        return onSneakUseWrench(world, x, y, z, par5EntityPlayer, side, hitX, hitY, hitZ);
    }

    public boolean doRotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis) {
        int currentRotMeta = worldObj.getBlockMetadata(x, y, z);
        ForgeDirection orientation = ForgeDirection.getOrientation(currentRotMeta);
        ForgeDirection rotated = orientation.getRotation(axis);
        int metadata = rotated.ordinal();
        int metadataBit = 1 << metadata;

        if ((metadataBit & rotationMask) == metadataBit && canRotate(metadata)) {
            worldObj.setBlockMetadataWithNotify(x, y, z, metadata, 3);

            return true;
        }

        return false;
    }

    @Override
    public ForgeDirection getDirection(World world, int x, int y, int z) {
        return ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
    }

    @Override
    public void setDirection(World world, int x, int y, int z, ForgeDirection direction) {
        world.setBlockMetadataWithNotify(x, y, z, direction.ordinal(), 3);
    }
}
