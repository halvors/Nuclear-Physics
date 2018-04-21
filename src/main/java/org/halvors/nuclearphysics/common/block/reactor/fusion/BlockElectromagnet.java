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
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.block.BlockConnectedTexture;
import org.halvors.nuclearphysics.common.tile.particle.TileElectromagnet;
import org.halvors.nuclearphysics.common.type.Position;

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
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);

        iconTop = iconRegister.registerIcon(Reference.PREFIX + name + "_top");
        iconGlass = iconRegister.registerIcon(Reference.PREFIX + name + "_glass");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
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
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        Position neighborPosition = new Position(x, y, z).translate(ForgeDirection.getOrientation(side).getOpposite());
        Block block = world.getBlock(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);
        Block neighborBlock = neighborPosition.getBlock(world);
        int neighborMetadata = neighborPosition.getBlockMetadata(world);

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
        for (EnumElectromagnet type : EnumElectromagnet.values()) {
            list.add(new ItemStack(item, 1, type.ordinal()));
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        world.setBlockMetadataWithNotify(x, y, z, itemStack.getMetadata(), 2);
    }

    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
        EnumElectromagnet type = EnumElectromagnet.values()[world.getBlockMetadata(x, y, z)];

        if (type == EnumElectromagnet.GLASS) {
            return 0;
        }

        return 255;
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileElectromagnet();
    }

    public enum EnumElectromagnet {
        NORMAL("normal"),
        GLASS("glass");

        private String name;

        EnumElectromagnet(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
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