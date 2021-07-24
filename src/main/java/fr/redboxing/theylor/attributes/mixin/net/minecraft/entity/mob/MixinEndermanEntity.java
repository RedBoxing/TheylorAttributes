package fr.redboxing.theylor.attributes.mixin.net.minecraft.entity.mob;

import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.interfaces.IServerPlayerEntity;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public class MixinEndermanEntity {
    @Inject(method = "isPlayerStaring", at = @At("HEAD"), cancellable = true)
    public void isPlayerStaring(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        TheylorPlayer theylorPlayer = ((IServerPlayerEntity) player).getTheylorPlayer();
        if(theylorPlayer.hasCompetenceEnabled(TheylorCompetences.ENDERMIND)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    public void setTarget(LivingEntity livingEntity, CallbackInfo ci) {
        if(livingEntity instanceof PlayerEntity) {
            TheylorPlayer theylorPlayer = ((IServerPlayerEntity) livingEntity).getTheylorPlayer();
            if(theylorPlayer.hasCompetenceEnabled(TheylorCompetences.ENDERMIND)) {
                ci.cancel();
            }
        }
    }
}
