package fr.redboxing.theylor.attributes.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface ServerWorldsTickCallback {
    Event<ServerWorldsTickCallback> EVENT = EventFactory.createArrayBacked(ServerWorldsTickCallback.class,
            (listeners) -> (ticks) -> {
                for (ServerWorldsTickCallback listener : listeners) {
                    listener.interact(ticks);
                }
            });

    void interact(int ticks);
}
