package org.halvors.nuclearphysics.api.tile;

/**
 * Applied to all tiles that are to act like an electromagnet.
 */
public interface IElectromagnet {
    /**
     * Is this electromagnet currently working?
     *
     * @return true if electromagnet is currently running.
     */
    boolean isRunning();
}
