package fr.redboxing.theylor.attributes.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public interface ServerPlayerEntityTickCallback {
    Event<ServerPlayerEntityTickCallback> EVENT = EventFactory.createArrayBacked(ServerPlayerEntityTickCallback.class,
            (listeners) -> (entity) -> {
                for (ServerPlayerEntityTickCallback listener : listeners) {
                  listener.interact(entity);
                }
            });

    void interact(ServerPlayerEntity entity);
}
