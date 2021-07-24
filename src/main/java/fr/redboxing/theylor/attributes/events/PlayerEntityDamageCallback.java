package fr.redboxing.theylor.attributes.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

public interface PlayerEntityDamageCallback {
    Event<PlayerEntityDamageCallback> EVENT = EventFactory.createArrayBacked(PlayerEntityDamageCallback.class,
            (listeners) -> (entity, target, source, amount) -> {
                for (PlayerEntityDamageCallback listener : listeners) {
                    float result = listener.interact(entity, target, source, amount);

                    if(result != -1) {
                        return result;
                    }
                }

                return -1;
            });

    float interact(PlayerEntity entity, Entity target, DamageSource source, float amount);
}
