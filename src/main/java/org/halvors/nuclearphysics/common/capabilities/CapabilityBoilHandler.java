package org.halvors.nuclearphysics.common.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import org.halvors.nuclearphysics.api.fluid.IBoilHandler;
import org.halvors.nuclearphysics.common.capabilities.fluid.GasTank;

public class CapabilityBoilHandler {
    @CapabilityInject(IBoilHandler.class)
    public static Capability<IBoilHandler> BOIL_HANDLER_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IBoilHandler.class, new Capability.IStorage<IBoilHandler>() {
            @Override
            public NBTBase writeNBT(Capability<IBoilHandler> capability, IBoilHandler instance, EnumFacing side) {
                if (!(instance instanceof IFluidTank)) {
                    throw new RuntimeException("IBoilHandler instance does not implement IFluidTank");
                }

                NBTTagCompound tag = new NBTTagCompound();
                IFluidTank tank = (IFluidTank) instance;
                FluidStack fluid = tank.getFluid();

                if (fluid != null) {
                    fluid.writeToNBT(tag);
                } else {
                    tag.setString("Empty", "");
                }

                tag.setInteger("Capacity", tank.getCapacity());

                return tag;
            }

            @Override
            public void readNBT(Capability<IBoilHandler> capability, IBoilHandler instance, EnumFacing side, NBTBase nbt) {
                if (!(instance instanceof FluidTank)) {
                    throw new RuntimeException("IBoilHandler instance is not instance of FluidTank");
                }

                NBTTagCompound tags = (NBTTagCompound) nbt;
                FluidTank tank = (FluidTank) instance;
                tank.setCapacity(tags.getInteger("Capacity"));
                tank.readFromNBT(tags);
            }
        }, () -> new GasTank(Fluid.BUCKET_VOLUME));
    }
}