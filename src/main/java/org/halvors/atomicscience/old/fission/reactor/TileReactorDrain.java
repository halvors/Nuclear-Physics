package atomicscience.fission.reactor;

import atomicscience.AtomicScience;
import atomicscience.ReactorExplosion;
import atomicscience.fusion.TilePlasma;
import calclavia.api.atomicscience.IReactor;
import calclavia.api.atomicscience.IReactorComponent;
import calclavia.api.atomicscience.PlasmaEvent.SpawnPlasmaEvent;
import calclavia.lib.content.module.prefab.TileInventory;
import calclavia.lib.multiblock.reference.IMultiBlockStructure;
import calclavia.lib.multiblock.reference.MultiBlockHandler;
import calclavia.lib.network.PacketAnnotation;
import calclavia.lib.network.PacketHandler;
import calclavia.lib.network.Synced.SyncedInput;
import calclavia.lib.network.Synced.SyncedOutput;
import calclavia.lib.prefab.poison.Poison;
import calclavia.lib.prefab.poison.PoisonRadiation;
import calclavia.lib.thermal.ThermalGrid;
import calclavia.lib.thermal.ThermalPhysics;
import calclavia.lib.utility.inventory.InventoryUtility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.BlockGrass;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventBus;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;

public class TileReactorCell
        extends TileInventory
        implements IMultiBlockStructure<TileReactorCell>, IInventory, IReactor, IFluidHandler
{
    public static final int RADIUS = 2;
    public static final int MELTING_POINT = 2000;
    private final int specificHeatCapacity = 1000;
    private final float mass = ThermalPhysics.getMass(1000.0F, 7.0F);
    public FluidTank tank = new FluidTank(15000);
    public float temperature = 295.0F;
    private long prevInternalEnergy = 0L;
    private long internalEnergy = 0L;
    private boolean markClientUpdate = false;
    private MultiBlockHandler<TileReactorCell> multiBlock;

    public TileReactorCell()
    {
        super(UniversalElectricity.machine);
        this.textureName = "machine";
        this.isOpaqueCube = false;
        this.normalRender = false;
        this.customItemRender = true;
    }

    protected void onWorldJoin()
    {
        updatePositionStatus();
    }

    protected void onNeighborChanged()
    {
        updatePositionStatus();
    }

    public void initiate()
    {
        updatePositionStatus();
        super.initiate();
    }

    protected boolean use(EntityPlayer player, int side, Vector3 hit)
    {
        if (!world().field_72995_K)
        {
            TileReactorCell tile = (TileReactorCell)getMultiBlock().get();
            if (!player.func_70093_af())
            {
                if (tile.func_70301_a(0) != null)
                {
                    InventoryUtility.dropItemStack(world(), new Vector3(player), tile.func_70301_a(0), 0);
                    tile.func_70299_a(0, null);
                    return true;
                }
                if (player.field_71071_by.func_70448_g() != null) {
                    if ((player.field_71071_by.func_70448_g().func_77973_b() instanceof IReactorComponent))
                    {
                        ItemStack itemStack = player.field_71071_by.func_70448_g().func_77946_l();
                        itemStack.field_77994_a = 1;
                        tile.func_70299_a(0, itemStack);
                        player.field_71071_by.func_70298_a(player.field_71071_by.field_70461_c, 1);
                        return true;
                    }
                }
            }
            player.openGui(AtomicScience.instance, 0, world(), tile.field_70329_l, tile.field_70330_m, tile.field_70327_n);
        }
        return true;
    }

    public void func_70316_g()
    {
        super.func_70316_g();
        if (!getMultiBlock().isPrimary())
        {
            if (func_70301_a(0) != null) {
                if (((TileReactorCell)getMultiBlock().get()).func_70301_a(0) == null)
                {
                    ((TileReactorCell)getMultiBlock().get()).func_70299_a(0, func_70301_a(0));
                    func_70299_a(0, null);
                }
            }
            if (this.tank.getFluidAmount() > 0) {
                ((TileReactorCell)getMultiBlock().get()).tank.fill(this.tank.drain(this.tank.getCapacity(), true), true);
            }
        }
        if (!this.field_70331_k.field_72995_K)
        {
            this.temperature = ThermalGrid.instance().getTemperature(new VectorWorld(this));
            if ((getMultiBlock().isPrimary()) && (this.tank.getFluid() != null) && (this.tank.getFluid().fluidID == AtomicScience.FLUID_PLASMA.getID()))
            {
                FluidStack drain = this.tank.drain(1000, false);
                if ((drain != null) && (drain.amount >= 1000))
                {
                    ForgeDirection spawnDir = ForgeDirection.getOrientation(this.field_70331_k.field_73012_v.nextInt(3) + 2);
                    Vector3 spawnPos = new Vector3(this).translate(spawnDir, 2.0D);
                    spawnPos.translate(0.0D, Math.max(this.field_70331_k.field_73012_v.nextInt(getHeight()) - 1, 0), 0.0D);
                    if (this.field_70331_k.func_72799_c(spawnPos.intX(), spawnPos.intY(), spawnPos.intZ()))
                    {
                        MinecraftForge.EVENT_BUS.post(new PlasmaEvent.SpawnPlasmaEvent(this.field_70331_k, spawnPos.intX(), spawnPos.intY(), spawnPos.intZ(), TilePlasma.plasmaMaxTemperature));
                        this.tank.drain(1000, true);
                    }
                }
            }
            else
            {
                this.prevInternalEnergy = this.internalEnergy;

                ItemStack fuelRod = ((TileReactorCell)getMultiBlock().get()).func_70301_a(0);
                if (fuelRod != null) {
                    if ((fuelRod.func_77973_b() instanceof IReactorComponent))
                    {
                        ((IReactorComponent)fuelRod.func_77973_b()).onReact(fuelRod, this);
                        if (!this.field_70331_k.field_72995_K) {
                            if (fuelRod.func_77960_j() >= fuelRod.func_77958_k()) {
                                ((TileReactorCell)getMultiBlock().get()).func_70299_a(0, null);
                            }
                        }
                        if (this.ticks % 20L == 0L) {
                            if (this.field_70331_k.field_73012_v.nextFloat() > 0.65D)
                            {
                                List<EntityLiving> entities = this.field_70331_k.func_72872_a(EntityLiving.class, AxisAlignedBB.func_72330_a(this.field_70329_l - 4, this.field_70330_m - 4, this.field_70327_n - 4, this.field_70329_l + 4, this.field_70330_m + 4, this.field_70327_n + 4));
                                for (EntityLiving entity : entities) {
                                    PoisonRadiation.INSTANCE.poisonEntity(new Vector3(this), entity);
                                }
                            }
                        }
                    }
                }
                if (this.internalEnergy - this.prevInternalEnergy > 0L)
                {
                    float deltaT = ThermalPhysics.getTemperatureForEnergy(this.mass, 1000L, ((this.internalEnergy - this.prevInternalEnergy) * 0.15D));

                    int rods = 0;
                    for (int i = 2; i < 6; i++)
                    {
                        Vector3 checkAdjacent = new Vector3(this).translate(ForgeDirection.getOrientation(i));
                        if (checkAdjacent.getBlockID(this.field_70331_k) == AtomicScience.blockControlRod.field_71990_ca)
                        {
                            deltaT = (float)(deltaT / 1.1D);
                            rods++;
                        }
                    }
                    ThermalGrid.instance().addTemperature(new VectorWorld(this), deltaT);
                    if (this.temperature >= 2000.0F)
                    {
                        meltDown();
                        return;
                    }
                }
                this.internalEnergy = 0L;
                if (isOverToxic())
                {
                    VectorWorld leakPos = new VectorWorld(this).translate(this.field_70331_k.field_73012_v.nextInt(20) - 10, this.field_70331_k.field_73012_v.nextInt(20) - 10, this.field_70331_k.field_73012_v.nextInt(20) - 10);

                    int blockID = leakPos.getBlockID();
                    if (blockID == Block.field_71980_u.field_71990_ca)
                    {
                        leakPos.setBlock(this.field_70331_k, AtomicScience.blockRadioactive.field_71990_ca);
                        this.tank.drain(1000, true);
                    }
                    else if ((blockID == 0) || (Block.field_71973_m[blockID].isBlockReplaceable(this.field_70331_k, leakPos.intX(), leakPos.intY(), leakPos.intZ())))
                    {
                        if (this.tank.getFluid() != null)
                        {
                            leakPos.setBlock(this.field_70331_k, this.tank.getFluid().getFluid().getBlockID());
                            this.tank.drain(1000, true);
                        }
                    }
                }
            }
            if ((this.ticks % 60L == 0L) || (this.markClientUpdate)) {
                PacketHandler.sendPacketToClients(func_70319_e(), this.field_70331_k, new Vector3(this), 50.0D);
            }
        }
    }

    public boolean isOverToxic()
    {
        return (this.tank.getFluid() != null) && (this.tank.getFluid().fluidID == AtomicScience.FLUID_TOXIC_WASTE.getID()) && (this.tank.getFluid().amount >= this.tank.getCapacity());
    }

    public void updatePositionStatus()
    {
        TileReactorCell mainTile = getLowest();
        mainTile.getMultiBlock().deconstruct();
        mainTile.getMultiBlock().construct();

        boolean top = new Vector3(this).add(new Vector3(0.0D, 1.0D, 0.0D)).getTileEntity(this.field_70331_k) instanceof TileReactorCell;
        boolean bottom = new Vector3(this).add(new Vector3(0.0D, -1.0D, 0.0D)).getTileEntity(this.field_70331_k) instanceof TileReactorCell;
        if ((top) && (bottom)) {
            this.field_70331_k.func_72921_c(this.field_70329_l, this.field_70330_m, this.field_70327_n, 1, 3);
        } else if (top) {
            this.field_70331_k.func_72921_c(this.field_70329_l, this.field_70330_m, this.field_70327_n, 0, 3);
        } else {
            this.field_70331_k.func_72921_c(this.field_70329_l, this.field_70330_m, this.field_70327_n, 2, 3);
        }
    }

    public void onMultiBlockChanged() {}

    public Vector3[] getMultiBlockVectors()
    {
        List<Vector3> vectors = new ArrayList();

        Vector3 checkPosition = new Vector3(this);
        for (;;)
        {
            TileEntity t = checkPosition.getTileEntity(this.field_70331_k);
            if (!(t instanceof TileReactorCell)) {
                break;
            }
            vectors.add(checkPosition.clone().subtract(getPosition()));

            checkPosition.y += 1.0D;
        }
        return (Vector3[])vectors.toArray(new Vector3[0]);
    }

    public TileReactorCell getLowest()
    {
        TileReactorCell lowest = this;
        Vector3 checkPosition = new Vector3(this);
        for (;;)
        {
            TileEntity t = checkPosition.getTileEntity(this.field_70331_k);
            if (!(t instanceof TileReactorCell)) {
                break;
            }
            lowest = (TileReactorCell)t;

            checkPosition.y -= 1.0D;
        }
        return lowest;
    }

    public World getWorld()
    {
        return this.field_70331_k;
    }

    public Vector3 getPosition()
    {
        return new Vector3(this);
    }

    public MultiBlockHandler<TileReactorCell> getMultiBlock()
    {
        if (this.multiBlock == null) {
            this.multiBlock = new MultiBlockHandler(this);
        }
        return this.multiBlock;
    }

    public int getHeight()
    {
        int height = 0;
        Vector3 checkPosition = new Vector3(this);
        TileEntity tile = this;
        while ((tile instanceof TileReactorCell))
        {
            checkPosition.y += 1.0D;
            height++;
            tile = checkPosition.getTileEntity(this.field_70331_k);
        }
        return height;
    }

    public Packet func_70319_e()
    {
        return AtomicScience.PACKET_ANNOTATION.getPacket(this);
    }

    private void meltDown()
    {
        if (!this.field_70331_k.field_72995_K)
        {
            func_70299_a(0, null);
            ReactorExplosion reactorExplosion = new ReactorExplosion(this.field_70331_k, null, this.field_70329_l, this.field_70330_m, this.field_70327_n, 9.0F);
            reactorExplosion.func_77278_a();
            reactorExplosion.func_77279_a(true);

            this.field_70331_k.func_94575_c(this.field_70329_l, this.field_70330_m, this.field_70327_n, Block.field_71944_C.field_71990_ca);
        }
    }

    @Synced.SyncedInput
    public void func_70307_a(NBTTagCompound nbt)
    {
        super.func_70307_a(nbt);
        this.temperature = nbt.func_74760_g("temperature");
        this.tank.readFromNBT(nbt);
        getMultiBlock().load(nbt);
    }

    @Synced.SyncedOutput
    public void func_70310_b(NBTTagCompound nbt)
    {
        super.func_70310_b(nbt);
        nbt.func_74776_a("temperature", this.temperature);
        this.tank.writeToNBT(nbt);
        getMultiBlock().save(nbt);
    }

    public int func_70297_j_()
    {
        return 1;
    }

    public boolean func_70300_a(EntityPlayer par1EntityPlayer)
    {
        return this.field_70331_k.func_72796_p(this.field_70329_l, this.field_70330_m, this.field_70327_n) == this;
    }

    public String func_70303_b()
    {
        return func_70311_o().func_71931_t();
    }

    public boolean func_94041_b(int slotID, ItemStack itemStack)
    {
        if ((getMultiBlock().isPrimary()) && (((TileReactorCell)getMultiBlock().get()).func_70301_a(0) == null)) {
            return itemStack.func_77973_b() instanceof IReactorComponent;
        }
        return false;
    }

    public void func_70296_d()
    {
        this.markClientUpdate = true;
    }

    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        return ((TileReactorCell)getMultiBlock().get()).tank.fill(resource, doFill);
    }

    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return this.tank.drain(maxDrain, doDrain);
    }

    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if ((resource == null) || (!resource.isFluidEqual(this.tank.getFluid()))) {
            return null;
        }
        return this.tank.drain(resource.amount, doDrain);
    }

    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return fluid == AtomicScience.FLUID_PLASMA;
    }

    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return fluid == AtomicScience.FLUID_TOXIC_WASTE;
    }

    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[] { this.tank.getInfo() };
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if ((getMultiBlock().isPrimary()) && (getMultiBlock().isConstructed())) {
            return INFINITE_EXTENT_AABB;
        }
        return super.getRenderBoundingBox();
    }

    public void heat(long energy)
    {
        this.internalEnergy = Math.max(this.internalEnergy + energy, 0L);
    }
}
