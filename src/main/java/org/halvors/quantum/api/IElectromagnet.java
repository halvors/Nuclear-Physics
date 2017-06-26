package org.halvors.quantum.api;

/**
 * Applied to all tiles that are to act like an electromagnet.
 */
public interface IElectromagnet {
    /** Is this electromagnet working currently? */
    boolean isRunning();
}
