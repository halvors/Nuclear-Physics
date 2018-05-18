package org.halvors.nuclearphysics.common.block.reactor.fusion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.block.BlockConnectedTexture;
import org.halvors.nuclearphysics.common.tile.particle.TileElectromagnet;

import java.util.List;

public class BlockElectromagnet extends BlockConnectedTexture {
    @SideOnly(Side.CLIENT)
    private static IIcon iconTop, iconGlass;

    public BlockElectromagnet() {
        super("electromagnet", Material.iron);

        setHardness(3.5F);
        setResistance(20);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
        super.registerIcons(iconRegister);

        iconTop = iconRegister.registerIcon(Reference.PREFIX + name + "_top");
        iconGlass = iconRegister.registerIcon(Reference.PREFIX + name + "_glass");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int metadata) {
        EnumElectromagnet type = EnumElectromagnet.values()[metadata];

        if (type == EnumElectromagnet.GLASS) {
            return iconGlass;
        }

        if (side == 0 || side == 1) {
            return iconTop;
        }

        return super.getIcon(side, metadata);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(final IBlockAccess world, final int x, final int y, final int z, final int side) {
        final BlockPos pos = new BlockPos(x, y, z);
        final Block block = pos.getBlock(world);
        final int metadata = pos.getBlockMetadata(world);
        final BlockPos neighborPos = pos.offset(ForgeDirection.getOrientation(side).getOpposite());
        final Block neighborBlock = neighborPos.getBlock(world);
        final int neighborMetadata = neighborPos.getBlockMetadata(world);

        // Transparent electromagnetic glass.
        if (block == this && neighborBlock == this && metadata == 1 && neighborMetadata == 1) {
            return false;
        }

        return super.shouldSideBeRendered(world, x, y, z, side);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (final EnumElectromagnet type : EnumElectromagnet.values()) {
            list.add(new ItemStack(item, 1, type.ordinal()));
        }
    }

    @Override
    public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase entity, final ItemStack itemStack) {
        world.setBlockMetadataWithNotify(x, y, z, itemStack.getMetadata(), 2);
    }

    @Override
    public int getLightOpacity(final IBlockAccess world, final int x, final int y, final int z) {
        final EnumElectromagnet type = EnumElectromagnet.values()[world.getBlockMetadata(x, y, z)];

        if (type == EnumElectromagnet.GLASS) {
            return 0;
        }

        return super.getLightOpacity(world, x, y, z);
    }

    @Override
    public int damageDropped(final int metadata) {
        return metadata;
    }

    @Override
    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileElectromagnet();
    }

    public enum EnumElectromagnet {
        NORMAL,
        GLASS;

        public String getName() {
            return name().toLowerCase();
        }
    }

    /*
    Note: Impossible due to lacking blockstates in 1.7.10.
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
    */
}