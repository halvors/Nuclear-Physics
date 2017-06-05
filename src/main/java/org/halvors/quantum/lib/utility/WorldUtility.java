package org.halvors.quantum.lib.utility;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.transform.vector.Vector3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WorldUtility {
    public static void rotateVectorFromDirection(Vector3 vec, ForgeDirection dir) {
        switch (dir) {
            default:
                break;
            case UP:
                vec.rotate(180.0F, Vector3.EAST());
                break;
            case NORTH:
                vec.rotate(90.0F, Vector3.WEST());
                break;
            case SOUTH:
                vec.rotate(90.0F, Vector3.EAST());
                break;
            case WEST:
                vec.rotate(90.0F, Vector3.NORTH());
                break;
            case EAST:
                vec.rotate(90.0F, Vector3.SOUTH());
        }
    }

    public static int getAngleFromForgeDirection(ForgeDirection dir) {
        switch (dir) {
            default:
                break;
            case NORTH:
                return 90;
            case SOUTH:
                return -90;
            case WEST:
                return 65356;
            case EAST:
                return 0;
        }

        return 0;
    }

    public static ForgeDirection invertX(ForgeDirection dir) {
        switch (dir) {
            case NORTH:
                return ForgeDirection.SOUTH;
            case SOUTH:
                return ForgeDirection.NORTH;
        }

        return dir;
    }

    public static ForgeDirection invertY(ForgeDirection dir) {
        switch (dir) {
            case UP:
                return ForgeDirection.DOWN;
            case DOWN:
                return ForgeDirection.UP;
        }

        return dir;
    }

    public static ForgeDirection invertZ(ForgeDirection dir) {
        switch (dir) {
            case WEST:
                return ForgeDirection.EAST;
            case EAST:
                return ForgeDirection.WEST;
        }

        return dir;
    }

    public static TileEntity[] getSurroundingTileEntities(TileEntity ent) {
        return getSurroundingTileEntities(ent.getWorld(), ent.xCoord, ent.yCoord, ent.zCoord);
    }

    public static TileEntity[] getSurroundingTileEntities(World world, Vector3 vec) {
        return getSurroundingTileEntities(world, vec.intX(), vec.intY(), vec.intZ());
    }

    public static TileEntity[] getSurroundingTileEntities(World world, int x, int y, int z) {
        TileEntity[] list = new TileEntity[6];
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            list[direction.ordinal()] = world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
        }
        return list;
    }

    public static int corner(TileEntity entity) {
        TileEntity[] en = getSurroundingTileEntities(entity.getWorld(), entity.xCoord, entity.yCoord, entity.zCoord);
        TileEntity north = en[ForgeDirection.NORTH.ordinal()];
        TileEntity south = en[ForgeDirection.SOUTH.ordinal()];
        TileEntity east = en[ForgeDirection.EAST.ordinal()];
        TileEntity west = en[ForgeDirection.WEST.ordinal()];

        if ((west != null) && (north != null) && (east == null) && (south == null)) {
            return 3;
        }

        if ((north != null) && (east != null) && (south == null) && (west == null)) {
            return 4;
        }

        if ((east != null) && (south != null) && (west == null) && (north == null)) {
            return 1;
        }

        if ((south != null) && (west != null) && (north == null) && (east == null)) {
            return 2;
        }

        return 0;
    }

    public static List<EntityItem> findAllItemsIn(World world, Vector3 start, Vector3 end) {
        return world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(start.x, start.y, start.z, end.x, end.y, end.z));
    }

    public static List<EntityItem> getEntitiesInDirection(World world, Vector3 center, ForgeDirection dir) {
        List<EntityItem> list = world.selectEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(center.x + dir.offsetX, center.y + dir.offsetY, center.z + dir.offsetZ, center.x + dir.offsetX + 1.0D, center.y + dir.offsetY + 1.0D, center.z + dir.offsetZ + 1.0D), IEntitySelector.selectAnything);

        return list.size() > 0 ? list : null;
    }

    public static List<EntityItem> findSelectItems(World world, Vector3 start, Vector3 end, List<ItemStack> disiredItems) {
        List<EntityItem> entityItems = findAllItemsIn(world, start, end);
        return filterEntityItemsList(entityItems, disiredItems);
    }

    public static List<EntityItem> filterEntityItemsList(List<EntityItem> entityItems, List<ItemStack> disiredItems) {
        List<EntityItem> newItemList = new ArrayList<>();
        ItemStack itemStack;

        for (Iterator it = disiredItems.iterator(); it.hasNext();) {
            itemStack = (ItemStack) it.next();

            for (EntityItem entityItem : entityItems) {
                if ((entityItem.getEntityItem().isItemEqual(itemStack)) && (!newItemList.contains(entityItem))) {
                    newItemList.add(entityItem);
                    break;
                }
            }
        }

        return newItemList;
    }

    public static List<EntityItem> filterOutEntityItems(List<Entity> entities) {
        List<EntityItem> newEntityList = new ArrayList<>();

        for (Entity entity : entities) {
            if ((entity instanceof EntityItem)) {
                newEntityList.add((EntityItem)entity);
            }
        }

        return newEntityList;
    }

    public static List<ItemStack> filterItems(List<ItemStack> totalItems, List<ItemStack> desiredItems) {
        List<ItemStack> newItemList = new ArrayList<>();
        ItemStack entityItem;

        for (Iterator it = totalItems.iterator(); it.hasNext();) {
            entityItem = (ItemStack) it.next();

            for (ItemStack itemStack : desiredItems) {
                if ((entityItem == itemStack) && (entityItem.getMetadata() == itemStack.getMetadata()) && (!newItemList.contains(entityItem))) {
                    newItemList.add(entityItem);
                    break;
                }
            }
        }

        return newItemList;
    }

    public static void replaceTileEntity(Class<? extends TileEntity> findTile, Class<? extends TileEntity> replaceTile) {
        try {
            Map<String, Class> nameToClassMap = ObfuscationReflectionHelper.getPrivateValue(TileEntity.class, null, new String[] { "field_70326_a", "nameToClassMap", "a" });
            Map<Class, String> classToNameMap = ObfuscationReflectionHelper.getPrivateValue(TileEntity.class, null, new String[] { "field_70326_b", "classToNameMap", "b" });

            String findTileID = classToNameMap.get(findTile);

            if (findTileID != null) {
                nameToClassMap.put(findTileID, replaceTile);
                classToNameMap.put(replaceTile, findTileID);
                classToNameMap.remove(findTile);

                Quantum.getLogger().info("Replaced TileEntity: " + findTile);
            } else {
                Quantum.getLogger().warn("Failed to replace TileEntity: " + findTile);
            }
        } catch (Exception e) {
            Quantum.getLogger().warn("Failed to replace TileEntity: " + findTile);
            e.printStackTrace();
        }
    }

    public static boolean isEnabledSide(byte sideMap, ForgeDirection direction) {
        return (sideMap & 1 << direction.ordinal()) != 0;
    }

    public static byte setEnableSide(byte sideMap, ForgeDirection direction, boolean doEnable) {
        if (doEnable) {
            sideMap = (byte)(sideMap | 1 << direction.ordinal());
        } else {
            sideMap = (byte)(sideMap & (~1 << direction.ordinal()));
        }

        return sideMap;
    }

    public static List<ItemStack> getItemStackFromBlock(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);

        if (block == null) {
            return null;
        }

        if (block.isAir(world, x, y, z)) {
            return null;
        }

        int metadata = world.getBlockMetadata(x, y, z);
        ArrayList<ItemStack> dropsList = block.getDrops(world, x, y, z, metadata, 0);
        float dropChance = ForgeEventFactory.fireBlockHarvesting(dropsList, world, block, x, y, z, metadata, 0, 1.0F, false, MachinePlayer.get(world));
        ArrayList<ItemStack> returnList = new ArrayList<>();

        for (ItemStack itemStack : dropsList) {
            if (world.rand.nextFloat() <= dropChance) {
                returnList.add(itemStack);
            }
        }

        return returnList;
    }
}
