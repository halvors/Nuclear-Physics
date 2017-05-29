package org.halvors.atomicscience.old.schematic;

import atomicscience.AtomicScience;
import calclavia.lib.schematic.Schematic;
import calclavia.lib.type.Pair;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.vector.Vector3;

public class SchematicFusionReactor
        extends Schematic
{
    public String getName()
    {
        return "schematic.fusionReactor.name";
    }

    public HashMap<Vector3, Pair<Integer, Integer>> getStructure(ForgeDirection dir, int size)
    {
        HashMap<Vector3, Pair<Integer, Integer>> returnMap = new HashMap();

        int radius = size + 2;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = 0; y <= size; y++)
                {
                    Vector3 position = new Vector3(x, y, z);
                    double magnitude = Math.sqrt(x * x + z * z);
                    if (!returnMap.containsKey(position)) {
                        returnMap.put(position, new Pair(Integer.valueOf(0), Integer.valueOf(0)));
                    }
                    if (magnitude <= radius) {
                        if ((y == 0) || (y == size))
                        {
                            if (magnitude >= 1.0D)
                            {
                                double yDeviation = (y == 0 ? size / 3 : -size / 3) + (y == 0 ? -1 : 1) * Math.sin(magnitude / radius * 3.141592653589793D) * size / 2.0D;
                                Vector3 newPos = position.clone().translate(0.0D, yDeviation, 0.0D);
                                returnMap.put(newPos.round(), new Pair(Integer.valueOf(AtomicScience.blockElectromagnet.field_71990_ca), Integer.valueOf(1)));
                            }
                        }
                        else if (magnitude > radius - 1) {
                            returnMap.put(position, new Pair(Integer.valueOf(AtomicScience.blockElectromagnet.field_71990_ca), Integer.valueOf(0)));
                        }
                    }
                }
            }
        }
        for (int y = 0; y < size; y++)
        {
            returnMap.put(new Vector3(0.0D, y, 0.0D), new Pair(Integer.valueOf(AtomicScience.blockReactorCell.field_71990_ca), Integer.valueOf(0)));
            returnMap.put(new Vector3(1.0D, y, 0.0D), new Pair(Integer.valueOf(AtomicScience.blockElectromagnet.field_71990_ca), Integer.valueOf(0)));
            returnMap.put(new Vector3(0.0D, y, 1.0D), new Pair(Integer.valueOf(AtomicScience.blockElectromagnet.field_71990_ca), Integer.valueOf(0)));
            returnMap.put(new Vector3(0.0D, y, -1.0D), new Pair(Integer.valueOf(AtomicScience.blockElectromagnet.field_71990_ca), Integer.valueOf(0)));
            returnMap.put(new Vector3(-1.0D, y, 0.0D), new Pair(Integer.valueOf(AtomicScience.blockElectromagnet.field_71990_ca), Integer.valueOf(0)));
        }
        returnMap.put(new Vector3(0.0D, 0.0D, 0.0D), new Pair(Integer.valueOf(AtomicScience.blockReactorCell.field_71990_ca), Integer.valueOf(0)));

        return returnMap;
    }
}
