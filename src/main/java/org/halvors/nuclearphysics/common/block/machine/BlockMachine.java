package org.halvors.nuclearphysics.common.block.machine;

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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.BlockInventory;
import org.halvors.nuclearphysics.common.block.states.BlockStateMachine;
import org.halvors.nuclearphysics.common.tile.machine.TileChemicalExtractor;
import org.halvors.nuclearphysics.common.tile.machine.TileGasCentrifuge;
import org.halvors.nuclearphysics.common.tile.machine.TileNuclearBoiler;
import org.halvors.nuclearphysics.common.tile.machine.TileQuantumAssembler;
import org.halvors.nuclearphysics.common.tile.particle.TileAccelerator;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TilePlasmaHeater;
import org.halvors.nuclearphysics.common.utility.FluidUtility;
import org.halvors.nuclearphysics.common.utility.PlayerUtility;

import javax.annotation.Nonnull;

public class BlockMachine extends BlockInventory {
    public BlockMachine() {
        super("machine", Material.IRON);

        setDefaultState(blockState.getBaseState().withProperty(BlockStateMachine.TYPE, EnumMachine.CHEMICAL_EXTRACTOR));
    }

    @Override
    public void registerItemModel(ItemBlock itemBlock) {
        for (EnumMachine type : EnumMachine.values()) {
            NuclearPhysics.getProxy().registerItemRenderer(itemBlock, type.ordinal(), name, "facing=north,type=" + type.getName());
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return state.getValue(BlockStateMachine.TYPE).getRenderType();
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(@Nonnull Item item, CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
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
        return getDefaultState().withProperty(BlockStateMachine.TYPE, EnumMachine.values()[metadata]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockStateMachine.TYPE).ordinal();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack itemStack) {
        world.setBlockState(pos, state.withProperty(BlockStateMachine.TYPE, EnumMachine.values()[itemStack.getItemDamage()]));

        super.onBlockPlacedBy(world, pos, state, entity, itemStack);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TilePlasmaHeater) {
            return FluidUtility.playerActivatedFluidItem(world, pos, player, facing);
        } else if (!player.isSneaking()) {
            PlayerUtility.openGui(player, world, pos);

            return true;
        }

        return false;
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        EnumMachine type = state.getValue(BlockStateMachine.TYPE);

        return type.getTileAsInstance();
    }

    public enum EnumMachine implements IStringSerializable {
        ACCELERATOR("accelerator", TileAccelerator.class, EnumBlockRenderType.MODEL),
        CHEMICAL_EXTRACTOR("chemical_extractor", TileChemicalExtractor.class, EnumBlockRenderType.ENTITYBLOCK_ANIMATED),
        GAS_CENTRIFUGE("gas_centrifuge", TileGasCentrifuge.class, EnumBlockRenderType.ENTITYBLOCK_ANIMATED),
        NUCLEAR_BOILER("nuclear_boiler", TileNuclearBoiler.class, EnumBlockRenderType.ENTITYBLOCK_ANIMATED),
        PLASMA_HEATER("plasma_heater", TilePlasmaHeater.class, EnumBlockRenderType.ENTITYBLOCK_ANIMATED),
        QUANTUM_ASSEMBLER("quantum_assembler", TileQuantumAssembler.class, EnumBlockRenderType.ENTITYBLOCK_ANIMATED);

        private String name;
        private Class<? extends TileEntity> tileClass;
        private EnumBlockRenderType renderType;

        EnumMachine(String name, Class<? extends TileEntity> tileClass, EnumBlockRenderType renderType) {
            this.name = name;
            this.tileClass = tileClass;
            this.renderType = renderType;
        }

        @Override
        public String getName() {
            return name;
        }

        public Class<? extends TileEntity> getTileClass() {
            return tileClass;
        }

        public TileEntity getTileAsInstance() {
            try {
                return tileClass.newInstance();
            } catch (Exception e) {
                NuclearPhysics.getLogger().error("Unable to indirectly create tile entity.");
                e.printStackTrace();

                return null;
            }
        }

        public EnumBlockRenderType getRenderType() {
            return renderType;
        }
    }
}