package fr.redboxing.theylor.attributes.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

public interface ServerPlayerEntityDamageCallback {
    Event<ServerPlayerEntityDamageCallback> EVENT = EventFactory.createArrayBacked(ServerPlayerEntityDamageCallback.class,
            (listeners) -> (entity, source, amount) -> {
                for (ServerPlayerEntityDamageCallback listener : listeners) {
                    float result = listener.interact(entity, source, amount);

                    if(result != -1) {
                        return result;
                    }
                }

                return -1;
            });

    float interact(ServerPlayerEntity entity, DamageSource source, float amount);
}
