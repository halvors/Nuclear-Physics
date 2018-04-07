package org.halvors.nuclearphysics.common.block.debug;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.block.BlockBase;
import org.halvors.nuclearphysics.common.block.debug.schematic.*;
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
        super("creative_builder", Material.iron);
    }

    // Called when the block is right clicked by the player.
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (schematicRegistry.size() > 0) {
            PlayerUtility.openGui(player, world, x, y, z);

            return true;
        }

        return false;
    }
}
