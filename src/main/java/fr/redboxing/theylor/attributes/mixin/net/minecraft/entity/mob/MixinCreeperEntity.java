package fr.redboxing.theylor.attributes.mixin.net.minecraft.entity.mob;

import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CreeperEntity.class)
public class MixinCreeperEntity {
    @ModifyVariable(method = "explode", at = @At(value = "STORE"), ordinal = 0)
    private Explosion.DestructionType modifyExplosion() {
        return Explosion.DestructionType.NONE;
    }
}
