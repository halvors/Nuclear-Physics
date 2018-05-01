package org.halvors.nuclearphysics.api.effect.poison;

public enum EnumPoisonType {
    RADIATION,
    CHEMICAL,
    CONTAGIOUS;

    public String getName() {
        return name().toLowerCase();
    }
}