package org.halvors.quantum.common.reactor.fission;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.effect.poison.PoisonRadiation;
import org.halvors.quantum.common.reactor.IReactor;
import org.halvors.quantum.common.reactor.IReactorComponent;
import org.halvors.quantum.common.reactor.TilePlasma;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.common.transform.vector.VectorWorld;
import org.halvors.quantum.lib.event.PlasmaEvent;
import org.halvors.quantum.lib.explosion.ReactorExplosion;
import org.halvors.quantum.lib.multiblock.IMultiBlockStructure;
import org.halvors.quantum.lib.multiblock.MultiBlockHandler;
import org.halvors.quantum.lib.thermal.ThermalGrid;
import org.halvors.quantum.lib.thermal.ThermalPhysics;
import org.halvors.quantum.lib.tile.TileInventory;
import org.halvors.quantum.lib.utility.inventory.InventoryUtility;

import java.util.ArrayList;
import java.util.List;

public class TileReactorCellXY extends TileInventory implements IMultiBlockStructure<TileReactorCellXY>, IInventory, IReactor, IFluidHandler, ISidedInventory {
    public static final int RADIUS = 2;
    public static final int MELTING_POINT = 2000;
    private final int specificHeatCapacity = 1000;
    private final float mass = ThermalPhysics.getMass(1000.0F, 7.0F);
    public FluidTank tank = new FluidTank(15000);
    public float temperature = 295.0F;
    private float previousTemperature = 295.0F;
    private boolean shouldUpdate = false;
    private long prevInternalEnergy = 0L;
    private long internalEnergy = 0L;
    private int meltdownCounter = 0;
    private int meltdownCounterMaximum = 1000;
    private MultiBlockHandler<TileReactorCellXY> multiBlock;

    public TileReactorCellXY() {
        super("reactorCell", Material.iron);

        //this.textureName = "machine";
        //this.isOpaqueCube = false;
        //this.normalRender = false;
        this.customItemRender = true;
    }

    @Override
    protected void onWorldJoin() {
        updatePositionStatus();
    }

    @Override
    protected void onNeighborChanged()
    {
        updatePositionStatus();
    }

    @Override
    public void initiate() {
        updatePositionStatus();

        super.initiate();
    }

    @Override
    protected boolean use(EntityPlayer player, int side, Vector3 hit) {
        if (!getWorld().isRemote) {
            Quantum.getLogger().info("Called!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

            TileReactorCellXY tileEntity = getMultiBlock().get();

            if (!player.isSneaking()) {
                if (tileEntity.getStackInSlot(0) != null) {
                    InventoryUtility.dropItemStack(world(), new Vector3(player), tileEntity.getStackInSlot(0), 0);
                    tileEntity.setInventorySlotContents(0, null);
                }

                if (player.inventory.getCurrentItem() != null) {
                    if (player.inventory.getCurrentItem().getItem() instanceof IReactorComponent) {
                        ItemStack itemStack = player.inventory.getCurrentItem().copy();
                        itemStack.stackSize = 1;
                        tileEntity.setInventorySlotContents(0, itemStack);
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    }
                }
            }

            player.openGui(Quantum.getInstance(), -1, getWorld(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

            return true;
        }

        return false;
    }

    @Override
    protected void markUpdate() {
        super.markUpdate();

        shouldUpdate = true;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (!getMultiBlock().isPrimary()) {
            if (getStackInSlot(0) != null) {
                if (getMultiBlock().get().getStackInSlot(0) == null) {
                    getMultiBlock().get().setInventorySlotContents(0, getStackInSlot(0));
                    setInventorySlotContents(0, null);
                }
            }
            if (tank.getFluidAmount() > 0) {
                getMultiBlock().get().tank.fill(tank.drain(tank.getCapacity(), true), true);
            }
        }

        if (!worldObj.isRemote) {
            if (getMultiBlock().isPrimary() && tank.getFluid() != null && tank.getFluid().getFluid() == Quantum.fluidPlasma) {
                FluidStack drain = tank.drain(1000, false);

                if (drain != null && drain.amount >= 1000) {
                    ForgeDirection spawnDir = ForgeDirection.getOrientation(worldObj.rand.nextInt(3) + 2);
                    Vector3 spawnPos = new Vector3(this).translate(spawnDir, 2.0D);
                    spawnPos.translate(0.0D, Math.max(worldObj.rand.nextInt(getHeight()) - 1, 0), 0.0D);

                    if (worldObj.isAirBlock(spawnPos.intX(), spawnPos.intY(), spawnPos.intZ())) {
                        MinecraftForge.EVENT_BUS.post(new PlasmaEvent.SpawnPlasmaEvent(worldObj, spawnPos.intX(), spawnPos.intY(), spawnPos.intZ(), TilePlasma.plasmaMaxTemperature));
                        tank.drain(1000, true);
                    }
                }
            } else {
                prevInternalEnergy = internalEnergy;

                ItemStack fuelRod = getMultiBlock().get().getStackInSlot(0);

                if (fuelRod != null) {
                    if (fuelRod.getItem() instanceof IReactorComponent) {
                        IReactorComponent reactorComponent = (IReactorComponent) fuelRod.getItem();
                        reactorComponent.onReact(fuelRod, this);

                        if (!worldObj.isRemote) {
                            if (fuelRod.getMetadata() >= fuelRod.getMaxDurability()) {
                                getMultiBlock().get().setInventorySlotContents(0, null);
                            }
                        }

                        if (ticks % 20L == 0L) {
                            if (worldObj.rand.nextFloat() > 0.65D) {
                                List<EntityLiving> entities = worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(xCoord - 4, yCoord - 4, zCoord - 4, xCoord + 4, yCoord + 4, zCoord + 4));

                                for (EntityLiving entity : entities) {
                                    PoisonRadiation.INSTANCE.poisonEntity(new Vector3(this), entity);
                                }
                            }
                        }
                    }
                }

                temperature = ThermalGrid.getTemperature(new VectorWorld(this));

                if (internalEnergy - prevInternalEnergy > 0L) {
                    float deltaT = ThermalPhysics.getTemperatureForEnergy(mass, 1000L, (int) ((internalEnergy - prevInternalEnergy) * 0.15D));

                    int rods = 0;

                    for (int i = 2; i < 6; i++) {
                        Vector3 checkAdjacent = new Vector3(this).translate(ForgeDirection.getOrientation(i));

                        if (checkAdjacent.getBlock(worldObj) == Quantum.blockControlRod) {
                            deltaT = (float) (deltaT / 1.1D);
                            rods++;
                        }
                    }

                    ThermalGrid.addTemperature(new VectorWorld(this), deltaT);

                    if (worldObj.rand.nextInt(80) == 0 && getTemperature() >= 373.0F) {
                        worldObj.playSoundEffect(xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, "Fluid.lava", 0.5F, 2.1F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.85F);
                    }

                    if (worldObj.rand.nextInt(40) == 0 && getTemperature() >= 373.0F) {
                        worldObj.playSoundEffect(xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, "Fluid.lavapop", 0.5F, 2.6F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.8F);
                    }

                    if (worldObj.getWorldTime() % 100.0F == 0.0F && getTemperature() >= 373.0F) {
                        float percentage = Math.min(getTemperature() / 2000.0F, 1.0F);
                        worldObj.playSoundEffect(xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, "resonantinduction:reactorcell", percentage, 1.0F);
                    }

                    if (previousTemperature != temperature && !shouldUpdate) {
                        shouldUpdate = true;
                        previousTemperature = temperature;
                    }

                    if (previousTemperature >= 2000.0F && meltdownCounter < meltdownCounterMaximum) {
                        shouldUpdate = true;
                        meltdownCounter += 1;
                    } else if (previousTemperature >= 2000.0F && meltdownCounter >= meltdownCounterMaximum) {
                        meltdownCounter = 0;
                        meltDown();

                        return;
                    }

                    if (previousTemperature < 2000.0F && meltdownCounter < meltdownCounterMaximum && meltdownCounter > 0) {
                        meltdownCounter -= 1;
                    }
                }

                internalEnergy = 0L;

                if (isOverToxic()) {
                    VectorWorld leakPos = new VectorWorld(this).translate(worldObj.rand.nextInt(20) - 10, worldObj.rand.nextInt(20) - 10, worldObj.rand.nextInt(20) - 10);

                    Block block = leakPos.getBlock();

                    if (block == Blocks.grass) {
                        leakPos.setBlock(worldObj, Quantum.blockRadioactiveGrass);
                        tank.drain(1000, true);
                    } else if (block == Blocks.air || block.isReplaceable(worldObj, leakPos.intX(), leakPos.intY(), leakPos.intZ())) {
                        if (tank.getFluid() != null) {
                            leakPos.setBlock(worldObj, tank.getFluid().getFluid().getBlock());
                            tank.drain(1000, true);
                        }
                    }
                }
            }

            if (ticks % 60L == 0L || shouldUpdate) {
                shouldUpdate = false;
                notifyChange();

                // TODO: Send packet to clients.
                //PacketHandler.sendPacketToClients(getDescriptionPacket(), worldObj, new Vector3(this), 50.0D);
            }
        } else if ((worldObj.rand.nextInt(5) == 0) && (getTemperature() >= 373.0F)) {
            worldObj.spawnParticle("cloud", xCoord + worldObj.rand.nextInt(2), yCoord + 1.0F, zCoord + worldObj.rand.nextInt(2), 0.0D, 0.1D, 0.0D);
            worldObj.spawnParticle("bubble", xCoord + worldObj.rand.nextInt(5), yCoord, zCoord + worldObj.rand.nextInt(5), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public boolean isOverToxic() {
        return tank.getFluid() != null && tank.getFluid() == Quantum.fluidStackToxicWaste && tank.getFluid().amount >= tank.getCapacity();
    }

    public void updatePositionStatus() {
        TileReactorCellXY mainTile = getLowest();
        mainTile.getMultiBlock().deconstruct();
        mainTile.getMultiBlock().construct();

        boolean top = new Vector3(this).add(new Vector3(0.0D, 1.0D, 0.0D)).getTileEntity(worldObj) instanceof TileReactorCellXY;
        boolean bottom = new Vector3(this).add(new Vector3(0.0D, -1.0D, 0.0D)).getTileEntity(worldObj) instanceof TileReactorCellXY;

        if (top && bottom) {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 3);
        } else if (top) {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
        } else {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 2, 3);
        }
    }

    @Override
    public void onMultiBlockChanged() {

    }

    @Override
    public Vector3[] getMultiBlockVectors() {
        List<Vector3> vectors = new ArrayList<>();
        Vector3 checkPosition = new Vector3(this);

        for (;;) {
            TileEntity tileEntity = checkPosition.getTileEntity(worldObj);

            if (!(tileEntity instanceof TileReactorCellXY)) {
                break;
            }

            vectors.add(checkPosition.clone().subtract(getPosition()));

            checkPosition.y += 1.0D;
        }

        return vectors.toArray(new Vector3[0]);
    }

    public TileReactorCellXY getLowest() {
        TileReactorCellXY lowest = this;
        Vector3 checkPosition = new Vector3(this);

        for (;;) {
            TileEntity tileEntity = checkPosition.getTileEntity(worldObj);

            if (!(tileEntity instanceof TileReactorCellXY)) {
                break;
            }

            lowest = (TileReactorCellXY) tileEntity;
            checkPosition.y -= 1.0D;
        }

        return lowest;
    }

    @Override
    public World getWorld() {
        return worldObj;
    }

    @Override
    public Vector3 getPosition() {
        return new Vector3(this);
    }

    @Override
    public MultiBlockHandler<TileReactorCellXY> getMultiBlock() {
        if (multiBlock == null) {
            multiBlock = new MultiBlockHandler(this);
        }

        return multiBlock;
    }

    public int getHeight() {
        int height = 0;
        Vector3 checkPosition = new Vector3(this);
        TileEntity tileEntity = this;

        while (tileEntity instanceof TileReactorCellXY) {
            height++;
            checkPosition.y += 1.0D;
            tileEntity = checkPosition.getTileEntity(worldObj);
        }

        return height;
    }

    @Override
    public Packet getDescriptionPacket() {
        return null;

        //return ResonantInduction.PACKET_ANNOTATION.getPacket(this);
    }

    private void meltDown() {
        if (!worldObj.isRemote) {
            worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.lava, 0, 3);

            ReactorExplosion reactorExplosion = new ReactorExplosion(worldObj, null, xCoord, yCoord, zCoord, 9.0F);
            reactorExplosion.doExplosionA();
            reactorExplosion.doExplosionB(true);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        temperature = nbt.getFloat("temperature");
        tank.readFromNBT(nbt);
        getMultiBlock().load(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        nbt.setFloat("temperature", temperature);
        tank.writeToNBT(nbt);
        getMultiBlock().save(nbt);
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack items, int side) {
        return isItemValidForSlot(slot, items);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this;
    }

    @Override
    public String getInventoryName() {
        return getBlockType().getLocalizedName();
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemStack) {
        if (getMultiBlock().isPrimary() && getMultiBlock().get().getStackInSlot(0) == null) {
            return itemStack.getItem() instanceof IReactorComponent;
        }

        return false;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return getMultiBlock().get().tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (resource == null || !resource.isFluidEqual(tank.getFluid())) {
            return null;
        }

        return tank.drain(resource.amount, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid == Quantum.fluidPlasma;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return fluid == Quantum.fluidToxicWaste;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { tank.getInfo() };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        if (getMultiBlock().isPrimary() && getMultiBlock().isConstructed()) {
            return INFINITE_EXTENT_AABB;
        }

        return super.getRenderBoundingBox();
    }

    @Override
    public void heat(long energy) {
        internalEnergy = Math.max(internalEnergy + energy, 0L);
    }

    @Override
    public float getTemperature() {
        return temperature;
    }
}
