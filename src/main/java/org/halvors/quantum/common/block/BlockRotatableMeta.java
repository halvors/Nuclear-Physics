package org.halvors.quantum.common.block;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class BlockRotatableMeta extends BlockContainerQuantum {
    public static final PropertyDirection facing = BlockHorizontal.FACING;

    public BlockRotatableMeta(String name, Material material) {
        super(name, material);

        setDefaultState(blockState.getBaseState().withProperty(facing, EnumFacing.NORTH));
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(facing).getHorizontalIndex();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack itemStack) {
        world.setBlockState(pos, state.withProperty(facing, entity.getHorizontalFacing().getOpposite()), 2);
    }

    /*
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
    */
}
