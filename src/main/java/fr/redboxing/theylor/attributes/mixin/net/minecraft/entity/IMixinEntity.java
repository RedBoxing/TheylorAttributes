package fr.redboxing.theylor.attributes.mixin.net.minecraft.entity;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(Entity.class)
public interface IMixinEntity {
    @Accessor("CURRENT_ID")
    AtomicInteger getCURRENT_ID();
}
