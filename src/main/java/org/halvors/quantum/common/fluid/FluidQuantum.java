package org.halvors.quantum.common.fluid;

import net.minecraftforge.fluids.Fluid;
import org.halvors.quantum.common.utility.ResourceUtility;
import org.halvors.quantum.common.utility.type.ResourceType;

public class FluidQuantum extends Fluid {
    public FluidQuantum(String fluidName) {
        super(fluidName, ResourceUtility.getResource(ResourceType.TEXTURE_FLUIDS, fluidName), ResourceUtility.getResource(ResourceType.TEXTURE_FLUIDS, fluidName));
    }
}
