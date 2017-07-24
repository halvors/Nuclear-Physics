package org.halvors.quantum.common.block.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasma;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockFluidPlasma extends BlockFluidBase implements ITileEntityProvider {
    public BlockFluidPlasma(final Fluid fluid, final Material material) {
        super(fluid, material);
        // TODO: Check if material should be lava.
        //super("plasma", Material.LAVA);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        Block neighborBlock = world.getBlockState(pos.offset(side)).getBlock();

        if (neighborBlock == this) {
            return false;
        }

        return super.shouldSideBeRendered(state, world, pos, side);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public int getLightValue(@Nonnull IBlockState state, IBlockAccess access, @Nonnull BlockPos pos) {
        return 7;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess access, @Nonnull BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        entity.attackEntityFrom(DamageSource.inFire, 100);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public int getQuantaValue(IBlockAccess world, BlockPos pos) {
        return 210;
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean fullHit) {
        return false;
    }

    @Override
    public int getMaxRenderHeightMeta() {
        return 0;
    }

    @Override
    public FluidStack drain(World world, BlockPos pos, boolean doDrain) {
        final FluidStack fluidStack = new FluidStack(getFluid(), MathHelper.floor(getQuantaPercentage(world, pos) * Fluid.BUCKET_VOLUME));

        if (doDrain) {
            world.setBlockToAir(pos);
        }

        return fluidStack;
    }

    @Override
    public boolean canDrain(World world, BlockPos pos) {
        return true;
    }

    @Override
    @Nonnull
    public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
        return new TilePlasma();
    }
}
