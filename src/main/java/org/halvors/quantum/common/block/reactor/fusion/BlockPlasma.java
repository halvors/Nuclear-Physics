package org.halvors.quantum.common.block.reactor.fusion;

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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.block.BlockContainerQuantum;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasma;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockPlasma extends BlockContainerQuantum {
    public BlockPlasma() {
        super("plasma", Material.LAVA);
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
        return null;
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
    @Nonnull
    public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
        return new TilePlasma();
    }
}
