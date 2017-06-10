package org.halvors.quantum.lib.grid;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import org.halvors.quantum.common.Reference;
import universalelectricity.api.net.IUpdate;

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

    private boolean pause = false;

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

    @Override
    public void run() {
        long last = System.currentTimeMillis();

        while (!pause) {
            // theProfiler.profilingEnabled = true;
            long current = System.currentTimeMillis();
            deltaTime = current - last;

            // Tick all updaters.
            synchronized (updaters) {
                Set<IUpdate> removeUpdaters = Collections.newSetFromMap(new WeakHashMap<IUpdate, Boolean>());
                Iterator<IUpdate> updaterIt = new HashSet<>(updaters).iterator();

                try {
                    while (updaterIt.hasNext()) {
                        IUpdate updater = updaterIt.next();

                        if (updater.canUpdate()) {
                            updater.update();
                        }

                        if (!updater.continueUpdate()) {
                            removeUpdaters.add(updater);
                        }
                    }

                    updaters.removeAll(removeUpdaters);
                } catch (Exception e) {
                    System.out.println("Threaded Ticker: Failed while tcking updater. This is a bug! Clearing all tickers for self repair.");
                    updaters.clear();
                    e.printStackTrace();
                }
            }

            // Perform all queued events.
            synchronized (queuedEvents) {
                while (!queuedEvents.isEmpty()) {
                    MinecraftForge.EVENT_BUS.post(queuedEvents.poll());
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
