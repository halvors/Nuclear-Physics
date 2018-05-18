package org.halvors.nuclearphysics.common.block.fluid;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TilePlasma;

import java.util.Random;

public class BlockFluidPlasma extends Block implements IFluidBlock {
    private Fluid fluid;

    public BlockFluidPlasma(final Fluid fluid, final Material material) {
        super(material);

        this.fluid = fluid;

        fluid.setBlock(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean canRenderInPass(int pass) {
        return true;
    }

    /*
    @Override
    public boolean canRenderInLayer(final IBlockState state, final @Nonnull BlockRenderLayer layer) {
        return layer == BlockRenderLayer.TRANSLUCENT;
    }
    */

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(final IBlockAccess world, final int x, final int y, final int z, final int side) {
        final BlockPos pos = new BlockPos(x, y, z);
        final BlockPos neighborPos = pos.offset(ForgeDirection.getOrientation(side));
        final Block neighborBlock = neighborPos.getBlock(world);

        return neighborBlock != this && super.shouldSideBeRendered(world, x, y, z, side);
    }

    @Override
    public int getLightValue(final IBlockAccess world, final int x, final int y, final int z) {
        return fluid.getLuminosity();
    }

    @Override
    public boolean isBlockSolid(final IBlockAccess world, final int x, final int y, final int z, final int side) {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
    }

    @Override
    public void onEntityCollidedWithBlock(final World world, final int x, final int y, final int z, final Entity entity) {
        entity.attackEntityFrom(DamageSource.inFire, 100);
    }

    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }

    @Override
    public boolean hasTileEntity(final int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TilePlasma();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Fluid getFluid() {
        return fluid;
    }

    @Override
    public FluidStack drain(final World world, final int x, final int y, final int z, final boolean doDrain) {
        return null;
    }

    @Override
    public boolean canDrain(final World world, final int x, final int y, final int z) {
        return false;
    }

    @Override
    public float getFilledPercentage(final World world, final int x, final int y, final int z) {
        return FluidContainerRegistry.BUCKET_VOLUME;
    }
}
