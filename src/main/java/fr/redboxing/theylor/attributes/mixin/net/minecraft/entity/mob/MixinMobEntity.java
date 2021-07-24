package fr.redboxing.theylor.attributes.mixin.net.minecraft.entity.mob;

import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class MixinMobEntity {

    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    public void setTarget(LivingEntity target, CallbackInfo ci) {
        Object mob = this;
        if((mob instanceof BlazeEntity || mob instanceof PiglinEntity || mob instanceof PiglinBruteEntity || mob instanceof WitherSkeletonEntity || mob instanceof MagmaCubeEntity ||
        mob instanceof GhastEntity || mob instanceof HoglinEntity || mob instanceof ZoglinEntity || mob instanceof ZombifiedPiglinEntity)
                && target instanceof PlayerEntity && TheylorPlayer.get((PlayerEntity) target).hasCompetenceEnabled(TheylorCompetences.OVERLORD)) {
            ci.cancel();
        }
    }
}
