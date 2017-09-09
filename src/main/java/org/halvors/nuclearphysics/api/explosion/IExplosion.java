package org.halvors.nuclearphysics.api.explosion;

public interface IExplosion {
    /** Called to initiate the explosion. */
    void explode();

    /** @return The radius of effect of the explosion. */
    float getRadius();

    /** @return The energy emitted by this explosive. In Joules and approximately based off of a real
     * life equivalent. */
    long getEnergy();
}