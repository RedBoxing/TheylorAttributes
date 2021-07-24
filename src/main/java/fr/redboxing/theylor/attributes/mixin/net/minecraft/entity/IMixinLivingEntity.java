package fr.redboxing.theylor.attributes.mixin.net.minecraft.entity;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface IMixinLivingEntity {

    @Accessor("jumping")
    boolean getJumping();
}
