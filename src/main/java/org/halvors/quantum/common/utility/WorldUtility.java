package org.halvors.quantum.common.utility;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.transform.vector.Vector3;

import java.util.ArrayList;
import java.util.List;

public class WorldUtility {
    public static void rotateVectorFromDirection(Vector3 position, ForgeDirection direction) {
        switch (direction) {
            case UP:
                position.rotate(180, Vector3.EAST());
                break;

            case NORTH:
                position.rotate(90, Vector3.WEST());
                break;

            case SOUTH:
                position.rotate(90, Vector3.EAST());
                break;

            case EAST:
                position.rotate(90, Vector3.SOUTH());
                break;

            case WEST:
                position.rotate(90, Vector3.NORTH());
                break;
        }
    }

    public static int getAngleFromForgeDirection(ForgeDirection direction) {
        switch (direction) {
            case NORTH:
                return 90;

            case SOUTH:
                return -90;

            case EAST:
                return 0;

            case WEST:
                return -180;
        }

        return 0;
    }

    public static ForgeDirection invertX(ForgeDirection direction) {
        switch (direction) {
            case NORTH:
                return ForgeDirection.SOUTH;

            case SOUTH:
                return ForgeDirection.NORTH;
        }

        return direction;
    }

    public static ForgeDirection invertY(ForgeDirection direction) {
        switch (direction) {
            case UP:
                return ForgeDirection.DOWN;
            case DOWN:
                return ForgeDirection.UP;
        }

        return direction;
    }

    public static ForgeDirection invertZ(ForgeDirection direction) {
        switch (direction) {
            case EAST:
                return ForgeDirection.WEST;

            case WEST:
                return ForgeDirection.EAST;
        }

        return direction;
    }

    /**
     * Used to find all tileEntities sounding the location you will have to filter for selective
     * tileEntities
     *
     * @param world - the world being searched threw
     * @param x
     * @param y
     * @param z
     * @return an array of up to 6 tileEntities */
    public static TileEntity[] getSurroundingTileEntities(TileEntity tile) {
        return getSurroundingTileEntities(tile.getWorld(), tile.xCoord, tile.yCoord, tile.zCoord);
    }

    public static TileEntity[] getSurroundingTileEntities(World world, Vector3 position) {
        return getSurroundingTileEntities(world, position.intX(), position.intY(), position.intZ());
    }

    public static TileEntity[] getSurroundingTileEntities(World world, int x, int y, int z) {
        TileEntity[] list = new TileEntity[6];

        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            list[direction.ordinal()] = world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
        }

        return list;
    }

    /**
     * Used to find which of 4 Corners this block is in a group of blocks 0 = not a corner 1-4 = a
     * corner of some direction.
     */
    public static int corner(TileEntity entity) {
        TileEntity[] en = getSurroundingTileEntities(entity.getWorld(), entity.xCoord, entity.yCoord, entity.zCoord);
        TileEntity north = en[ForgeDirection.NORTH.ordinal()];
        TileEntity south = en[ForgeDirection.SOUTH.ordinal()];
        TileEntity east = en[ForgeDirection.EAST.ordinal()];
        TileEntity west = en[ForgeDirection.WEST.ordinal()];

        if (west != null && north != null && east == null && south == null) {
            return 3;
        }

        if (north != null && east != null && south == null && west == null) {
            return 4;
        }

        if (east != null && south != null && west == null && north == null) {
            return 1;
        }

        if (south != null && west != null && north == null && east == null) {
            return 2;
        }

        return 0;
    }

    /** gets all EntityItems in a location using a start and end point */
    public static List<EntityItem> findAllItemsIn(World world, Vector3 startPosition, Vector3 endPosition) {
        return world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(startPosition.x, startPosition.y, startPosition.z, endPosition.x, endPosition.y, endPosition.z));
    }

    public static List<EntityItem> getEntitiesInDirection(World world, Vector3 center, ForgeDirection direction) {
        List<EntityItem> list = world.selectEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(center.x + direction.offsetX, center.y + direction.offsetY, center.z + direction.offsetZ, center.x + direction.offsetX + 1, center.y + direction.offsetY + 1, center.z + direction.offsetZ + 1), IEntitySelector.selectAnything);

        return list.size() > 0 ? list : null;
    }

    /** Gets all EntityItems in an area and sorts them by a list of itemStacks
     *
     * @param world - world being worked in
     * @param start - start point
     * @param end - end point
     * @param disiredItems - list of item that are being looked for
     * @return a list of EntityItem that match the itemStacks desired */
    public static List<EntityItem> findSelectItems(World world, Vector3 start, Vector3 end, List<ItemStack> disiredItems) {
        List<EntityItem> entityItems = findAllItemsIn(world, start, end);

        return filterEntityItemsList(entityItems, disiredItems);
    }

    /** filters an EntityItem List to a List of Items */
    public static List<EntityItem> filterEntityItemsList(List<EntityItem> entityItems, List<ItemStack> disiredItems) {
        List<EntityItem> newItemList = new ArrayList<>();

        for (ItemStack itemStack : disiredItems) {
            for (EntityItem entityItem : entityItems) {
                if (entityItem.getEntityItem().isItemEqual(itemStack) && !newItemList.contains(entityItem)) {
                    newItemList.add(entityItem);

                    break;
                }
            }
        }
        return newItemList;
    }

    /** filters out EnittyItems from an Entity list */
    public static List<EntityItem> filterOutEntityItems(List<Entity> entities) {
        List<EntityItem> newEntityList = new ArrayList<>();

        for (Entity entity : entities) {
            if (entity instanceof EntityItem) {
                newEntityList.add((EntityItem) entity);
            }

        }

        return newEntityList;
    }

    /** filter a list of itemStack to another list of itemStacks
     *
     * @param totalItems - full list of items being filtered
     * @param desiredItems - list the of item that are being filtered too
     * @return a list of item from the original that are wanted */
    public static List<ItemStack> filterItems(List<ItemStack> totalItems, List<ItemStack> desiredItems) {
        List<ItemStack> newItemList = new ArrayList<>();

        for (ItemStack entityItem : totalItems) {
            for (ItemStack itemStack : desiredItems) {
                if (entityItem == itemStack && entityItem.getMetadata() == itemStack.getMetadata() && !newItemList.contains(entityItem)) {
                    newItemList.add(entityItem);

                    break;
                }
            }
        }

        return newItemList;
    }

    /** Checks based on a bitmap for sides if this side can be rendered.
     *
     * @return True if so. */
    public static boolean isEnabledSide(byte sideMap, ForgeDirection direction) {
        return (sideMap & (1 << direction.ordinal())) != 0;
    }

    public static byte setEnableSide(byte sideMap, ForgeDirection direction, boolean doEnable) {
        if (doEnable) {
            sideMap = (byte) (sideMap | (1 << direction.ordinal()));
        } else {
            sideMap = (byte) (sideMap & ~(1 << direction.ordinal()));
        }

        return sideMap;
    }
}