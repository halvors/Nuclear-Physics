package org.halvors.nuclearphysics.api.effect.explosion;

public interface IFulmination {
    /**
     * The radius of effect of the explosion.
     */
    float getRadius();

    /**
     * The energy emitted by this explosive. In Joules and approximately based off of a real life equivalent.
     */
    int getEnergy();
}