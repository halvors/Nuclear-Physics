package org.halvors.nuclearphysics.common.block.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TilePlasma;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockFluidPlasma extends Block implements IFluidBlock {
    private Fluid fluid;

    public BlockFluidPlasma(final Fluid fluid, final Material material) {
        super(material);

        fluid.setBlock(this);

        this.fluid = fluid;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, @Nonnull BlockRenderLayer layer) {
        return layer == BlockRenderLayer.TRANSLUCENT;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        Block neighborBlock = world.getBlockState(pos.offset(side)).getBlock();

        return neighborBlock != this && super.shouldSideBeRendered(state, world, pos, side);

    }

    @Override
    public int getLightValue(@Nonnull IBlockState state, IBlockAccess access, @Nonnull BlockPos pos) {
        return 7;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean fullHit) {
        return false;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        entity.attackEntityFrom(DamageSource.IN_FIRE, 100);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TilePlasma();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Fluid getFluid() {
        return fluid;
    }

    @Override
    public int place(World world, BlockPos pos, @Nonnull FluidStack fluidStack, boolean doPlace) {
        return 0;
    }

    @Override
    public FluidStack drain(World world, BlockPos pos, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canDrain(World world, BlockPos pos) {
        return false;
    }

    @Override
    public float getFilledPercentage(World world, BlockPos pos) {
        return Fluid.BUCKET_VOLUME;
    }
}
