package org.halvors.nuclearphysics.api;

/**
 * This interface should be applied to all things that has a tier/level.
 */
public interface ITier {
    /** Gets the tier of this object
     *
     * @return - The tier
     */
    int getTier();

    /** Sets the tier of the object
     *
     * @param tier - The tier to be set
     */
    void setTier(int tier);
}
