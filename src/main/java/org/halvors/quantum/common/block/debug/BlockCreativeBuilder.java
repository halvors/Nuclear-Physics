package org.halvors.quantum.common.block.debug;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.block.BlockRotatable;
import org.halvors.quantum.common.schematic.ISchematic;

import java.util.ArrayList;
import java.util.List;

/**
 * Automatically set up structures to allow easy debugging in creative mode.
 */
public class BlockCreativeBuilder extends BlockRotatable {
    private static final List<ISchematic> schematicRegistry = new ArrayList<>();

    public static int registerSchematic(ISchematic schematic) {
        schematicRegistry.add(schematic);

        return schematicRegistry.size() - 1;
    }

    public static ISchematic getSchematic(int id) {
        return schematicRegistry.get(id);
    }

    public static int getSchematicCount() {
        return schematicRegistry.size();
    }

    public BlockCreativeBuilder() {
        super("creativeBuilder", Material.IRON);

        rotationMask = Byte.parseByte("111111", 2);

        //setTextureName(Reference.PREFIX + "creativeBuilder");
        setCreativeTab(Quantum.getCreativeTab());
    }

    // Called when the block is right clicked by the player.
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack itemStack, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (schematicRegistry.size() > 0) {
            player.openGui(Quantum.getInstance(), 0, world, pos.getX(), pos.getY(), pos.getZ());

            return true;
        }

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return null;
    }
}
