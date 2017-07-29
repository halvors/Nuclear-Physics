package org.halvors.quantum.common.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
import org.halvors.quantum.common.block.BlockInventory;
import org.halvors.quantum.common.block.states.BlockStateMachine;
import org.halvors.quantum.common.tile.particle.TileAccelerator;
import org.halvors.quantum.common.tile.reactor.TileGasFunnel;
import org.halvors.quantum.common.utility.PlayerUtility;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockMachine extends BlockInventory {
    public BlockMachine() {
        super("machine", Material.IRON);
    }

    @Override
    public void registerItemModel(ItemBlock itemBlock) {
        for (EnumMachine type : EnumMachine.values()) {
            Quantum.getProxy().registerItemRenderer(itemBlock, type.ordinal(), name, "facing=north,type=" + type.getName());
        }
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
        return new BlockStateMachine(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int metadata) {
        return getDefaultState().withProperty(BlockStateMachine.typeProperty, EnumMachine.values()[metadata]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockStateMachine.typeProperty).ordinal();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack itemStack) {
        super.onBlockPlacedBy(world, pos, state, entity, itemStack);

        world.setBlockState(pos, state.withProperty(BlockStateMachine.typeProperty, EnumMachine.values()[itemStack.getItemDamage()]), 2);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack itemStack, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            PlayerUtility.openGui(player, world, pos);

            return true;
        }

        return false;
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return state.getValue(BlockStateMachine.typeProperty).getTileAsInstance();
    }

    public enum EnumMachine implements IStringSerializable {
        ACCELERATOR("accelerator", TileAccelerator.class),
        GAS_FUNNEL("gas_funnel", TileGasFunnel.class);

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

        public TileEntity getTileAsInstance() {
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
