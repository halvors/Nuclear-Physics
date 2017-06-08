package org.halvors.quantum.lib.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.transform.vector.Cuboid;
import org.halvors.quantum.common.transform.vector.Vector2;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.common.transform.vector.VectorWorld;
import org.halvors.quantum.common.utility.LanguageUtility;
import org.halvors.quantum.lib.IRotatable;
import org.halvors.quantum.lib.prefab.item.ItemBlockTooltip;
import org.halvors.quantum.lib.utility.WrenchUtility;

public abstract class TileBlock extends TileEntity {
    public final String name;
    public final Material material;
    public Class<? extends ItemBlock> itemBlock = ItemBlockTooltip.class;
    public CreativeTabs creativeTab = null;
    public Cuboid bounds = Cuboid.full();
    public BlockDummy block;
    public float blockHardness = 1.0F;
    public float blockResistance = 1.0F;
    public boolean canProvidePower = false;
    public boolean tickRandomly = false;
    public boolean normalRender = true;
    public boolean forceStandardRender = false;
    public boolean customItemRender = false;
    public boolean isOpaqueCube = true;
    public IBlockAccess access;
    protected String textureName;
    protected String domain;
    protected byte rotationMask = Byte.parseByte("111100", 2);
    protected boolean isFlipPlacement = false;

    public TileBlock(String newName, Material newMaterial) {
        this.name = newName;
        this.material = newMaterial;
        this.textureName = this.name;
    }

    public TileBlock(Material newMaterial) {
        this.name = LanguageUtility.decapitalizeFirst(getClass().getSimpleName().replaceFirst("Tile", ""));
        this.material = newMaterial;
        this.textureName = this.name;
    }

    public static Vector2 getClickedFace(byte hitSide, float hitX, float hitY, float hitZ) {
        switch (hitSide) {
            case 0:
                return new Vector2(1.0F - hitX, hitZ);

            case 1:
                return new Vector2(hitX, hitZ);

            case 2:
                return new Vector2(1.0F - hitX, 1.0F - hitY);

            case 3:
                return new Vector2(hitX, 1.0F - hitY);

            case 4:
                return new Vector2(hitZ, 1.0F - hitY);

            case 5:
                return new Vector2(1.0F - hitZ, 1.0F - hitY);
        }

        return new Vector2(0.5D, 0.5D);
    }

    public void onInstantiate() {

    }

    public World world() {
        return this.worldObj;
    }

    public IBlockAccess access() {
        if (world() != null) {
            return world();
        }

        return access;
    }

    public int x() {
        assert world() != null : ("TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.");

        return xCoord;
    }

    public int y() {
        assert world() != null : ("TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.");

        return yCoord;
    }

    public int z() {
        assert world() != null : ("TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.");

        return zCoord;
    }

    public VectorWorld position()
    {
        assert (world() != null) : ("TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.");
        return new VectorWorld(this);
    }

    protected VectorWorld center() {
        assert (world() != null) : ("TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.");

        return (VectorWorld)position().translate(0.5D);
    }

    @Override
    public Block getBlockType() {
        if (access() != null) {
            Block accessBlock = access().getBlock(xCoord, yCoord, zCoord);

            if (accessBlock == null) {
                return block;
            }

            return accessBlock;
        }

        return block;
    }

    public TileBlock tile() {
        return null;
    }

    public int metadata() {
        return access().getBlockMetadata(x(), y(), z());
    }

    public ArrayList<ItemStack> getDrops(int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();

        if (getBlockType() != null) {
            drops.add(new ItemStack(getBlockType(), quantityDropped(metadata, fortune), metadataDropped(metadata, fortune)));
        }

        return drops;
    }

    public int quantityDropped(int meta, int fortune) {
        return 1;
    }

    public int metadataDropped(int meta, int fortune) {
        return 0;
    }

    public boolean isControlDown(EntityPlayer player) {
        try {
            Class ckm = Class.forName("codechicken.multipart.ControlKeyModifer");
            Method m = ckm.getMethod("isControlDown", EntityPlayer.class);

            return ((Boolean)m.invoke(null, new Object[] { player })).booleanValue();
        } catch (Exception e) {

        }

        return false;
    }

    public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
        list.add(new ItemStack(item, 1, 0));
    }

    public ItemStack getPickBlock(MovingObjectPosition target) {
        return new ItemStack(getBlockType(), 1, metadataDropped(metadata(), 0));
    }

    public int getLightValue(IBlockAccess access) {
        Block block = access.getBlock(x(), y(), z());

        return block.getLightValue();
    }

    public void click(EntityPlayer player) {}

    public boolean activate(EntityPlayer player, int side, Vector3 hit) {
        if (WrenchUtility.isUsableWrench(player, player.inventory.getCurrentItem(), x(), y(), z())) {
            if (configure(player, side, hit)) {
                WrenchUtility.damageWrench(player, player.inventory.getCurrentItem(), x(), y(), z());
                return true;
            }

            return false;
        }

        return use(player, side, hit);
    }

    protected boolean use(EntityPlayer player, int side, Vector3 hit) {
        return false;
    }

    protected boolean configure(EntityPlayer player, int side, Vector3 hit) {
        return tryRotate(side, hit);
    }

    protected boolean tryRotate(int side, Vector3 hit) {
        if (this instanceof IRotatable) {
            byte result = getSideToRotate((byte) side, hit.x, hit.y, hit.z);

            if (result != -1) {
                setDirection(ForgeDirection.getOrientation(result));

                return true;
            }
        }
        return false;
    }

    public byte getSideToRotate(byte hitSide, double hitX, double hitY, double hitZ) {
        byte tBack = (byte)(hitSide ^ 0x1);

        switch (hitSide) {
            case 0:
            case 1:
                if (hitX < 0.25D) {
                    if (hitZ < 0.25D) {
                        if (canRotate(tBack)) {
                            return tBack;
                        }
                    }

                    if (hitZ > 0.75D) {
                        if (canRotate(tBack)) {
                            return tBack;
                        }
                    }

                    if (canRotate(4)) {
                        return 4;
                    }
                }

                if (hitX > 0.75D) {
                    if (hitZ < 0.25D) {
                        if (canRotate(tBack)) {
                            return tBack;
                        }
                    }

                    if (hitZ > 0.75D) {
                        if (canRotate(tBack)) {
                            return tBack;
                        }
                    }

                    if (canRotate(5)) {
                        return 5;
                    }
                }

                if (hitZ < 0.25D) {
                    if (canRotate(2)) {
                        return 2;
                    }
                }

                if (hitZ > 0.75D) {
                    if (canRotate(3)) {
                        return 3;
                    }
                }

                if (canRotate(hitSide)) {
                    return hitSide;
                }

            case 2:
            case 3:
                if (hitX < 0.25D) {
                    if (hitY < 0.25D) {
                        if (canRotate(tBack)) {
                            return tBack;
                        }
                    }

                    if (hitY > 0.75D) {
                        if (canRotate(tBack)) {
                            return tBack;
                        }
                    }

                    if (canRotate(4)) {
                        return 4;
                    }
                }

                if (hitX > 0.75D) {
                    if (hitY < 0.25D) {
                        if (canRotate(tBack)) {
                            return tBack;
                        }
                    }

                    if (hitY > 0.75D) {
                        if (canRotate(tBack)) {
                            return tBack;
                        }
                    }

                    if (canRotate(5)) {
                        return 5;
                    }
                }

                if (hitY < 0.25D) {
                    if (canRotate(0)) {
                        return 0;
                    }
                }

                if (hitY > 0.75D) {
                    if (canRotate(1)) {
                        return 1;
                    }
                }

                if (canRotate(hitSide)) {
                    return hitSide;
                }

            case 4:
            case 5:
                if (hitZ < 0.25D) {
                    if (hitY < 0.25D) {
                        if (canRotate(tBack)) {
                            return tBack;
                        }
                    }

                    if (hitY > 0.75D) {
                        if (canRotate(tBack)) {
                            return tBack;
                        }
                    }

                    if (canRotate(2)) {
                        return 2;
                    }
                }

                if (hitZ > 0.75D) {
                    if (hitY < 0.25D) {
                        if (canRotate(tBack)) {
                            return tBack;
                        }
                    }

                    if (hitY > 0.75D) {
                        if (canRotate(tBack)) {
                            return tBack;
                        }
                    }

                    if (canRotate(3)) {
                        return 3;
                    }
                }

                if (hitY < 0.25D) {
                    if (canRotate(0)) {
                        return 0;
                    }
                }

                if (hitY > 0.75D) {
                    if (canRotate(1)) {
                        return 1;
                    }
                }

                if (canRotate(hitSide)) {
                    return hitSide;
                }

                break;
        }

        return -1;
    }

    public ForgeDirection determineOrientation(EntityLivingBase entityLiving) {
        if (MathHelper.abs((float) entityLiving.posX - x()) < 2.0F && MathHelper.abs((float) entityLiving.posZ - z()) < 2.0F) {
            double d0 = entityLiving.posY + 1.82D - entityLiving.yOffset;

            if (canRotate(1) && d0 - y() > 2.0D) {
                return ForgeDirection.UP;
            }

            if (canRotate(0) && y() - d0 > 0.0D) {
                return ForgeDirection.DOWN;
            }
        }

        int playerSide = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3;
        int returnSide = playerSide == 3 && canRotate(4) ? 4 : playerSide == 2 && canRotate(3) ? 3 : playerSide == 1 && canRotate(5) ? 5 : playerSide == 0 && canRotate(2) ? 2 : 0;

        if (isFlipPlacement) {
            return ForgeDirection.getOrientation(returnSide).getOpposite();
        }

        return ForgeDirection.getOrientation(returnSide);
    }

    public boolean canRotate(int ord) {
        return (rotationMask & 1 << ord) != 0;
    }

    public ForgeDirection getDirection() {
        return ForgeDirection.getOrientation(metadata());
    }

    public void setDirection(ForgeDirection direction) {
        world().setBlockMetadataWithNotify(x(), y(), z(), direction.ordinal(), 3);
    }

    protected void onAdded() {
        onWorldJoin();
    }

    public void onPlaced(EntityLivingBase entityLiving, ItemStack itemStack) {
        this.setDirection(determineOrientation(entityLiving));
    }

    public void onRemove(Block block, int metadata) {
        onWorldSeparate();
    }

    protected void onWorldJoin() {

    }

    protected void onWorldSeparate() {

    }

    protected void onNeighborChanged() {

    }

    protected void notifyChange() {
        world().notifyBlocksOfNeighborChange(x(), y(), z(), getBlockType());
    }

    protected void markRender() {
        world().markBlockForRenderUpdate(x(), y(), z());
    }

    protected void markUpdate() {
        world().markBlockForUpdate(x(), y(), z());
    }

    protected void updateLight() {
        world().updateAllLightTypes(x(), y(), z());
    }

    protected void scheduelTick(int delay) {
        world().scheduleBlockUpdate(x(), y(), z(), getBlockType(), delay);
    }

    public void collide(Entity entity) {

    }

    public Iterable<Cuboid> getCollisionBoxes(Cuboid intersect, Entity entity) {
        List<Cuboid> boxes = new ArrayList<>();

        for (Cuboid cuboid : getCollisionBoxes()) {
            if (intersect != null && cuboid.intersects(intersect)) {
                boxes.add(cuboid);
            }
        }

        return boxes;
    }

    public Iterable<Cuboid> getCollisionBoxes() {
        return Arrays.asList(bounds);
    }

    public Cuboid getSelectBounds() {
        return bounds;
    }

    public Cuboid getCollisionBounds() {
        return bounds;
    }

    @SideOnly(Side.CLIENT)
    public final TileRender getRenderer() {
        if (!RenderInfo.renderer.containsKey(this)) {
            RenderInfo.renderer.put(this, newRenderer());
        }

        return RenderInfo.renderer.get(this);
    }

    @SideOnly(Side.CLIENT)
    protected TileRender newRenderer() {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess access, int side) {
        return getIcon(side, access.getBlockMetadata(x(), y(), z()));
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {
        return getIcon();
    }

    public IIcon getIcon()
    {
        return RenderInfo.icon.get(getTextureName());
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        RenderInfo.icon.put(getTextureName(), iconRegister.registerIcon(getTextureName()));
    }

    @SideOnly(Side.CLIENT)
    protected String getTextureName() {
        return block.dummyTile.domain + ":" + textureName;
    }

    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side) {
        return side == 0 && this.bounds.min.y > 0.0D;
    }

    public void onFillRain() {}

    public boolean isIndirectlyPowered() {
        return world().isBlockIndirectlyGettingPowered(x(), y(), z());
    }

    public int getStrongestIndirectPower() {
        return world().getStrongestIndirectPower(x(), y(), z());
    }

    public int getWeakRedstonePower(IBlockAccess access, int side) {
        return getStrongRedstonePower(access, side);
    }

    public int getStrongRedstonePower(IBlockAccess access, int side) {
        return 0;
    }

    public boolean isSolid(IBlockAccess access, int side) {
        return material.isSolid();
    }

    public int getRenderBlockPass() {
        return 0;
    }

    public int tickRate(World world) {
        return 20;
    }

    @SideOnly(Side.CLIENT)
    public static class RenderInfo {
        @SideOnly(Side.CLIENT)
        private static final WeakHashMap<TileBlock, TileRender> renderer = new WeakHashMap<>();

        @SideOnly(Side.CLIENT)
        private static final HashMap<String, IIcon> icon = new HashMap<>();
    }

    public interface IComparatorInputOverride {
        int getComparatorInputOverride(int paramInt);
    }
}
