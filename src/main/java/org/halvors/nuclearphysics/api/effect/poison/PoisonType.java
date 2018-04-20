package org.halvors.nuclearphysics.api.effect.poison;

public enum PoisonType {
    RADIATION,
    CHEMICAL,
    CONTAGIOUS;

    public String getName() {
        return name().toLowerCase();
    }
}