package org.halvors.quantum.common.util;

import net.minecraft.util.ResourceLocation;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.base.ResourceType;

public class ResourceUtils {
	public static ResourceLocation getResource(ResourceType resourceType, String name) {
		return new ResourceLocation(Reference.DOMAIN, resourceType.getPrefix() + name);
	}
}
