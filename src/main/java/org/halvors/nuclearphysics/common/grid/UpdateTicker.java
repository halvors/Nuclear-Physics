package org.halvors.nuclearphysics.common.grid;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/*
 * A ticker to update all grids. This is multithreaded.
 */
public class UpdateTicker extends Thread {
    private static final UpdateTicker instance = new UpdateTicker();

    // For updaters to be ticked.
    private final Set<IUpdate> updaters = Collections.newSetFromMap(new WeakHashMap<IUpdate, Boolean>());

    // For queuing Forge events to be invoked the next tick.
    private final Queue<Event> queuedEvents = new ConcurrentLinkedQueue<>();

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

    public static void addNetwork(IUpdate updater) {
        synchronized (instance.updaters) {
            instance.updaters.add(updater);
        }
    }

    public static synchronized void queueEvent(Event event) {
        synchronized (instance.queuedEvents) {
            instance.queuedEvents.add(event);
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

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public void run() {
        try {
            long last = System.currentTimeMillis();

            while (!paused) {
                long current = System.currentTimeMillis();
                deltaTime = current - last;

                // Tick all updaters.
                synchronized (updaters) {
                    final Set<IUpdate> removeUpdaters = Collections.newSetFromMap(new WeakHashMap<>());
                    final Iterator<IUpdate> updaterIt = new HashSet<>(updaters).iterator();

                    try {
                        while (updaterIt.hasNext()) {
                            final IUpdate updater = updaterIt.next();

                            if (updater.canUpdate()) {
                                updater.update();
                            }

                            if (!updater.continueUpdate()) {
                                removeUpdaters.add(updater);
                            }
                        }

                        updaters.removeAll(removeUpdaters);
                    } catch (Exception e) {
                        NuclearPhysics.getLogger().warn("Threaded Ticker: Failed while ticking updater. This is a bug! Clearing all tickers for self repair.");
                        updaters.clear();
                        e.printStackTrace();
                    }
                }

                // Perform all queued events.
                synchronized (queuedEvents) {
                    while (!queuedEvents.isEmpty()) {
                        MinecraftForge.EVENT_BUS.post(Objects.requireNonNull(queuedEvents.poll()));
                    }
                }

                last = current;
                Thread.sleep(50L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
