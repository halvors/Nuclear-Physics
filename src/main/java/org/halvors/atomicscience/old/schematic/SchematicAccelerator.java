package org.halvors.atomicscience.old.schematic;

import atomicscience.AtomicScience;
import calclavia.lib.schematic.Schematic;
import calclavia.lib.type.Pair;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.vector.Vector3;

public class SchematicAccelerator
        extends Schematic
{
    public String getName()
    {
        return "schematic.accelerator.name";
    }

    public HashMap<Vector3, Pair<Integer, Integer>> getStructure(ForgeDirection dir, int size)
    {
        HashMap<Vector3, Pair<Integer, Integer>> returnMap = new HashMap();

        int r = size;
        for (int x = -r; x < r; x++) {
            for (int z = -r; z < r; z++) {
                for (int y = -1; y <= 1; y++) {
                    if ((x == -r) || (x == r - 1) || (z == -r) || (z == r - 1)) {
                        returnMap.put(new Vector3(x, y, z), new Pair(Integer.valueOf(AtomicScience.blockElectromagnet.field_71990_ca), Integer.valueOf(0)));
                    }
                }
            }
        }
        r = size - 2;
        for (int x = -r; x < r; x++) {
            for (int z = -r; z < r; z++) {
                for (int y = -1; y <= 1; y++) {
                    if ((x == -r) || (x == r - 1) || (z == -r) || (z == r - 1)) {
                        returnMap.put(new Vector3(x, y, z), new Pair(Integer.valueOf(AtomicScience.blockElectromagnet.field_71990_ca), Integer.valueOf(0)));
                    }
                }
            }
        }
        r = size - 1;
        for (int x = -r; x < r; x++) {
            for (int z = -r; z < r; z++) {
                for (int y = -1; y <= 1; y++) {
                    if ((x == -r) || (x == r - 1) || (z == -r) || (z == r - 1)) {
                        if ((y == -1) || (y == 1)) {
                            returnMap.put(new Vector3(x, y, z), new Pair(Integer.valueOf(AtomicScience.blockElectromagnet.field_71990_ca), Integer.valueOf(1)));
                        } else if (y == 0) {
                            returnMap.put(new Vector3(x, y, z), new Pair(Integer.valueOf(0), Integer.valueOf(0)));
                        }
                    }
                }
            }
        }
        return returnMap;
    }
}
