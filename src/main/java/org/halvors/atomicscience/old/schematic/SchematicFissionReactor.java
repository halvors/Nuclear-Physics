package org.halvors.atomicscience.old.schematic;

import atomicscience.AtomicScience;
import calclavia.lib.schematic.Schematic;
import calclavia.lib.type.Pair;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.vector.Vector3;

public class SchematicFissionReactor
        extends Schematic
{
    public String getName()
    {
        return "schematic.fissionReactor.name";
    }

    public HashMap<Vector3, Pair<Integer, Integer>> getStructure(ForgeDirection dir, int size)
    {
        HashMap<Vector3, Pair<Integer, Integer>> returnMap = new HashMap();
        if (size <= 1)
        {
            int r = 2;
            for (int x = -r; x <= r; x++) {
                for (int z = -r; z <= r; z++)
                {
                    Vector3 targetPosition = new Vector3(x, 0.0D, z);
                    returnMap.put(targetPosition, new Pair(Integer.valueOf(Block.field_71943_B.field_71990_ca), Integer.valueOf(0)));
                }
            }
            r--;
            for (int x = -r; x <= r; x++) {
                for (int z = -r; z <= r; z++)
                {
                    Vector3 targetPosition = new Vector3(x, 1.0D, z);
                    returnMap.put(targetPosition, new Pair(Integer.valueOf(AtomicScience.blockElectricTurbine.field_71990_ca), Integer.valueOf(0)));
                    if (((x != -r) && (x != r)) || ((z != -r) && (z != r) && (new Vector3(x, 0.0D, z).getMagnitude() <= 1.0D)))
                    {
                        returnMap.put(new Vector3(x, -1.0D, z), new Pair(Integer.valueOf(AtomicScience.blockControlRod.field_71990_ca), Integer.valueOf(0)));
                        returnMap.put(new Vector3(x, -2.0D, z), new Pair(Integer.valueOf(Block.field_71956_V.field_71990_ca), Integer.valueOf(1)));
                    }
                }
            }
            returnMap.put(new Vector3(0.0D, -1.0D, 0.0D), new Pair(Integer.valueOf(AtomicScience.blockThermometer.field_71990_ca), Integer.valueOf(0)));

            returnMap.put(new Vector3(0.0D, -3.0D, 0.0D), new Pair(Integer.valueOf(AtomicScience.blockSiren.field_71990_ca), Integer.valueOf(0)));
            returnMap.put(new Vector3(0.0D, -2.0D, 0.0D), new Pair(Integer.valueOf(Block.field_72075_av.field_71990_ca), Integer.valueOf(0)));
            returnMap.put(new Vector3(), new Pair(Integer.valueOf(AtomicScience.blockReactorCell.field_71990_ca), Integer.valueOf(0)));
        }
        else
        {
            int r = 2;
            for (int y = 0; y < size; y++) {
                for (int x = -r; x <= r; x++) {
                    for (int z = -r; z <= r; z++)
                    {
                        Vector3 targetPosition = new Vector3(x, y, z);
                        Vector3 leveledPosition = new Vector3(0.0D, y, 0.0D);
                        if (y < size - 1)
                        {
                            if (targetPosition.distance(leveledPosition) == 2.0D)
                            {
                                returnMap.put(targetPosition, new Pair(Integer.valueOf(AtomicScience.blockControlRod.field_71990_ca), Integer.valueOf(0)));

                                int rotationMetadata = 0;
                                Vector3 offset = new Vector3(x, 0.0D, z).normalize();
                                for (ForgeDirection checkDir : ForgeDirection.VALID_DIRECTIONS) {
                                    if ((offset.x == checkDir.offsetX) && (offset.y == checkDir.offsetY) && (offset.z == checkDir.offsetZ))
                                    {
                                        rotationMetadata = checkDir.getOpposite().ordinal();
                                        break;
                                    }
                                }
                                returnMap.put(targetPosition.clone().translate(offset), new Pair(Integer.valueOf(Block.field_71956_V.field_71990_ca), Integer.valueOf(rotationMetadata)));
                            }
                            else if ((x == -r) || (x == r) || (z == -r) || (z == r))
                            {
                                returnMap.put(targetPosition, new Pair(Integer.valueOf(Block.field_71946_M.field_71990_ca), Integer.valueOf(0)));
                            }
                            else if ((x == 0) && (z == 0))
                            {
                                returnMap.put(targetPosition, new Pair(Integer.valueOf(AtomicScience.blockReactorCell.field_71990_ca), Integer.valueOf(0)));
                            }
                            else
                            {
                                returnMap.put(targetPosition, new Pair(Integer.valueOf(Block.field_71942_A.field_71990_ca), Integer.valueOf(0)));
                            }
                        }
                        else if (targetPosition.distance(leveledPosition) < 2.0D) {
                            returnMap.put(targetPosition, new Pair(Integer.valueOf(AtomicScience.blockElectricTurbine.field_71990_ca), Integer.valueOf(0)));
                        }
                    }
                }
            }
        }
        return returnMap;
    }
}
