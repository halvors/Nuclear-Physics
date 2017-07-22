package org.halvors.quantum.common.block.reactor.fusion;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.block.BlockContainerQuantum;
import org.halvors.quantum.common.tile.reactor.fusion.TileElectromagnet;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockElectromagnet extends BlockContainerQuantum {
    private static final PropertyEnum<EnumElectromagnet> type = PropertyEnum.create("type", EnumElectromagnet.class);

    public BlockElectromagnet() {
        super("electromagnet", Material.IRON);

        setResistance(20);
        setDefaultState(blockState.getBaseState().withProperty(type, EnumElectromagnet.NORMAL));
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, type);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(type).ordinal();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack itemStack) {
        world.setBlockState(pos, state.withProperty(type, EnumElectromagnet.values()[itemStack.getItemDamage()]), 2);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        /*
        Vector3 neighborPosition = new Vector3(pos.getX(), pos.getY(), pos.getZ()).translate(side.getOpposite());
        Block block = state.getBlock();
        int metadata = state.getBlock().getMetaFromState(state);
        Block neighborBlock = neighborPosition.getBlock(world);
        int neighborMetadata = neighborPosition.getBlock(world).getMetaFromState(state);

        // Transparent electromagnetic glass.
        if (block == this && neighborBlock == this && metadata == 1 && neighborMetadata == 1) {
            return false;
        }
        */

        return super.shouldSideBeRendered(state, world, pos, side);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public void getSubBlocks(@Nonnull Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        for (EnumElectromagnet type : EnumElectromagnet.values()) {
            list.add(new ItemStack(item, 1, type.ordinal()));
        }
    }

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public ISimpleBlockRenderer getRenderer() {
        return new ConnectedTextureRenderer(this, Reference.PREFIX + "atomic_edge");
    }

    @Override
    public boolean canRenderInLayer(BlockRenderLayer layer) {
        //return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT;
        return true;
    }
    */

    @Override
    @Nonnull
    public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
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
            return name().toLowerCase();
        }
    }
}