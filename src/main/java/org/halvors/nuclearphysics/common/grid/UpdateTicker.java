package org.halvors.nuclearphysics.common.grid;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
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

    @Override
    public void run() {
        try {
            while (!paused) {
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

                Thread.sleep(50L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
