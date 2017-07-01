package org.halvors.quantum.common.block;

import net.minecraft.block.material.Material;

public abstract class BlockRotatable extends BlockContainerQuantum { //implements IRotatableBlock {
    protected byte rotationMask = Byte.parseByte("111100", 2);
    protected boolean isFlipPlacement = false;

    public BlockRotatable(String name, Material material) {
        super(name, material);
    }

    /*
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entityLiving, ItemStack itemStack) {
        world.setBlockMetadataWithNotify(x, y, z, determineOrientation(world, x, y, z, entityLiving), 3);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            if (WrenchUtility.hasUsableWrench(player, pos)) {
                rotate(world, pos, side);

                return true;
            }
        }

        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }

    public int determineOrientation(World world, int x, int y, int z, EntityLivingBase entity) {
        if (MathHelper.abs((float) entity.posX - x) < 2 && MathHelper.abs((float) entity.posZ - z) < 2) {
            double d0 = entity.posY + 1.82D - entity.getYOffset();

            if (canRotate(1) && (d0 - y > 2)) {
                return 1;
            }

            if (canRotate(0) && (y - d0 > 0)) {
                return 0;
            }
        }

        int playerSide = MathHelper.floor(entity.rotationYaw * 4 / 360 + 0.5) & 0x3;
        int returnSide = playerSide == 3 && canRotate(4) ? 4 : playerSide == 2 && canRotate(3) ? 3 : playerSide == 1 && canRotate(5) ? 5 : playerSide == 0 && canRotate(2) ? 2 : 0;

        if (isFlipPlacement) {
            // TODO: Proper replacement in 1.10.2?
            //return EnumFacing.getOrientation(returnSide).getOpposite().ordinal();
            return EnumFacing.getFront(returnSide).getOpposite().ordinal();
        }

        return returnSide;
    }

    public boolean canRotate(int ordinal) {
        return (rotationMask & 1 << ordinal) != 0;
    }

    public boolean rotate(World worldObj, int x, int y, int z, EnumFacing axis) {
        int currentRotMeta = worldObj.getBlockMetadata(x, y, z);
        EnumFacing orientation = EnumFacing.getOrientation(currentRotMeta);
        EnumFacing rotated = orientation.getRotation(axis);
        int metadata = rotated.ordinal();
        int metadataBit = 1 << metadata;

        if ((metadataBit & rotationMask) == metadataBit && canRotate(metadata)) {
            worldObj.setBlockMetadataWithNotify(x, y, z, metadata, 3);

            return true;
        }

        return false;
    }

    @Override
    public EnumFacing getDirection(World world, int x, int y, int z) {
        return EnumFacing.getOrientation(world.getBlockMetadata(x, y, z));
    }

    @Override
    public void setDirection(World world, int x, int y, int z, EnumFacing direction) {
        world.setBlockMetadataWithNotify(x, y, z, direction.ordinal(), 3);
    }
    */
}
