package com.calclavia.core.lib.content.module;

package calclavia.lib.content.module;

        import calclavia.lib.prefab.item.ItemBlockTooltip;
        import calclavia.lib.prefab.tile.IRotatable;
        import calclavia.lib.prefab.vector.Cuboid;
        import calclavia.lib.utility.LanguageUtility;
        import calclavia.lib.utility.WrenchUtility;
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
        import net.minecraft.client.renderer.texture.IconRegister;
        import net.minecraft.creativetab.CreativeTabs;
        import net.minecraft.entity.Entity;
        import net.minecraft.entity.EntityLivingBase;
        import net.minecraft.entity.player.EntityPlayer;
        import net.minecraft.entity.player.InventoryPlayer;
        import net.minecraft.item.ItemBlock;
        import net.minecraft.item.ItemStack;
        import net.minecraft.tileentity.TileEntity;
        import net.minecraft.util.Icon;
        import net.minecraft.util.MathHelper;
        import net.minecraft.util.MovingObjectPosition;
        import net.minecraft.world.IBlockAccess;
        import net.minecraft.world.World;
        import net.minecraftforge.common.ForgeDirection;
        import universalelectricity.api.vector.Vector2;
        import universalelectricity.api.vector.Vector3;
        import universalelectricity.api.vector.VectorWorld;

public abstract class TileBlock
        extends TileEntity
{
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

    public TileBlock(String newName, Material newMaterial)
    {
        this.name = newName;
        this.material = newMaterial;
        this.textureName = this.name;
    }

    public TileBlock(Material newMaterial)
    {
        this.name = LanguageUtility.decapitalizeFirst(getClass().getSimpleName().replaceFirst("Tile", ""));
        this.material = newMaterial;
        this.textureName = this.name;
    }

    public static Vector2 getClickedFace(byte hitSide, float hitX, float hitY, float hitZ)
    {
        switch (hitSide)
        {
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

    public void onInstantiate() {}

    public World world()
    {
        return this.field_70331_k;
    }

    public IBlockAccess access()
    {
        if (world() != null) {
            return world();
        }
        return this.access;
    }

    public int x()
    {
        assert (world() != null) : ("TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.");
        return this.field_70329_l;
    }

    public int y()
    {
        assert (world() != null) : ("TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.");
        return this.field_70330_m;
    }

    public int z()
    {
        assert (world() != null) : ("TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.");
        return this.field_70327_n;
    }

    public VectorWorld position()
    {
        assert (world() != null) : ("TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.");
        return new VectorWorld(this);
    }

    protected VectorWorld center()
    {
        assert (world() != null) : ("TileBlock [" + getClass().getSimpleName() + "] attempted to access invalid method.");
        return (VectorWorld)position().translate(0.5D);
    }

    public Block func_70311_o()
    {
        if (access() != null)
        {
            Block b = Block.field_71973_m[access().func_72798_a(this.field_70329_l, this.field_70330_m, this.field_70327_n)];
            if (b == null) {
                return this.block;
            }
            return b;
        }
        return this.block;
    }

    public TileBlock tile()
    {
        return null;
    }

    public Block block()
    {
        return Block.field_71973_m[blockID()];
    }

    public int blockID()
    {
        if (access() != null) {
            return access().func_72798_a(x(), y(), z());
        }
        return this.block.field_71990_ca;
    }

    public int metadata()
    {
        return access().func_72805_g(x(), y(), z());
    }

    public ArrayList<ItemStack> getDrops(int metadata, int fortune)
    {
        ArrayList<ItemStack> drops = new ArrayList();
        if (func_70311_o() != null) {
            drops.add(new ItemStack(func_70311_o(), quantityDropped(metadata, fortune), metadataDropped(metadata, fortune)));
        }
        return drops;
    }

    public int quantityDropped(int meta, int fortune)
    {
        return 1;
    }

    public int metadataDropped(int meta, int fortune)
    {
        return 0;
    }

    public boolean isControlDown(EntityPlayer player)
    {
        try
        {
            Class ckm = Class.forName("codechicken.multipart.ControlKeyModifer");
            Method m = ckm.getMethod("isControlDown", new Class[] { EntityPlayer.class });
            return ((Boolean)m.invoke(null, new Object[] { player })).booleanValue();
        }
        catch (Exception e) {}
        return false;
    }

    public void getSubBlocks(int id, CreativeTabs creativeTab, List list)
    {
        list.add(new ItemStack(id, 1, 0));
    }

    public ItemStack getPickBlock(MovingObjectPosition target)
    {
        return new ItemStack(func_70311_o(), 1, metadataDropped(metadata(), 0));
    }

    public int getLightValue(IBlockAccess access)
    {
        return Block.field_71984_q[access.func_72798_a(x(), y(), z())];
    }

    public void click(EntityPlayer player) {}

    public boolean activate(EntityPlayer player, int side, Vector3 hit)
    {
        if (WrenchUtility.isUsableWrench(player, player.field_71071_by.func_70448_g(), x(), y(), z()))
        {
            if (configure(player, side, hit))
            {
                WrenchUtility.damageWrench(player, player.field_71071_by.func_70448_g(), x(), y(), z());
                return true;
            }
            return false;
        }
        return use(player, side, hit);
    }

    protected boolean use(EntityPlayer player, int side, Vector3 hit)
    {
        return false;
    }

    protected boolean configure(EntityPlayer player, int side, Vector3 hit)
    {
        return tryRotate(side, hit);
    }

    protected boolean tryRotate(int side, Vector3 hit)
    {
        if ((this instanceof IRotatable))
        {
            byte result = getSideToRotate((byte)side, hit.x, hit.y, hit.z);
            if (result != -1)
            {
                setDirection(ForgeDirection.getOrientation(result));
                return true;
            }
        }
        return false;
    }

    public byte getSideToRotate(byte hitSide, double hitX, double hitY, double hitZ)
    {
        byte tBack = (byte)(hitSide ^ 0x1);
        switch (hitSide)
        {
            case 0:
            case 1:
                if (hitX < 0.25D)
                {
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
                if (hitX > 0.75D)
                {
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
                if (hitX < 0.25D)
                {
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
                if (hitX > 0.75D)
                {
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
                if (hitZ < 0.25D)
                {
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
                if (hitZ > 0.75D)
                {
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

    public ForgeDirection determineOrientation(EntityLivingBase entityLiving)
    {
        if ((MathHelper.func_76135_e((float)entityLiving.field_70165_t - x()) < 2.0F) && (MathHelper.func_76135_e((float)entityLiving.field_70161_v - z()) < 2.0F))
        {
            double d0 = entityLiving.field_70163_u + 1.82D - entityLiving.field_70129_M;
            if ((canRotate(1)) && (d0 - y() > 2.0D)) {
                return ForgeDirection.UP;
            }
            if ((canRotate(0)) && (y() - d0 > 0.0D)) {
                return ForgeDirection.DOWN;
            }
        }
        int playerSide = MathHelper.func_76128_c(entityLiving.field_70177_z * 4.0F / 360.0F + 0.5D) & 0x3;
        int returnSide = (playerSide == 3) && (canRotate(4)) ? 4 : (playerSide == 2) && (canRotate(3)) ? 3 : (playerSide == 1) && (canRotate(5)) ? 5 : (playerSide == 0) && (canRotate(2)) ? 2 : 0;
        if (this.isFlipPlacement) {
            return ForgeDirection.getOrientation(returnSide).getOpposite();
        }
        return ForgeDirection.getOrientation(returnSide);
    }

    public boolean canRotate(int ord)
    {
        return (this.rotationMask & 1 << ord) != 0;
    }

    public ForgeDirection getDirection()
    {
        return ForgeDirection.getOrientation(metadata());
    }

    public void setDirection(ForgeDirection direction)
    {
        world().func_72921_c(x(), y(), z(), direction.ordinal(), 3);
    }

    protected void onAdded()
    {
        onWorldJoin();
    }

    public void onPlaced(EntityLivingBase entityLiving, ItemStack itemStack)
    {
        if ((this instanceof IRotatable)) {
            ((IRotatable)this).setDirection(determineOrientation(entityLiving));
        }
    }

    public void onRemove(int par5, int par6)
    {
        onWorldSeparate();
    }

    protected void onWorldJoin() {}

    protected void onWorldSeparate() {}

    protected void onNeighborChanged() {}

    protected void notifyChange()
    {
        world().func_72898_h(x(), y(), z(), blockID());
    }

    protected void markRender()
    {
        world().func_72902_n(x(), y(), z());
    }

    protected void markUpdate()
    {
        world().func_72845_h(x(), y(), z());
    }

    protected void updateLight()
    {
        world().func_72969_x(x(), y(), z());
    }

    protected void scheduelTick(int delay)
    {
        world().func_72836_a(x(), y(), z(), blockID(), delay);
    }

    public void collide(Entity entity) {}

    public Iterable<Cuboid> getCollisionBoxes(Cuboid intersect, Entity entity)
    {
        List<Cuboid> boxes = new ArrayList();
        for (Cuboid cuboid : getCollisionBoxes()) {
            if ((intersect != null) && (cuboid.intersects(intersect))) {
                boxes.add(cuboid);
            }
        }
        return boxes;
    }

    public Iterable<Cuboid> getCollisionBoxes()
    {
        return Arrays.asList(new Cuboid[] { this.bounds });
    }

    public Cuboid getSelectBounds()
    {
        return this.bounds;
    }

    public Cuboid getCollisionBounds()
    {
        return this.bounds;
    }

    @SideOnly(Side.CLIENT)
    public final TileRender getRenderer()
    {
        if (!RenderInfo.renderer.containsKey(this)) {
            RenderInfo.renderer.put(this, newRenderer());
        }
        return (TileRender)RenderInfo.renderer.get(this);
    }

    @SideOnly(Side.CLIENT)
    protected TileRender newRenderer()
    {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public Icon getIcon(IBlockAccess access, int side)
    {
        return getIcon(side, access.func_72805_g(x(), y(), z()));
    }

    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        return getIcon();
    }

    public Icon getIcon()
    {
        return (Icon)RenderInfo.icon.get(getTextureName());
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        RenderInfo.icon.put(getTextureName(), iconRegister.func_94245_a(getTextureName()));
    }

    @SideOnly(Side.CLIENT)
    protected String getTextureName()
    {
        return this.block.dummyTile.domain + this.textureName;
    }

    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
    {
        return (side == 0) && (this.bounds.min.y > 0.0D);
    }

    public void onFillRain() {}

    public boolean isIndirectlyPowered()
    {
        return world().func_72864_z(x(), y(), z());
    }

    public int getStrongestIndirectPower()
    {
        return world().func_94572_D(x(), y(), z());
    }

    public int getWeakRedstonePower(IBlockAccess access, int side)
    {
        return getStrongRedstonePower(access, side);
    }

    public int getStrongRedstonePower(IBlockAccess access, int side)
    {
        return 0;
    }

    public boolean isSolid(IBlockAccess access, int side)
    {
        return this.material.func_76220_a();
    }

    public int getRenderBlockPass()
    {
        return 0;
    }

    public int tickRate(World world)
    {
        return 20;
    }

    @SideOnly(Side.CLIENT)
    public static class RenderInfo
    {
        @SideOnly(Side.CLIENT)
        private static final WeakHashMap<TileBlock, TileRender> renderer = new WeakHashMap();
        @SideOnly(Side.CLIENT)
        private static final HashMap<String, Icon> icon = new HashMap();
    }

    public static abstract interface IComparatorInputOverride
    {
        public abstract int getComparatorInputOverride(int paramInt);
    }
}
