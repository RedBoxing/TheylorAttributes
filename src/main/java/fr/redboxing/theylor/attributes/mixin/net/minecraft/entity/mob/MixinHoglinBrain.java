package fr.redboxing.theylor.attributes.mixin.net.minecraft.entity.mob;

import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HoglinBrain;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HoglinBrain.class)
public class MixinHoglinBrain {
    @Inject(method = "setAttackTarget", at = @At("HEAD"), cancellable = true)
    private static void setAttackTarget(HoglinEntity hoglin, LivingEntity target, CallbackInfo ci) {
        if(target instanceof PlayerEntity && TheylorPlayer.get((PlayerEntity) target).hasCompetenceEnabled(TheylorCompetences.OVERLORD)) {
            ci.cancel();
        }
    }
}
