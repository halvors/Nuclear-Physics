package org.halvors.nuclearphysics.api;

/**
 * This interface should be applied to all things that has a tier/level.
 */
public interface ITier {
    int getTier();

    void setTier(int tier);
}
