package org.halvors.nuclearphysics.common.utility;

import net.minecraft.util.ResourceLocation;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.utility.type.Resource;

public class ResourceUtility {
	public static ResourceLocation getResource(Resource resource, String name) {
		return new ResourceLocation(Reference.DOMAIN, resource.getPrefix() + name);
	}
}
