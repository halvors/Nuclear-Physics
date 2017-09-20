package org.halvors.nuclearphysics.common.tile.process;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.halvors.nuclearphysics.common.ConfigurationManager;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.capabilities.energy.EnergyStorage;
import org.halvors.nuclearphysics.common.capabilities.fluid.GasTank;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.init.ModItems;
import org.halvors.nuclearphysics.common.item.reactor.fission.ItemUranium.EnumUranium;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.recipe.RecipeHandler;
import org.halvors.nuclearphysics.common.recipe.machine.GasCentrifugeRecipe;
import org.halvors.nuclearphysics.common.recipe.input.FluidInput;
import org.halvors.nuclearphysics.common.tile.TileInventoryMachine;
import org.halvors.nuclearphysics.common.utility.EnergyUtility;
import org.halvors.nuclearphysics.common.utility.OreDictionaryHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileGasCentrifuge extends TileInventoryMachine {
    public static final int ticksRequired = 60 * 20;
    private static final int energyPerTick = 20000;

    public float rotation = 0;

    public GasCentrifugeRecipe cachedRecipe;

    private final GasTank tank = new GasTank(Fluid.BUCKET_VOLUME * 5) {
        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if (resource.isFluidEqual(ModFluids.fluidStackUraniumHexaflouride)) {
                return super.fill(resource, doFill);
            }

            return 0;
        }

        @Override
        public boolean canDrain() {
            return false;
        }
    };

    public TileGasCentrifuge() {
        this(EnumMachine.GAS_CENTRIFUGE);
    }

    public TileGasCentrifuge(EnumMachine type) {
        super(type);

        energyStorage = new EnergyStorage(energyPerTick * 2);
        inventory = new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }

            private boolean isItemValidForSlot(int slot, ItemStack itemStack) {
                switch (slot) {
                    case 0: // Battery input slot.
                        return EnergyUtility.canBeDischarged(itemStack);

                    // TODO: Add uranium hexaflouride container here.
                    /*
                    case 1: // Input tank drain slot.
                        return OreDictionaryHelper.isEmptyCell(itemStack);
                    */

                    case 2: // Item output slot.
                    case 3: // Item output slot.
                        return OreDictionaryHelper.isUranium(itemStack);
                }

                return false;
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if (!isItemValidForSlot(slot, stack)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tank, null, tag.getTag("tank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setTag("tank", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tank, null));

        return tag;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) tank;
        }

        return super.getCapability(capability, facing);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void update() {
        super.update();

        if (operatingTicks > 0) {
            rotation += 0.45;
        } else {
            rotation = 0;
        }

        if (!world.isRemote) {
            EnergyUtility.discharge(0, this);

            /*
            if (canFunction() && canProcess() && energyStorage.extractEnergy(energyPerTick, true) >= energyPerTick) {
                if (operatingTicks < ticksRequired) {
                    operatingTicks++;
                } else {


                    //process();
                    reset();
                }

                energyUsed = energyStorage.extractEnergy(energyPerTick, false);
            } else {
                reset();
            }
            */

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////

            GasCentrifugeRecipe recipe = getRecipe();

            if (canOperate(recipe)) {// && canFunction() && energyStorage.extractEnergy(energyPerTick, true) >= energyPerTick) {
                NuclearPhysics.getLogger().info("Check 1");

                energyUsed = energyStorage.extractEnergy(energyPerTick, false);

                if ((operatingTicks + 1) < ticksRequired) {
                    operatingTicks++;

                    NuclearPhysics.getLogger().info("Check 2");
                } else {
                    NuclearPhysics.getLogger().info("Check 3");

                    operate(recipe);

                    operatingTicks = 0;
                }
            }

            if (!canOperate(recipe)) {
                NuclearPhysics.getLogger().info("Check 4");

                operatingTicks = 0;
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////

            if (world.getWorldTime() % 10 == 0) {
                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public FluidInput getInput() {
        return new FluidInput(tank.getFluid());
    }

    public GasCentrifugeRecipe getRecipe() {
        FluidInput input = getInput();

        if (cachedRecipe == null || !input.testEquality(cachedRecipe.getInput())) {
            cachedRecipe = RecipeHandler.getGasCentrifugeRecipe(getInput());
        }

        return cachedRecipe;
    }

    public boolean canOperate(GasCentrifugeRecipe recipe) {
        return recipe != null;// && recipe.canOperate(tank, inventory);
    }

    public void operate(GasCentrifugeRecipe recipe) {
        recipe.operate(tank, inventory);

        markDirty();
        //ejectorComponent.outputItems();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (world.isRemote) {
            tank.handlePacketData(dataStream);
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        super.getPacketData(objects);

        tank.getPacketData(objects);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public IFluidTank getTank() {
        return tank;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.UP ? new int[] { 0, 1 } : new int[] { 2, 3 };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, EnumFacing side) {
        return slot == 1 && isItemValidForSlot(slot, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, EnumFacing side) {
        return slot == 2 || slot == 3;
    }
    */

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean canProcess() {
        FluidStack fluidStack = tank.getFluid();

        if (fluidStack != null) {
            if (fluidStack.amount >= ConfigurationManager.General.uraniumHexaflourideRatio) {
                return true;
            }
        }

        return false;
    }

    public void process() {
        if (canProcess()) {
            tank.drainInternal(ConfigurationManager.General.uraniumHexaflourideRatio, true);

            if (world.rand.nextFloat() > 0.6) {
                inventory.insertItem(2, new ItemStack(ModItems.itemUranium, 1, EnumUranium.URANIUM_235.ordinal()), false);
            } else {
                inventory.insertItem(3, new ItemStack(ModItems.itemUranium, 1, EnumUranium.URANIUM_238.ordinal()), false);
            }
        }
    }
}
