package org.halvors.nuclearphysics.common.utility;

import net.minecraft.util.ResourceLocation;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.type.EnumResource;

public class ResourceUtility {
	public static ResourceLocation getResource(EnumResource resource, String name) {
		return new ResourceLocation(Reference.DOMAIN, resource.getPrefix() + name);
	}
}
