package org.halvors.quantum.atomic.common.utility;

import net.minecraft.util.ResourceLocation;
import org.halvors.quantum.atomic.common.Reference;
import org.halvors.quantum.atomic.common.utility.type.ResourceType;

public class ResourceUtility {
	public static ResourceLocation getResource(ResourceType resourceType, String name) {
		return new ResourceLocation(Reference.DOMAIN, resourceType.getPrefix() + name);
	}
}
