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
import java.util.Random;

public class BlockFluidPlasma extends Block implements IFluidBlock {
    private Fluid fluid;

    public BlockFluidPlasma(final Fluid fluid, final Material material) {
        super(material);

        this.fluid = fluid;

        fluid.setBlock(this);
    }

    @Override
    public boolean canRenderInLayer(final IBlockState state, final @Nonnull BlockRenderLayer layer) {
        return layer == BlockRenderLayer.TRANSLUCENT;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(final IBlockState state, final @Nonnull IBlockAccess world, final @Nonnull BlockPos pos, final EnumFacing side) {
        final IBlockState neighborState = world.getBlockState(pos.offset(side));

        return neighborState != state && super.shouldSideBeRendered(state, world, pos, side);

    }

    @Override
    public int getLightValue(final @Nonnull IBlockState state, final IBlockAccess access, final @Nonnull BlockPos pos) {
        return fluid.getLuminosity();
    }

    @Override
    public boolean isBlockSolid(final IBlockAccess access, final @Nonnull BlockPos pos, final EnumFacing side) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState state, final @Nonnull World world, final @Nonnull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean canCollideCheck(final IBlockState state, final boolean fullHit) {
        return false;
    }

    @Override
    public void onEntityCollidedWithBlock(final World world, final BlockPos pos, final IBlockState state, final Entity entity) {
        entity.attackEntityFrom(DamageSource.inFire, 100);
    }

    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }

    @Override
    public boolean hasTileEntity(final IBlockState state) {
        return true;
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(final @Nonnull World world, final @Nonnull IBlockState state) {
        return new TilePlasma();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Fluid getFluid() {
        return fluid;
    }

    @Override
    public FluidStack drain(final World world, final BlockPos pos, final boolean doDrain) {
        return null;
    }

    @Override
    public boolean canDrain(final World world, final BlockPos pos) {
        return false;
    }

    @Override
    public float getFilledPercentage(final World world, final BlockPos pos) {
        return Fluid.BUCKET_VOLUME;
    }
}
