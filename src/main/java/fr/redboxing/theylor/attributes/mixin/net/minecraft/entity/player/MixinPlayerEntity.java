package fr.redboxing.theylor.attributes.mixin.net.minecraft.entity.player;

import fr.redboxing.theylor.attributes.classes.TheylorClasses;
import fr.redboxing.theylor.attributes.competences.TheylorCompetence;
import fr.redboxing.theylor.attributes.competences.TheylorCompetences;
import fr.redboxing.theylor.attributes.events.PlayerEntityDamageCallback;
import fr.redboxing.theylor.attributes.player.TheylorPlayer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {
    @Shadow @Final private PlayerInventory inventory;

    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 0), name = "f", ordinal = 0)
    public float modifyDamage(float f, Entity target) {
        DamageSource source = DamageSource.player((PlayerEntity)(Object)this);
        return PlayerEntityDamageCallback.EVENT.invoker().interact((PlayerEntity) (Object) this, target, source, f);
    }

    @Inject(method = "damageArmor", at = @At("HEAD"), cancellable = true)
    private void damageArmor(DamageSource source, float amount, CallbackInfo ci) {
        if(TheylorPlayer.get((PlayerEntity) (Object) this).hasCompetenceEnabled(TheylorCompetences.MIDAS)) {
            ci.cancel();
        }
    }

    @Inject(method = "damageHelmet", at = @At("HEAD"), cancellable = true)
    private void damageHelmet(DamageSource source, float amount, CallbackInfo ci) {
        if(TheylorPlayer.get((PlayerEntity) (Object) this).hasCompetenceEnabled(TheylorCompetences.MIDAS)) {
            ci.cancel();
        }
    }

    @Inject(method = "canHarvest", at = @At("HEAD"), cancellable = true)
    private void canHarvest(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if(TheylorPlayer.get(((PlayerEntity) (Object) this)).hasCompetenceEnabled(TheylorCompetences.GOLD_DIGGER) && this.inventory.getMainHandStack().getItem() == Items.GOLDEN_PICKAXE) {
            cir.setReturnValue(Items.DIAMOND_PICKAXE.isSuitableFor(state));
        }
    }
}
