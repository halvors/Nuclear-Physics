package org.halvors.nuclearphysics.common.grid;

import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;

import java.util.*;

/*
 * A ticker to update all grids. This is multithreaded.
 */
public class UpdateTicker extends Thread {
    private static final UpdateTicker instance = new UpdateTicker();

    // For updaters to be ticked.
    private final Set<IUpdate> updaters = Collections.newSetFromMap(new WeakHashMap<>());

    private boolean paused = false;

    // The time in milliseconds between successive updates.
    private long deltaTime;

    public UpdateTicker() {
        setName(Reference.NAME);
        setPriority(MIN_PRIORITY);
    }

    public static UpdateTicker getInstance() {
        return instance;
    }

    public static void addNetwork(final IUpdate updater) {
        synchronized (instance.updaters) {
            instance.updaters.add(updater);
        }
    }

    public long getDeltaTime() {
        return deltaTime;
    }

    public int getUpdaterCount() {
        return updaters.size();
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(final boolean paused) {
        this.paused = paused;
    }

    @Override
    public void run() {
        long last = System.currentTimeMillis();

        while (!paused) {
            final long current = System.currentTimeMillis();
            deltaTime = current - last;

            // Tick all updaters.
            synchronized (updaters) {
                final Iterator<IUpdate> updaterIterator = new HashSet<>(updaters).iterator();

                try {
                    while (updaterIterator.hasNext()) {
                        final IUpdate updater = updaterIterator.next();

                        if (updater.canUpdate()) {
                            updater.update();
                        }

                        if (!updater.continueUpdate()) {
                            updaterIterator.remove();
                        }
                    }
                } catch (Exception e) {
                    NuclearPhysics.getLogger().warn("Threaded Ticker: Failed while ticking updater. This is a bug! Clearing all tickers for self repair.");
                    e.printStackTrace();
                }
            }

            last = current;

            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
