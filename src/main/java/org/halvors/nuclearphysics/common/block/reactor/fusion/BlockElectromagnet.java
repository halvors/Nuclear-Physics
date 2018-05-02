package org.halvors.nuclearphysics.common.block.reactor.fusion;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.BlockConnectedTexture;
import org.halvors.nuclearphysics.common.block.states.BlockStateElectromagnet;
import org.halvors.nuclearphysics.common.block.states.BlockStateElectromagnet.EnumElectromagnet;
import org.halvors.nuclearphysics.common.tile.particle.TileElectromagnet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockElectromagnet extends BlockConnectedTexture {
    public BlockElectromagnet() {
        super("electromagnet", Material.IRON);

        setHardness(3.5F);
        setResistance(20);
        setDefaultState(blockState.getBaseState().withProperty(BlockStateElectromagnet.TYPE, EnumElectromagnet.NORMAL));
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nonnull
    public SoundType getSoundType(final IBlockState state, final World world, final BlockPos pos, final @Nullable Entity entity) {
        final EnumElectromagnet type = state.getValue(BlockStateElectromagnet.TYPE);

        if (type == EnumElectromagnet.GLASS) {
            return SoundType.GLASS;
        }

        return super.getSoundType(state, world, pos, entity);
    }

    @Override
    public void registerBlockModel() {
        NuclearPhysics.getProxy().registerBlockRenderer(this, BlockStateElectromagnet.TYPE, name);
    }

    @Override
    public void registerItemModel(final ItemBlock itemBlock) {
        for (final EnumElectromagnet type : EnumElectromagnet.values()) {
            NuclearPhysics.getProxy().registerItemRenderer(itemBlock, type.ordinal(), type.getName() + "_" + name);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderInLayer(final IBlockState state, final @Nonnull BlockRenderLayer layer) {
        final EnumElectromagnet type = state.getValue(BlockStateElectromagnet.TYPE);

        if (type == EnumElectromagnet.GLASS) {
            return layer == BlockRenderLayer.CUTOUT;
        }

        return super.canRenderInLayer(state, layer);
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFullCube(final IBlockState state) {
        final EnumElectromagnet type = state.getValue(BlockStateElectromagnet.TYPE);

        return type != EnumElectromagnet.GLASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(final IBlockState state) {
        final EnumElectromagnet type = state.getValue(BlockStateElectromagnet.TYPE);

        return type != EnumElectromagnet.GLASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(final IBlockState state, @Nonnull final IBlockAccess world, @Nonnull final BlockPos pos, final EnumFacing side) {
        return !canConnect(state, world.getBlockState(pos.offset(side))) && super.shouldSideBeRendered(state, world, pos, side);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final CreativeTabs tab, final NonNullList<ItemStack> list) {
        for (EnumElectromagnet type : EnumElectromagnet.values()) {
            list.add(new ItemStack(this, 1, type.ordinal()));
        }
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState() {
        return new BlockStateElectromagnet(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState getStateFromMeta(final int metadata) {
        return getDefaultState().withProperty(BlockStateElectromagnet.TYPE, EnumElectromagnet.values()[metadata]);
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockStateElectromagnet.TYPE).ordinal();
    }

    @Override
    public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase entity, final ItemStack itemStack) {
        world.setBlockState(pos, state.withProperty(BlockStateElectromagnet.TYPE, EnumElectromagnet.values()[itemStack.getItemDamage()]), 2);
    }

    @Override
    public int getLightOpacity(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
        final EnumElectromagnet type = state.getValue(BlockStateElectromagnet.TYPE);

        if (type == EnumElectromagnet.GLASS) {
            return 0;
        }

        return super.getLightOpacity(state, world, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSideSolid(final IBlockState state, @Nonnull final IBlockAccess world, @Nonnull final BlockPos pos, final EnumFacing side) {
        return state.getValue(BlockStateElectromagnet.TYPE) == EnumElectromagnet.NORMAL;
    }

    @Override
    public int damageDropped(final IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    protected boolean canConnect(final IBlockState originalState, final IBlockState connectedState) {
        if (originalState.getBlock() == connectedState.getBlock()) {
            final EnumElectromagnet originalType = originalState.getValue(BlockStateElectromagnet.TYPE);
            final EnumElectromagnet connectedType = connectedState.getValue(BlockStateElectromagnet.TYPE);

            return originalType == connectedType;
        }

        return super.canConnect(originalState, connectedState);
    }

    @Override
    public TileEntity createTileEntity(@Nonnull final World world, @Nonnull final IBlockState state) {
        return new TileElectromagnet();
    }
}