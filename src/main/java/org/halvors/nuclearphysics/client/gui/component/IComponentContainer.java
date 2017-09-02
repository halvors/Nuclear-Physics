package org.halvors.nuclearphysics.client.gui.component;

import java.util.HashSet;
import java.util.Set;

public interface IComponentContainer {
    Set<IComponent> components = new HashSet<>();
}
