package org.halvors.nuclearphysics.common.block.debug;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.api.schematic.ISchematic;
import org.halvors.nuclearphysics.common.block.BlockBase;
import org.halvors.nuclearphysics.common.block.debug.schematic.SchematicAccelerator;
import org.halvors.nuclearphysics.common.block.debug.schematic.SchematicBreedingReactor;
import org.halvors.nuclearphysics.common.block.debug.schematic.SchematicFissionReactor;
import org.halvors.nuclearphysics.common.block.debug.schematic.SchematicFusionReactor;
import org.halvors.nuclearphysics.common.utility.PlayerUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Automatically set up structures to allow easy debugging in creative mode.
 */
public class BlockCreativeBuilder extends BlockBase {
    private static final List<ISchematic> schematicRegistry = new ArrayList<>();

    static {
        // Add schematics to the creative builder.
        registerSchematic(new SchematicAccelerator());
        registerSchematic(new SchematicBreedingReactor());
        registerSchematic(new SchematicFissionReactor());
        registerSchematic(new SchematicFusionReactor());
    }

    public static void registerSchematic(final ISchematic schematic) {
        schematicRegistry.add(schematic);
    }

    public static ISchematic getSchematic(final int id) {
        return schematicRegistry.get(id);
    }

    public static int getSchematicCount() {
        return schematicRegistry.size();
    }

    public BlockCreativeBuilder() {
        super("creative_builder", Material.IRON);
    }

    // Called when the block is right clicked by the player.
    @Override
    public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final ItemStack itemStack, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (schematicRegistry.size() > 0) {
            PlayerUtility.openGui(player, world, pos);

            return true;
        }

        return false;
    }
}
