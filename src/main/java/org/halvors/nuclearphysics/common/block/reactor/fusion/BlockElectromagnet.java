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
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.BlockConnectedTexture;
import org.halvors.nuclearphysics.common.block.states.BlockStateElectromagnet;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TileElectromagnet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockElectromagnet extends BlockConnectedTexture {
    public BlockElectromagnet() {
        super("electromagnet", Material.IRON);

        setResistance(20);
        setDefaultState(blockState.getBaseState().withProperty(BlockStateElectromagnet.TYPE, EnumElectromagnet.NORMAL));
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nonnull
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        EnumElectromagnet type = state.getValue(BlockStateElectromagnet.TYPE);

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
    public void registerItemModel(ItemBlock itemBlock) {
        for (EnumElectromagnet type : EnumElectromagnet.values()) {
            NuclearPhysics.getProxy().registerItemRenderer(itemBlock, type.ordinal(), type.getName() + "_" + name);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderInLayer(IBlockState state, @Nonnull BlockRenderLayer layer) {
        EnumElectromagnet type = state.getValue(BlockStateElectromagnet.TYPE);

        if (type == EnumElectromagnet.GLASS) {
            return layer == BlockRenderLayer.CUTOUT;
        }

        return layer == BlockRenderLayer.SOLID;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFullCube(IBlockState state) {
        EnumElectromagnet type = state.getValue(BlockStateElectromagnet.TYPE);

        return type != EnumElectromagnet.GLASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(IBlockState state) {
        EnumElectromagnet type = state.getValue(BlockStateElectromagnet.TYPE);

        return type != EnumElectromagnet.GLASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return !canConnect(state, world.getBlockState(pos.offset(side))) && super.shouldSideBeRendered(state, world, pos, side);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs item, NonNullList<ItemStack> list) {
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
    public IBlockState getStateFromMeta(int metadata) {
        return getDefaultState().withProperty(BlockStateElectromagnet.TYPE, EnumElectromagnet.values()[metadata]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockStateElectromagnet.TYPE).ordinal();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack itemStack) {
        world.setBlockState(pos, state.withProperty(BlockStateElectromagnet.TYPE, EnumElectromagnet.values()[itemStack.getItemDamage()]), 2);
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        EnumElectromagnet type = state.getValue(BlockStateElectromagnet.TYPE);

        if (type == EnumElectromagnet.GLASS) {
            return 0;
        }

        return 255;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSideSolid(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return state.getValue(BlockStateElectromagnet.TYPE) == EnumElectromagnet.NORMAL;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    protected boolean canConnect(@Nonnull IBlockState originalState, @Nonnull IBlockState connectedState) {
        if (originalState.getBlock() == connectedState.getBlock()) {
            EnumElectromagnet originalType = originalState.getValue(BlockStateElectromagnet.TYPE);
            EnumElectromagnet connectedType = connectedState.getValue(BlockStateElectromagnet.TYPE);

            return originalType == connectedType;
        }

        return super.canConnect(originalState, connectedState);
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileElectromagnet();
    }

    public enum EnumElectromagnet implements IStringSerializable {
        NORMAL("normal"),
        GLASS("glass");

        private String name;

        EnumElectromagnet(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}