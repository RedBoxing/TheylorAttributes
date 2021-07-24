package fr.redboxing.theylor.attributes.mixin.net.minecraft.entity.mob;

import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBruteBrain;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinBruteBrain.class)
public class MixinPiglinBruteBrain {
    @Inject(method = "method_35198", at = @At("HEAD"), cancellable = true)
    private static void method_35198(PiglinBruteEntity piglinBruteEntity, LivingEntity livingEntity, CallbackInfo ci) {
        if(livingEntity instanceof PlayerEntity && TheylorPlayer.get((PlayerEntity) livingEntity).hasCompetenceEnabled(TheylorCompetences.OVERLORD)) {
            ci.cancel();
        }
    }
}
