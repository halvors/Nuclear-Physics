package org.halvors.quantum.common.debug.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.schematic.ISchematic;
import org.halvors.quantum.lib.prefab.block.BlockRotatable;

import java.util.ArrayList;
import java.util.List;

/*
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
        super(Material.iron);

        rotationMask = Byte.parseByte("111111", 2);

        setUnlocalizedName("creativeBuilder");
        setTextureName(Reference.PREFIX + "creativeBuilder");
        setCreativeTab(Quantum.getCreativeTab());
    }

    /** Called when the block is right clicked by the player */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int facing, float playerX, float playerY, float playerZ) {
        if (schematicRegistry.size() > 0) {
            player.openGui(Quantum.getInstance(), -1, world, x, y, z);

            return true;
        }

        return false;
    }

    @Override
    public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (schematicRegistry.size() > 0) {
            player.openGui(Quantum.getInstance(), -1, world, x, y, z);
            return true;
        }

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int metadata) {
        return null;
    }
}
