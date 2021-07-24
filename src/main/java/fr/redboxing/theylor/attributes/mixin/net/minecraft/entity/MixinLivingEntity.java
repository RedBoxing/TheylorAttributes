package fr.redboxing.theylor.attributes.mixin.net.minecraft.entity;

import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.events.LivingEntityDamageCallback;
import fr.redboxing.theylor.attributes.events.PlayerEntityDamageCallback;
import fr.redboxing.theylor.attributes.events.PlayerMoveCallback;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Shadow public abstract double getAttributeValue(EntityAttribute attribute);

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ActionResult result = LivingEntityDamageCallback.EVENT.invoker().interact((LivingEntity) (Object) this, source, amount);

        if(result != ActionResult.PASS) {
            cir.setReturnValue(result.isAccepted());
            cir.cancel();
        }
    }

    @Inject(method = "applyMovementEffects", at = @At("RETURN"))
    private void applyMovementEffects(BlockPos pos, CallbackInfo ci) {
        if((Object) this instanceof PlayerEntity) {
            PlayerMoveCallback.EVENT.invoker().interact((ServerPlayerEntity) (Object) this, pos, ((LivingEntity) (Object) this).getEntityWorld());
        }
    }

    @Inject(method = "getArmor", at = @At("HEAD"), cancellable = true)
    public void getArmor(CallbackInfoReturnable<Integer> cir) {
        if(((Object) this) instanceof PlayerEntity && TheylorPlayer.get((PlayerEntity) (Object)this).hasCompetenceEnabled(TheylorCompetences.GOLD_DIGGER)) {
            cir.setReturnValue(MathHelper.floor(this.getAttributeValue(EntityAttributes.GENERIC_ARMOR) + 3));
        }
    }

    @Inject(method = "setAttacking", at = @At("HEAD"), cancellable = true)
    private void setAttacking(PlayerEntity attacking, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if((entity instanceof PiglinEntity || entity instanceof PiglinBruteEntity || entity instanceof ZombifiedPiglinEntity || entity instanceof HoglinEntity || entity instanceof ZoglinEntity) && TheylorPlayer.get(attacking).hasCompetenceEnabled(TheylorCompetences.OVERLORD)) {
            ci.cancel();
        }
    }
}
