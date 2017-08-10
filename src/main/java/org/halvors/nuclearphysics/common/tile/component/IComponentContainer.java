package org.halvors.nuclearphysics.common.tile.component;

import java.util.HashSet;
import java.util.Set;

public interface IComponentContainer {
    Set<IComponent> components = new HashSet<>();
}
