package org.halvors.nuclearphysics.common.science.grid;

import org.halvors.nuclearphysics.common.Reference;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/*
 * A ticker to update all grids. This is multithreaded.
 */
public class GridTicker extends Thread {
    private static final GridTicker instance = new GridTicker();

    // Grids to be ticked.
    private final Set<IGrid> grids = ConcurrentHashMap.newKeySet();

    private boolean paused = false;

    // The time in milliseconds between successive updates.
    private long deltaTime;

    public GridTicker() {
        setName(Reference.NAME);
        setPriority(MIN_PRIORITY);
    }

    public static GridTicker getInstance() {
        return instance;
    }

    public void addGrid(final IGrid grid) {
        grids.add(grid);
    }

    public long getDeltaTime() {
        return deltaTime;
    }

    public int getGridCount() {
        return grids.size();
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
            for (final IGrid grid : grids) {
                if (grid.canUpdate()) {
                    grid.update();
                }

                if (!grid.continueUpdate()) {
                    grids.remove(grid);
                }
            }

            last = current;

            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                // This means the server has stopped this thread because it is shutting down.
            }
        }
    }
}
