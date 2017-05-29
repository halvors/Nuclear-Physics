package org.halvors.atomicscience.old.schematic;

import atomicscience.AtomicScience;
import calclavia.lib.schematic.Schematic;
import calclavia.lib.type.Pair;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.vector.Vector3;

public class SchematicBreedingReactor
        extends Schematic
{
    public String getName()
    {
        return "schematic.breedingReactor.name";
    }

    public HashMap<Vector3, Pair<Integer, Integer>> getStructure(ForgeDirection dir, int size)
    {
        HashMap<Vector3, Pair<Integer, Integer>> returnMap = new HashMap();

        int r = Math.max(size, 2);
        for (int x = -r; x <= r; x++) {
            for (int z = -r; z <= r; z++) {
                returnMap.put(new Vector3(x, 0.0D, z), new Pair(Integer.valueOf(Block.field_71943_B.field_71990_ca), Integer.valueOf(0)));
            }
        }
        r--;
        for (int x = -r; x <= r; x++) {
            for (int z = -r; z <= r; z++)
            {
                Vector3 targetPosition = new Vector3(x, 1.0D, z);
                if (new Vector3(x, 0.0D, z).getMagnitude() <= 2.0D) {
                    if (((x != -r) && (x != r)) || ((z != -r) && (z != r)))
                    {
                        returnMap.put(new Vector3(x, 0.0D, z), new Pair(Integer.valueOf(AtomicScience.blockReactorCell.field_71990_ca), Integer.valueOf(0)));
                        returnMap.put(new Vector3(x, -1.0D, z), new Pair(Integer.valueOf(AtomicScience.blockThermometer.field_71990_ca), Integer.valueOf(0)));
                        returnMap.put(new Vector3(x, -3.0D, z), new Pair(Integer.valueOf(AtomicScience.blockSiren.field_71990_ca), Integer.valueOf(0)));
                        returnMap.put(new Vector3(x, -2.0D, z), new Pair(Integer.valueOf(Block.field_72075_av.field_71990_ca), Integer.valueOf(0)));
                    }
                    else
                    {
                        returnMap.put(new Vector3(x, -1.0D, z), new Pair(Integer.valueOf(AtomicScience.blockControlRod.field_71990_ca), Integer.valueOf(0)));
                        returnMap.put(new Vector3(x, -2.0D, z), new Pair(Integer.valueOf(Block.field_71956_V.field_71990_ca), Integer.valueOf(1)));
                    }
                }
            }
        }
        returnMap.put(new Vector3(0.0D, -2.0D, 0.0D), new Pair(Integer.valueOf(Block.field_71981_t.field_71990_ca), Integer.valueOf(0)));
        returnMap.put(new Vector3(0.0D, -3.0D, 0.0D), new Pair(Integer.valueOf(Block.field_71981_t.field_71990_ca), Integer.valueOf(0)));
        returnMap.put(new Vector3(), new Pair(Integer.valueOf(AtomicScience.blockReactorCell.field_71990_ca), Integer.valueOf(0)));
        return returnMap;
    }
}
