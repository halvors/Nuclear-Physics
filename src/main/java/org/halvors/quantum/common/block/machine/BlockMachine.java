package org.halvors.quantum.common.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.block.BlockRotatable;
import org.halvors.quantum.common.tile.particle.TileAccelerator;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasmaHeater;
import org.halvors.quantum.common.utility.FluidUtility;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockMachine extends BlockRotatable {
    private static final PropertyEnum<EnumMachine> type = PropertyEnum.create("type", EnumMachine.class);

    public BlockMachine() {
        super("machine", Material.IRON);

        setDefaultState(blockState.getBaseState().withProperty(type, EnumMachine.ACCELERATOR));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(@Nonnull Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        for (EnumMachine type : EnumMachine.values()) {
            list.add(new ItemStack(item, 1, type.ordinal()));
        }
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, type, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(type).ordinal();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack itemStack) {
        super.onBlockPlacedBy(world, pos, state, entity, itemStack);

        world.setBlockState(pos, state.withProperty(type, EnumMachine.values()[itemStack.getItemDamage()]), 2);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack itemStack, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TilePlasmaHeater) {
            return FluidUtility.playerActivatedFluidItem(world, pos, player, side);
        } else {
            if (!player.isSneaking()) {
                player.openGui(Quantum.getInstance(), 0, world, pos.getX(), pos.getY(), pos.getZ());

                return true;
            }
        }

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
        final EnumMachine type = EnumMachine.values()[metadata];

        return type.getTileAsNewIntance();
    }

    public enum EnumMachine implements IStringSerializable {
        ACCELERATOR("accelerator", TileAccelerator.class),
        PLASMA_HEATER("plasma_heater", TilePlasmaHeater.class);

        private String name;
        private Class<? extends TileEntity> tileClass;

        EnumMachine(String name, Class<? extends TileEntity> tileClass) {
            this.name = name;
            this.tileClass = tileClass;
        }

        @Override
        public String getName() {
            return name.toLowerCase();
        }

        public Class<? extends TileEntity> getTileClass() {
            return tileClass;
        }

        public TileEntity getTileAsNewIntance() {
            try {
                return tileClass.newInstance();
            } catch (Exception e) {
                Quantum.getLogger().error("Unable to indirectly create tile entity.");
                e.printStackTrace();

                return null;
            }
        }
    }
}
