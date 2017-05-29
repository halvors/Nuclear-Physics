package org.halvors.atomicscience.common.util;

import net.minecraft.util.ResourceLocation;
import org.halvors.electrometrics.common.Reference;
import org.halvors.electrometrics.common.base.ResourceType;

public class ResourceUtils {
	public static ResourceLocation getResource(ResourceType resourceType, String name) {
		return new ResourceLocation(Reference.DOMAIN, resourceType.getPrefix() + name);
	}
}
