package org.halvors.quantum.common.utility;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import java.util.ArrayList;
import java.util.List;

public class WorldUtility {
    public static void rotateVectorFromDirection(Vector3 position, EnumFacing direction) {
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

    public static int getAngleFromForgeDirection(EnumFacing direction) {
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

    public static EnumFacing invertX(EnumFacing direction) {
        switch (direction) {
            case NORTH:
                return EnumFacing.SOUTH;

            case SOUTH:
                return EnumFacing.NORTH;
        }

        return direction;
    }

    public static EnumFacing invertY(EnumFacing direction) {
        switch (direction) {
            case UP:
                return EnumFacing.DOWN;

            case DOWN:
                return EnumFacing.UP;
        }

        return direction;
    }

    public static EnumFacing invertZ(EnumFacing direction) {
        switch (direction) {
            case EAST:
                return EnumFacing.WEST;

            case WEST:
                return EnumFacing.EAST;
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
        return getSurroundingTileEntities(tile.getWorld(), tile.getPos());
    }

    public static TileEntity[] getSurroundingTileEntities(World world, Vector3 position) {
        return getSurroundingTileEntities(world, new BlockPos(position.intX(), position.intY(), position.intZ()));
    }

    public static TileEntity[] getSurroundingTileEntities(World world, BlockPos pos) {
        TileEntity[] list = new TileEntity[6];

        for (EnumFacing direction : EnumFacing.VALUES) {
            list[direction.ordinal()] = world.getTileEntity(new BlockPos(pos.getX() + direction.getFrontOffsetX(), pos.getY() + direction.getFrontOffsetY(), pos.getZ() + direction.getFrontOffsetZ()));
        }

        return list;
    }

    /**
     * Used to find which of 4 Corners this block is in a group of blocks 0 = not a corner 1-4 = a
     * corner of some direction.
     */
    public static int corner(TileEntity entity) {
        TileEntity[] en = getSurroundingTileEntities(entity.getWorld(), entity.getPos());
        TileEntity north = en[EnumFacing.NORTH.ordinal()];
        TileEntity south = en[EnumFacing.SOUTH.ordinal()];
        TileEntity east = en[EnumFacing.EAST.ordinal()];
        TileEntity west = en[EnumFacing.WEST.ordinal()];

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
        return world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(startPosition.x, startPosition.y, startPosition.z, endPosition.x, endPosition.y, endPosition.z));
    }

    // TODO: Fix this.
    /*
    public static List<EntityItem> getEntitiesInDirection(World world, Vector3 center, EnumFacing direction) {
        List<EntityItem> list = world.selectEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(center.x + direction.getFrontOffsetX(), center.y + direction.getFrontOffsetY(), center.z + direction.getFrontOffsetZ(), center.x + direction.getFrontOffsetX() + 1, center.y + direction.getFrontOffsetY() + 1, center.z + direction.getFrontOffsetZ() + 1), IEntitySelector.selectAnything);

        return list.size() > 0 ? list : null;
    }
    */

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
    public static boolean isEnabledSide(byte sideMap, EnumFacing direction) {
        return (sideMap & (1 << direction.ordinal())) != 0;
    }

    public static byte setEnableSide(byte sideMap, EnumFacing direction, boolean doEnable) {
        if (doEnable) {
            sideMap = (byte) (sideMap | (1 << direction.ordinal()));
        } else {
            sideMap = (byte) (sideMap & ~(1 << direction.ordinal()));
        }

        return sideMap;
    }
}